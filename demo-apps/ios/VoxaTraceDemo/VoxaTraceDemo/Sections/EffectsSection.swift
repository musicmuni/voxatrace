import SwiftUI
import VoxaTrace

/// Audio Effects Playground using CalibraEffects unified API.
///
/// Demonstrates the new builder pattern for audio effects with:
/// - Combo presets (VOCAL_CHAIN, PRACTICE, RECORDING)
/// - Individual effect presets
/// - Runtime parameter updates
///
/// Uses Sonix for recording and playback with real-time effects processing.
struct EffectsSection: View {
    @State private var isRecording = false
    @State private var isPlaying = false

    // Sonix components
    @State private var player: SonixPlayer?
    @State private var recorder: SonixRecorder?
    @State private var playerObserverTask: Task<Void, Never>?
    @State private var audioTask: Task<Void, Never>?

    // Preset selection
    @State private var selectedPresetIndex = 0

    // Effect parameters
    @State private var reverbMix: Float = 0.3
    @State private var reverbRoomSize: Float = 0.5
    @State private var compressorThreshold: Float = -20.0
    @State private var compressorRatio: Float = 4.0
    @State private var noiseGateThreshold: Float = -40.0

    // Unified CalibraEffects chain
    @State private var effects: CalibraEffects?

    // Status for user feedback
    @State private var status = "Ready"

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Audio Effects Playground")
                .font(.headline)

            Text(status)
                .font(.caption)
                .foregroundColor(.secondary)

            // Preset selector
            VStack(alignment: .leading, spacing: 4) {
                Text("Preset:")
                    .font(.caption)
                    .foregroundColor(.secondary)

                Picker("Preset", selection: $selectedPresetIndex) {
                    ForEach(0..<presetNames.count, id: \.self) { index in
                        Text(presetNames[index]).tag(index)
                    }
                }
                .pickerStyle(.segmented)
                .onChange(of: selectedPresetIndex) { _ in
                    applyPreset()
                }
            }

            HStack {
                Button(isRecording ? "Stop" : "Record") {
                    if isRecording {
                        stopRecording()
                    } else {
                        startRecording()
                    }
                }
                .buttonStyle(.borderedProminent)
                .disabled(isPlaying)

                Button(isPlaying ? "Pause" : "Play") {
                    if isPlaying {
                        pausePlayback()
                    } else {
                        startPlayback()
                    }
                }
                .buttonStyle(.bordered)
                .disabled(isRecording)
            }

            // Reverb
            Text("Reverb")
                .font(.subheadline)
                .fontWeight(.medium)
            Text("Mix: \(String(format: "%.2f", reverbMix))")
                .font(.caption)
            Slider(value: $reverbMix, in: 0...1)
                .onChange(of: reverbMix) { newValue in
                    effects?.setReverbMix(mix: newValue)
                }
            Text("Room Size: \(String(format: "%.2f", reverbRoomSize))")
                .font(.caption)
            Slider(value: $reverbRoomSize, in: 0...1)
                .onChange(of: reverbRoomSize) { newValue in
                    effects?.setReverbRoomSize(roomSize: newValue)
                }

            // Compressor
            Text("Compressor")
                .font(.subheadline)
                .fontWeight(.medium)
            Text("Threshold: \(String(format: "%.1f", compressorThreshold)) dB")
                .font(.caption)
            Slider(value: $compressorThreshold, in: -60...0)
                .onChange(of: compressorThreshold) { newValue in
                    effects?.setCompressorThreshold(thresholdDb: newValue)
                }
            Text("Ratio: \(String(format: "%.1f", compressorRatio)):1")
                .font(.caption)
            Slider(value: $compressorRatio, in: 1...20)
                .onChange(of: compressorRatio) { newValue in
                    effects?.setCompressorRatio(ratio: newValue)
                }

            // Noise Gate
            Text("Noise Gate")
                .font(.subheadline)
                .fontWeight(.medium)
            Text("Threshold: \(String(format: "%.1f", noiseGateThreshold)) dB")
                .font(.caption)
            Slider(value: $noiseGateThreshold, in: -80...0)
                .onChange(of: noiseGateThreshold) { newValue in
                    effects?.setNoiseGateThreshold(thresholdDb: newValue)
                }
        }
        .onDisappear(perform: cleanupAudio)
    }

    private let presetNames = ["Vocal Chain", "Practice", "Recording", "Dry", "Wet", "Clean"]

    private func applyPreset() {
        // Update slider values based on preset
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

        // Recreate effects with new values if already initialized
        if effects != nil {
            let oldEffects = effects  // Keep reference to old effects

            let builder = CalibraEffects.Builder()
            _ = builder.addNoiseGate(thresholdDb: noiseGateThreshold, holdTimeMs: 100, timeConstMs: 10)
            _ = builder.addCompressor(thresholdDb: compressorThreshold, ratio: compressorRatio)
            _ = builder.addReverb(mix: reverbMix, roomSize: reverbRoomSize)
            effects = builder.build()

            // Reinstall processing tap if playing so it uses new effects
            if isPlaying {
                player?.setProcessingTap { [self] samples in
                    effects?.process(samples: &samples)
                }
            }

            // NOW safe to release old effects (tap is using new one)
            oldEffects?.release()
        }
    }

    private func setupAudioIfNeeded() {
        guard effects == nil else { return }

        // Build effects chain
        let builder = CalibraEffects.Builder()
        _ = builder.addNoiseGate(thresholdDb: noiseGateThreshold, holdTimeMs: 100, timeConstMs: 10)
        _ = builder.addCompressor(thresholdDb: compressorThreshold, ratio: compressorRatio)
        _ = builder.addReverb(mix: reverbMix, roomSize: reverbRoomSize)
        effects = builder.build()

        // Create Sonix recorder
        let recordingPath = getAudioFileURL().path
        recorder = SonixRecorder.create(outputPath: recordingPath, format: "wav", quality: "voice")
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
            await MainActor.run {
                setupAudioIfNeeded()
            }

            guard let recorder = recorder else {
                await MainActor.run {
                    isRecording = false
                    status = "Setup failed"
                }
                return
            }

            await MainActor.run {
                status = "Recording..."
            }

            // Launch async audio buffer consumption loop
            audioTask = Task {
                for await buffer in recorder.audioBuffers {
                    // Must access floatSamples to properly consume the buffer
                    // Effects are applied during playback, not recording
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

        // Check if recording exists
        guard FileManager.default.fileExists(atPath: audioPath) else {
            status = "No recording found. Record first!"
            return
        }

        status = "Loading..."

        Task {
            do {
                // Create player if needed
                if player == nil {
                    player = try await SonixPlayer.create(source: audioPath)

                    // Observe playback state
                    playerObserverTask = player?.observeIsPlaying { playing in
                        if !playing && isPlaying {
                            // Playback completed
                            isPlaying = false
                            status = "Playback complete"
                        }
                    }
                }

                // Install processing tap for real-time effects
                player?.setProcessingTap { samples in
                    // Apply effects (modifies in-place)
                    effects?.process(samples: &samples)
                }

                player?.play()
                await MainActor.run {
                    isPlaying = true
                    status = "Playing with effects..."
                }
            } catch {
                await MainActor.run {
                    status = "Error: \(error.localizedDescription)"
                }
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
