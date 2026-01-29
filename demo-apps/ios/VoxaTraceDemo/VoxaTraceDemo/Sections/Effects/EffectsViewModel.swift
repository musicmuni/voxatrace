import Foundation
import Combine
import VoxaTrace

/// ViewModel for audio effects using CalibraEffectsConfig.Builder.
///
/// ## VoxaTrace Integration (~15 lines)
/// ```swift
/// // 1. Build effects config and create chain (ADR-001: Builder builds Config)
/// let config = CalibraEffectsConfig.Builder()
///     .addNoiseGate(thresholdDb: threshold, holdTimeMs: 100, timeConstMs: 10)
///     .addCompressor(thresholdDb: threshold, ratio: ratio)
///     .addReverb(mix: mix, roomSize: roomSize)
///     .build()
/// effects = CalibraEffects.create(config: config)
///
/// // 2. Apply to audio (via SonixPlayer processing tap)
/// player.setProcessingTap { samples in
///     effects.process(samples: &samples)
/// }
///
/// // 3. Adjust parameters in realtime
/// effects.setReverbMix(mix:)
/// effects.setCompressorThreshold(thresholdDb:)
/// ```
@MainActor
final class EffectsViewModel: ObservableObject {

    // MARK: - Published State

    @Published private(set) var isRecording = false
    @Published private(set) var isPlaying = false
    @Published private(set) var status = "Ready"
    @Published var selectedPresetIndex = 0

    // Effect parameters
    @Published var reverbMix: Float = 0.3
    @Published var reverbRoomSize: Float = 0.5
    @Published var compressorThreshold: Float = -20.0
    @Published var compressorRatio: Float = 4.0
    @Published var noiseGateThreshold: Float = -40.0

    let presetNames = ["Vocal Chain", "Practice", "Recording", "Dry", "Wet", "Clean"]

    // MARK: - Private

    private var player: SonixPlayer?
    private var recorder: SonixRecorder?
    private var effects: CalibraEffects?
    private var playerObserverTask: Task<Void, Never>?
    private var audioTask: Task<Void, Never>?

    // MARK: - Lifecycle

    func onDisappear() {
        cleanupAudio()
    }

    // MARK: - Actions

    func toggleRecording() {
        if isRecording {
            stopRecording()
        } else {
            startRecording()
        }
    }

    func togglePlayback() {
        if isPlaying {
            pausePlayback()
        } else {
            startPlayback()
        }
    }

    func applyPreset() {
        switch selectedPresetIndex {
        case 0: // Vocal Chain
            noiseGateThreshold = -45.0
            compressorThreshold = -20.0
            compressorRatio = 3.0
            reverbMix = 0.25
            reverbRoomSize = 0.5
        case 1: // Practice
            noiseGateThreshold = -50.0
            compressorThreshold = -18.0
            compressorRatio = 2.0
            reverbMix = 0.0
            reverbRoomSize = 0.3
        case 2: // Recording
            noiseGateThreshold = -40.0
            compressorThreshold = -24.0
            compressorRatio = 6.0
            reverbMix = 0.15
            reverbRoomSize = 0.4
        case 3: // Dry
            noiseGateThreshold = -80.0
            compressorThreshold = -20.0
            compressorRatio = 4.0
            reverbMix = 0.0
            reverbRoomSize = 0.0
        case 4: // Wet
            noiseGateThreshold = -80.0
            compressorThreshold = -20.0
            compressorRatio = 1.0
            reverbMix = 0.5
            reverbRoomSize = 0.7
        case 5: // Clean
            noiseGateThreshold = -40.0
            compressorThreshold = -60.0
            compressorRatio = 1.0
            reverbMix = 0.0
            reverbRoomSize = 0.0
        default:
            break
        }

        rebuildEffectsIfNeeded()
    }

    func updateReverbMix(_ value: Float) {
        reverbMix = value
        effects?.setReverbMix(mix: value)
    }

    func updateReverbRoomSize(_ value: Float) {
        reverbRoomSize = value
        effects?.setReverbRoomSize(roomSize: value)
    }

    func updateCompressorThreshold(_ value: Float) {
        compressorThreshold = value
        effects?.setCompressorThreshold(thresholdDb: value)
    }

    func updateCompressorRatio(_ value: Float) {
        compressorRatio = value
        effects?.setCompressorRatio(ratio: value)
    }

    func updateNoiseGateThreshold(_ value: Float) {
        noiseGateThreshold = value
        effects?.setNoiseGateThreshold(thresholdDb: value)
    }

    // MARK: - Private Methods

    private func setupAudioIfNeeded() {
        guard effects == nil else { return }

        // Build effects config and create chain (ADR-001: Builder builds Config)
        let config = CalibraEffectsConfig.Builder()
            .addNoiseGate(thresholdDb: noiseGateThreshold, holdTimeMs: 100, timeConstMs: 10)
            .addCompressor(
                thresholdDb: compressorThreshold,
                ratio: compressorRatio,
                attackMs: 10,
                releaseMs: 100,
                autoMakeup: false,
                makeupDb: 0
            )
            .addReverb(mix: reverbMix, roomSize: reverbRoomSize)
            .build()
        effects = CalibraEffects.create(config: config)

        // Create recorder
        let recordingPath = getAudioFileURL().path
        let recorderConfig = SonixRecorderConfig.Builder().preset(.voice).format(.wav).build()
        recorder = SonixRecorder.create(outputPath: recordingPath, config: recorderConfig, audioSession: .recording)
    }

    private func rebuildEffectsIfNeeded() {
        guard effects != nil else { return }

        let oldEffects = effects

        // Build effects config and create chain (ADR-001: Builder builds Config)
        let config = CalibraEffectsConfig.Builder()
            .addNoiseGate(thresholdDb: noiseGateThreshold, holdTimeMs: 100, timeConstMs: 10)
            .addCompressor(
                thresholdDb: compressorThreshold,
                ratio: compressorRatio,
                attackMs: 10,
                releaseMs: 100,
                autoMakeup: false,
                makeupDb: 0
            )
            .addReverb(mix: reverbMix, roomSize: reverbRoomSize)
            .build()
        effects = CalibraEffects.create(config: config)

        // Reinstall processing tap if playing
        if isPlaying {
            player?.setProcessingTap { [weak self] samples in
                self?.effects?.process(samples: &samples)
            }
        }

        oldEffects?.release()
    }

    private func cleanupAudio() {
        audioTask?.cancel()
        audioTask = nil
        playerObserverTask?.cancel()
        player?.setProcessingTap(nil)
        player?.stop()
        player?.release()
        player = nil

        recorder?.stop()
        recorder?.release()
        recorder = nil

        effects?.release()
        effects = nil
    }

    private func startRecording() {
        isRecording = true
        status = "Setting up..."

        Task {
            setupAudioIfNeeded()

            guard let recorder = recorder else {
                isRecording = false
                status = "Setup failed"
                return
            }

            status = "Recording..."

            audioTask = Task {
                for await buffer in recorder.audioBuffers {
                    _ = buffer.samples
                }
            }

            recorder.start()
        }
    }

    private func stopRecording() {
        audioTask?.cancel()
        audioTask = nil
        recorder?.stop()
        isRecording = false

        // Release player so it reloads the new recording
        playerObserverTask?.cancel()
        playerObserverTask = nil
        player?.stop()
        player?.release()
        player = nil

        status = "Recording saved"
    }

    private func startPlayback() {
        setupAudioIfNeeded()

        let audioPath = getAudioFileURL().path

        guard FileManager.default.fileExists(atPath: audioPath) else {
            status = "No recording found. Record first!"
            return
        }

        status = "Loading..."

        Task {
            do {
                if player == nil {
                    player = try await SonixPlayer.create(source: audioPath, audioSession: .playback)

                    playerObserverTask = player?.observeIsPlaying { [weak self] playing in
                        if !playing && self?.isPlaying == true {
                            self?.isPlaying = false
                            self?.status = "Playback complete"
                        }
                    }
                }

                // Install processing tap for real-time effects
                player?.setProcessingTap { [weak self] samples in
                    self?.effects?.process(samples: &samples)
                }

                player?.play()
                isPlaying = true
                status = "Playing with effects..."
            } catch {
                status = "Error: \(error.localizedDescription)"
            }
        }
    }

    private func pausePlayback() {
        player?.pause()
        isPlaying = false
        status = "Paused"
    }

    private func getAudioFileURL() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0].appendingPathComponent("recording.wav")
    }
}
