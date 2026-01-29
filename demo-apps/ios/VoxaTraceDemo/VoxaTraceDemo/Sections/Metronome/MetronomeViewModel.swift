import Foundation
import Combine
import VoxaTrace

/// ViewModel for metronome using SonixMetronome.Builder.
///
/// ## VoxaTrace Integration (~20 lines)
/// ```swift
/// // 1. Create metronome with Builder pattern
/// metronome = SonixMetronome.Builder()
///     .samaSamplePath(path: samaPath)
///     .beatSamplePath(path: beatPath)
///     .bpm(bpm: bpm)
///     .beatsPerCycle(count: beats)
///     .volume(volume: volume)
///     .build()
///
/// // 2. Observe state
/// metronome.observeIsInitialized { }
/// metronome.observeCurrentBeat { }
/// metronome.observeIsPlaying { }
///
/// // 3. Control
/// metronome.start() / metronome.stop()
/// metronome.setBpm(bpm:) / metronome.volume = newVolume
/// ```
@MainActor
final class MetronomeViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var isRunning = false
    @Published private(set) var isInitialized = false
    @Published private(set) var currentBeat: Int32 = 0
    @Published private(set) var status = "Ready"
    @Published var bpm: Float = 120
    @Published var beatsPerCycle: Int32 = 4
    @Published var volume: Float = 0.8

    // MARK: - Private

    private var metronome: SonixMetronome?
    private var beatTask: Task<Void, Never>?
    private var playingTask: Task<Void, Never>?
    private var initializedTask: Task<Void, Never>?
    private var errorTask: Task<Void, Never>?
    private var samaSamplePath: String?
    private var beatSamplePath: String?

    // MARK: - Lifecycle

    func onAppear() async {
        await initializeMetronome()
    }

    func onDisappear() {
        cancelObservers()
        metronome?.release()
    }

    // MARK: - Actions

    func start() {
        metronome?.start()
        status = "Running"
    }

    func stop() {
        metronome?.stop()
        status = "Stopped"
        currentBeat = 0
    }

    func setBpm(_ newBpm: Float) {
        bpm = newBpm
        metronome?.setBpm(bpm: newBpm)
    }

    func setVolume(_ newVolume: Float) {
        volume = newVolume
        metronome?.volume = newVolume
    }

    func setBeatsPerCycle(_ newBeats: Int32) {
        guard newBeats != beatsPerCycle else { return }
        beatsPerCycle = newBeats
        reinitializeMetronome(withBeats: newBeats)
    }

    // MARK: - Private Methods

    private func initializeMetronome() async {
        guard let samaPath = copyAssetToFile(name: "sama_click", ext: "wav"),
              let beatPath = copyAssetToFile(name: "beat_click", ext: "wav") else {
            status = "Click samples not found"
            return
        }

        samaSamplePath = samaPath
        beatSamplePath = beatPath

        // Create metronome with Builder pattern
        let newMetronome = SonixMetronome.Builder()
            .samaSamplePath(path: samaPath)
            .beatSamplePath(path: beatPath)
            .bpm(bpm: bpm)
            .beatsPerCycle(count: beatsPerCycle)
            .volume(volume: volume)
            .build()

        metronome = newMetronome
        status = "Initializing audio..."

        setupObservers(newMetronome)
    }

    private func reinitializeMetronome(withBeats newBeats: Int32) {
        guard let samaPath = samaSamplePath,
              let beatPath = beatSamplePath else {
            return
        }

        Task {
            cancelObservers()
            metronome?.release()

            isInitialized = false
            status = "Reinitializing..."

            let newMetronome = SonixMetronome.Builder()
                .samaSamplePath(path: samaPath)
                .beatSamplePath(path: beatPath)
                .bpm(bpm: bpm)
                .beatsPerCycle(count: newBeats)
                .volume(volume: volume)
                .build()

            metronome = newMetronome
            currentBeat = 0

            setupObservers(newMetronome)
        }
    }

    private func setupObservers(_ metronome: SonixMetronome) {
        initializedTask = metronome.observeIsInitialized { [weak self] initialized in
            self?.isInitialized = initialized
            if initialized {
                self?.status = "Ready"
            }
        }
        beatTask = metronome.observeCurrentBeat { [weak self] beat in
            self?.currentBeat = beat
        }
        playingTask = metronome.observeIsPlaying { [weak self] playing in
            self?.isRunning = playing
        }
        errorTask = metronome.observeError { [weak self] error in
            if let err = error {
                self?.status = "Error: \(err.message)"
            }
        }
    }

    private func cancelObservers() {
        beatTask?.cancel()
        playingTask?.cancel()
        initializedTask?.cancel()
        errorTask?.cancel()
    }
}
