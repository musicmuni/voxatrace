import Foundation
import Combine
import VoxaTrace

/// ViewModel for audio recording using SonixRecorder.
///
/// ## VoxaTrace Integration (~25 lines)
/// ```swift
/// // 1. Create recorder with Config + Factory pattern
/// let config = SonixRecorderConfig.Builder()
///     .preset(.voice).sampleRate(16000).channels(1).bitrate(128000)
///     .onRecordingStarted { }.onRecordingStopped { }.onError { }.build()
/// recorder = SonixRecorder.create(outputPath: path, config: config, audioSession: .recording)
///
/// // 2. Observe state
/// recorder.observeIsRecording { }
/// recorder.observeDuration { }
/// recorder.observeLevel { }
///
/// // 3. Control recording
/// recorder.start() / recorder.stop()
/// ```
@MainActor
final class RecordingViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var isRecording = false
    @Published private(set) var durationMs: Int64 = 0
    @Published private(set) var audioLevel: Float = 0
    @Published private(set) var savedFilePath: String?
    @Published private(set) var status = "Ready"
    @Published var selectedFormat = "m4a"

    // Buffer Pool metrics (for Calibra integration)
    @Published private(set) var bufferPoolAvailable: Int32 = 4
    @Published private(set) var bufferPoolWasExhausted = false

    // Playback state
    @Published private(set) var isPlaying = false
    @Published private(set) var playbackTimeMs: Int64 = 0
    @Published private(set) var playbackDurationMs: Int64 = 0

    // MARK: - Computed Properties

    let formats = ["m4a", "mp3"]

    var formattedDuration: String { formatDuration(durationMs) }
    var formattedPlaybackTime: String { formatDuration(playbackTimeMs) }
    var formattedPlaybackDuration: String { formatDuration(playbackDurationMs) }

    var canPlay: Bool { savedFilePath != nil && !isRecording && !isPlaying }

    // MARK: - Private

    private var recorder: SonixRecorder?
    private var player: SonixPlayer?
    private var recorderObservers = ObserverBag()
    private var playerObservers = ObserverBag()

    // MARK: - Lifecycle

    func onDisappear() {
        recorderObservers.cancelAll()
        playerObservers.cancelAll()
        recorder?.release()
        player?.release()
    }

    // MARK: - Recording Actions

    func startRecording() {
        Task {
            let granted = await AudioSessionManager.requestMicrophonePermission()
            guard granted else {
                status = "Microphone permission denied"
                return
            }

            status = "Starting..."

            let documentsPath = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0]
            let outputPath = documentsPath.appendingPathComponent("recording_\(Int(Date().timeIntervalSince1970)).\(selectedFormat)").path

            // Create recorder using Config + Factory pattern
            let recorderConfig = SonixRecorderConfig.Builder()
                .preset(.voice)
                .sampleRate(16000)
                .channels(1)
                .bitrate(128000)
                .onRecordingStarted {
                    Log.i(.recording, "Recording started")
                }
                .onRecordingStopped { path in
                    Log.i(.recording, "Recording saved: \(path)")
                }
                .onError { error in
                    Log.e(.recording, "Recording error: \(error)")
                }
                .build()
            let newRecorder = SonixRecorder.create(outputPath: outputPath, config: recorderConfig, audioSession: .recording)

            recorder = newRecorder
            savedFilePath = outputPath

            newRecorder.start()

            isRecording = true
            status = "Recording (\(selectedFormat.uppercased()))"

            setupRecorderObservers(newRecorder)
        }
    }

    func stopRecording() {
        recorder?.stop()
        isRecording = false
        audioLevel = 0
        status = "Saved"
    }

    // MARK: - Playback Actions

    func playRecording() {
        guard let path = savedFilePath else { return }

        Task {
            playerObservers.cancelAll()
            player?.release()

            do {
                let newPlayer = try await SonixPlayer.create(source: path, audioSession: .playback)
                player = newPlayer
                playbackDurationMs = newPlayer.duration

                setupPlayerObservers(newPlayer)

                newPlayer.play()
                status = "Playing recording"
            } catch {
                status = "Player error: \(error.localizedDescription)"
            }
        }
    }

    func pausePlayback() {
        player?.pause()
        status = "Paused"
    }

    func stopPlayback() {
        player?.stop()
        playbackTimeMs = 0
        status = "Stopped"
    }

    // MARK: - Private Methods

    private func setupRecorderObservers(_ recorder: SonixRecorder) {
        recorderObservers.cancelAll()
        recorderObservers.add(recorder.observeIsRecording { [weak self] recording in
            self?.isRecording = recording
        })
        recorderObservers.add(recorder.observeDuration { [weak self] duration in
            self?.durationMs = duration
        })
        recorderObservers.add(recorder.observeLevel { [weak self] level in
            self?.audioLevel = level
            self?.bufferPoolAvailable = recorder.bufferPoolAvailable
            self?.bufferPoolWasExhausted = recorder.bufferPoolWasExhausted
        })
        recorderObservers.add(recorder.observeError { [weak self] error in
            if let err = error {
                self?.status = "Error: \(err.message)"
            }
        })
    }

    private func setupPlayerObservers(_ player: SonixPlayer) {
        playerObservers.add(player.observeIsPlaying { [weak self] playing in
            self?.isPlaying = playing
        })
        playerObservers.add(player.observeCurrentTime { [weak self] time in
            self?.playbackTimeMs = time
        })
    }

    private func formatDuration(_ ms: Int64) -> String {
        let seconds = (ms / 1000) % 60
        let minutes = (ms / 1000) / 60
        return String(format: "%02d:%02d", minutes, seconds)
    }
}

// MARK: - Observer Bag

/// Utility for managing multiple observer Task handles.
final class ObserverBag {
    private var tasks: [Task<Void, Never>] = []

    func add(_ task: Task<Void, Never>) {
        tasks.append(task)
    }

    func cancelAll() {
        tasks.forEach { $0.cancel() }
        tasks.removeAll()
    }
}
