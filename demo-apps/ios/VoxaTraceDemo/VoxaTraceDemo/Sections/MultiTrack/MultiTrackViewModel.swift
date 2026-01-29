import Foundation
import Combine
import VoxaTrace

/// ViewModel for multi-track mixing using SonixMixer.
///
/// ## VoxaTrace Integration (~20 lines)
/// ```swift
/// // 1. Create mixer with Config + Factory pattern
/// let config = SonixMixerConfig.Builder()
///     .loopCount(1)
///     .onPlaybackComplete { }
///     .onLoopComplete { }
///     .onError { }
///     .build()
/// mixer = SonixMixer.create(config: config, audioSession: .playback)
///
/// // 2. Add tracks (auto-decodes files)
/// try await mixer.addTrack(name: "backing", filePath: path)
///
/// // 3. Observe state
/// mixer.observeIsPlaying { }
/// mixer.observeCurrentTime { }
///
/// // 4. Control
/// mixer.play() / mixer.pause() / mixer.stop()
/// mixer.setTrackVolume(name:, volume:)
/// mixer.fadeTrackVolume(name:, targetVolume:, durationMs:)
/// ```
@MainActor
final class MultiTrackViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var isPlaying = false
    @Published private(set) var currentTimeMs: Int64 = 0
    @Published private(set) var durationMs: Int64 = 0
    @Published private(set) var isLoaded = false
    @Published private(set) var status = "Ready"
    @Published var backingVolume: Float = 0.8
    @Published var vocalVolume: Float = 1.0

    // MARK: - Computed Properties

    var seekProgress: Double {
        durationMs > 0 ? Double(currentTimeMs) / Double(durationMs) : 0
    }

    var formattedCurrentTime: String { formatTime(currentTimeMs) }
    var formattedDuration: String { formatTime(durationMs) }

    // MARK: - Private

    private var mixer: SonixMixer?
    private var playingTask: Task<Void, Never>?
    private var timeTask: Task<Void, Never>?
    private var errorTask: Task<Void, Never>?

    // MARK: - Lifecycle

    func onAppear() async {
        await initializeMixer()
    }

    func onDisappear() {
        cancelObservers()
        mixer?.release()
    }

    // MARK: - Actions

    func play() {
        mixer?.play()
    }

    func pause() {
        mixer?.pause()
    }

    func stop() {
        mixer?.stop()
        currentTimeMs = 0
    }

    func seek(to progress: Double) {
        let seekPos = Int64(progress * Double(durationMs))
        mixer?.seek(positionMs: seekPos)
    }

    func setBackingVolume(_ volume: Float) {
        backingVolume = volume
        mixer?.setTrackVolume(name: "backing", volume: volume)
    }

    func setVocalVolume(_ volume: Float) {
        vocalVolume = volume
        mixer?.setTrackVolume(name: "vocal", volume: volume)
    }

    func fadeVocalDown() {
        mixer?.fadeTrackVolume(name: "vocal", targetVolume: 0.2, durationMs: 500)
        vocalVolume = 0.2
    }

    func fadeVocalUp() {
        mixer?.fadeTrackVolume(name: "vocal", targetVolume: 1.0, durationMs: 500)
        vocalVolume = 1.0
    }

    // MARK: - Private Methods

    private func initializeMixer() async {
        status = "Loading tracks..."

        guard let backingPath = copyAssetToFile(name: "sample", ext: "m4a"),
              let vocalPath = copyAssetToFile(name: "vocal", ext: "m4a") else {
            status = "Assets not found"
            return
        }

        // Create mixer with Config + Factory pattern
        let mixerConfig = SonixMixerConfig.Builder()
            .loopCount(1)
            .onPlaybackComplete { [weak self] in
                Log.i(.mixer, "Playback complete")
                Task { @MainActor in
                    self?.status = "Playback complete"
                }
            }
            .onLoopComplete { loopIndex in
                Log.i(.mixer, "Loop \(loopIndex) completed")
            }
            .onError { [weak self] error in
                Log.e(.mixer, "Playback error: \(error)")
                Task { @MainActor in
                    self?.status = "Error: \(error)"
                }
            }
            .build()
        let newMixer = SonixMixer.create(config: mixerConfig, audioSession: .playback)

        // Auto-decode tracks
        let backingSuccess = try? await newMixer.addTrack(name: "backing", filePath: backingPath)
        let vocalSuccess = try? await newMixer.addTrack(name: "vocal", filePath: vocalPath)

        mixer = newMixer

        if backingSuccess == true && vocalSuccess == true {
            durationMs = newMixer.duration
            isLoaded = true
            status = "Loaded 2 tracks"
            Log.i(.mixer, "Multi-track loaded: duration=\(newMixer.duration)ms, tracks=2")

            setupObservers(newMixer)
        } else {
            Log.e(.mixer, "Failed to load tracks: backing=\(backingSuccess ?? false), vocal=\(vocalSuccess ?? false)")
            status = "Failed to load tracks"
        }
    }

    private func setupObservers(_ mixer: SonixMixer) {
        playingTask = mixer.observeIsPlaying { [weak self] playing in
            self?.isPlaying = playing
        }
        timeTask = mixer.observeCurrentTime { [weak self] time in
            self?.currentTimeMs = time
        }
        errorTask = mixer.observeError { [weak self] error in
            if let error = error {
                self?.status = "Error: \(error.message)"
            }
        }
    }

    private func cancelObservers() {
        playingTask?.cancel()
        timeTask?.cancel()
        errorTask?.cancel()
    }

    private func formatTime(_ ms: Int64) -> String {
        let seconds = (ms / 1000) % 60
        let minutes = (ms / 1000) / 60
        return String(format: "%d:%02d", minutes, seconds)
    }
}
