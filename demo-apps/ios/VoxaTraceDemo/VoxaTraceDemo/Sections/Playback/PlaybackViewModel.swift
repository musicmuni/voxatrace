import Foundation
import Combine
import VoxaTrace

/// ViewModel for audio playback using SonixPlayer.
///
/// ## VoxaTrace Integration (~20 lines)
/// ```swift
/// // 1. Create player with Config + Factory pattern
/// let config = SonixPlayerConfig.Builder()
///     .volume(volume).pitch(pitch).loopCount(loopCount)
///     .onComplete { }.onError { }.build()
/// player = try await SonixPlayer.create(source: path, config: config, audioSession: .playback)
///
/// // 2. Observe state
/// player.observeIsPlaying { }
/// player.observeCurrentTime { }
///
/// // 3. Control playback
/// player.play() / player.pause() / player.stop() / player.seek(positionMs:)
/// ```
@MainActor
final class PlaybackViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var isPlaying = false
    @Published private(set) var currentTimeMs: Int64 = 0
    @Published private(set) var durationMs: Int64 = 0
    @Published private(set) var isLoaded = false
    @Published private(set) var status = "Ready"
    @Published var volume: Float = 0.8
    @Published var pitch: Float = 0
    @Published var loopCount: Int32 = 1

    // MARK: - Computed Properties

    var seekProgress: Double {
        durationMs > 0 ? Double(currentTimeMs) / Double(durationMs) : 0
    }

    var formattedCurrentTime: String { formatTime(currentTimeMs) }
    var formattedDuration: String { formatTime(durationMs) }

    // MARK: - Private

    private var player: SonixPlayer?
    private var playingTask: Task<Void, Never>?
    private var timeTask: Task<Void, Never>?

    // MARK: - Init

    init() {
        // Defer async initialization using DispatchQueue (Task @MainActor doesn't update UI properly)
        DispatchQueue.main.async { [weak self] in
            self?.loadPlayer()
        }
    }

    private func loadPlayer() {
        status = "Loading..."

        Task { [weak self] in
            guard let self = self else { return }
            await self.doInitializePlayer()
        }
    }

    private func doInitializePlayer() async {
        guard let assetPath = copyAssetToFile(name: "sample", ext: "m4a") else {
            Log.e(.playback, "Asset 'sample.m4a' not found in bundle")
            DispatchQueue.main.async { [weak self] in
                self?.status = "Asset not found"
            }
            return
        }

        do {
            let config = SonixPlayerConfig.Builder()
                .volume(volume)
                .pitch(pitch)
                .loopCount(loopCount)
                .onComplete { [weak self] in
                    Log.i(.playback, "Playback completed")
                    Task { @MainActor in
                        self?.status = "Playback complete"
                    }
                }
                .onLoopComplete { loopIndex, totalLoops in
                    Log.i(.playback, "Loop \(loopIndex + 1)/\(totalLoops) completed")
                }
                .onError { error in
                    Log.e(.playback, "Playback error: \(error)")
                }
                .build()

            let newPlayer = try await SonixPlayer.create(source: assetPath, config: config, audioSession: .playback)
            Log.i(.playback, "Player initialized: duration=\(newPlayer.duration)ms")

            DispatchQueue.main.async { [weak self] in
                self?.player = newPlayer
                self?.durationMs = newPlayer.duration
                self?.isLoaded = true
                self?.status = "Loaded: sample.m4a"
                self?.observePlaybackState(newPlayer)
            }
        } catch {
            Log.e(.playback, "Failed to create player", error: error)
            DispatchQueue.main.async { [weak self] in
                self?.status = "Error: \(error.localizedDescription)"
            }
        }
    }

    // MARK: - Lifecycle

    func onAppear() {
        // Initialization now happens in init
    }

    func onDisappear() {
        cancelObservers()
        player?.release()
        player = nil
    }

    // MARK: - Actions

    func play() {
        player?.play()
    }

    func pause() {
        player?.pause()
    }

    func stop() {
        player?.stop()
        currentTimeMs = 0
    }

    func seek(to progress: Double) {
        let seekPos = Int64(progress * Double(durationMs))
        player?.seek(positionMs: seekPos)
    }

    func setVolume(_ newVolume: Float) {
        volume = newVolume
        player?.volume = newVolume
    }

    func setPitch(_ newPitch: Float) {
        pitch = newPitch
        player?.pitch = newPitch
    }

    func setLoopCount(_ count: Int32) {
        loopCount = count
        player?.loopCount = count
    }

    func fadeIn() {
        Task {
            try? await player?.fadeIn(targetVolume: 1.0, durationMs: 1000)
        }
    }

    func fadeOut() {
        Task {
            try? await player?.fadeOut(durationMs: 1000)
        }
    }

    // MARK: - Private Methods

    private func observePlaybackState(_ player: SonixPlayer) {
        playingTask = player.observeIsPlaying { [weak self] playing in
            self?.isPlaying = playing
        }
        timeTask = player.observeCurrentTime { [weak self] time in
            self?.currentTimeMs = time
        }
    }

    private func cancelObservers() {
        playingTask?.cancel()
        timeTask?.cancel()
    }

    private func formatTime(_ ms: Int64) -> String {
        let seconds = (ms / 1000) % 60
        let minutes = (ms / 1000) / 60
        return String(format: "%d:%02d", minutes, seconds)
    }
}
