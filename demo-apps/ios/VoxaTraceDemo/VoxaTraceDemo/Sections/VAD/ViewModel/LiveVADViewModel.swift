import Foundation
import Combine
import VoxaTrace

/// ViewModel for live voice activity detection.
///
/// ## VoxaTrace Integration (~8 lines)
/// ```swift
/// // 1. Create VAD for selected backend (auto-loads bundled models)
/// let vad = CalibraVAD.create(.singingRealtime())
///
/// // 2. Process audio buffers
/// let samples16k = SonixResampler.resample(samples:fromRate:toRate:)
/// let ratio = vad.getVADRatio(samples: samples16k)
/// ```
@MainActor
final class LiveVADViewModel: ObservableObject {

    // MARK: - Configuration

    @Published var selectedBackendIndex: Int = 2 {
        didSet { recreateVAD() }
    }

    // MARK: - Published State

    @Published private(set) var vadRatio: Float = 0.0
    @Published private(set) var activityLevel: VoiceActivityLevel = .none
    @Published private(set) var isRecording = false
    @Published private(set) var voiceTimeSeconds: Float = 0.0
    @Published private(set) var silenceTimeSeconds: Float = 0.0
    @Published private(set) var lastProcessingLatencyMs: Int = 0
    @Published private(set) var waveformSamples: [WaveformSample] = []

    @Published var threshold: Float = 0.5

    // MARK: - Configuration Data

    let backends = VADBackendInfo.all
    let maxWaveformSamples = 160

    // MARK: - Computed Properties

    var apiCodeExample: String {
        let backend = backends[selectedBackendIndex].backend
        switch backend {
        case .general:
            return """
            let vad = CalibraVAD.create(.general)
            let ratio = vad.getVADRatio(samples: samples16k)
            """
        case .speech:
            return """
            // Auto-loads bundled Silero model
            let vad = CalibraVAD.create(.speech())
            let ratio = vad.getVADRatio(samples: samples16k)
            """
        case .singingRealtime:
            return """
            // Auto-loads bundled SwiftF0 model
            let vad = CalibraVAD.create(.singingRealtime())
            let ratio = vad.getVADRatio(samples: samples16k)
            """
        default:
            return "// Unknown backend"
        }
    }

    // MARK: - Private

    private var vad: CalibraVAD?
    private var recorder: SonixRecorder?

    // MARK: - Lifecycle

    func onDisappear() {
        stopRecording()
        cleanup()
    }

    // MARK: - Actions

    func toggleRecording() {
        if isRecording {
            stopRecording()
        } else {
            startRecording()
        }
    }

    // MARK: - Private Methods

    private func createVAD(for backend: VADBackend) -> CalibraVAD {
        switch backend {
        case .general:
            return CalibraVAD.create(.general)
        case .speech:
            // Auto-loads bundled Silero model
            return CalibraVAD.create(.speech())
        case .singingRealtime:
            // Auto-loads bundled SwiftF0 model
            return CalibraVAD.create(.singingRealtime())
        default:
            return CalibraVAD.create(.general)
        }
    }

    private func recreateVAD() {
        let wasRecording = isRecording
        if wasRecording {
            stopRecording()
        }

        vad?.release()
        let backend = backends[selectedBackendIndex].backend
        vad = createVAD(for: backend)

        resetStatistics()

        if wasRecording {
            startRecording()
        }
    }

    private func resetStatistics() {
        vadRatio = 0.0
        activityLevel = .none
        voiceTimeSeconds = 0.0
        silenceTimeSeconds = 0.0
        lastProcessingLatencyMs = 0
        waveformSamples.removeAll()
    }

    private func startRecording() {
        if vad == nil {
            let backend = backends[selectedBackendIndex].backend
            vad = createVAD(for: backend)
        }

        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("vad_live_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, config: .voice, audioSession: .recording)

        guard let recorder = recorder, let vad = vad else { return }

        isRecording = true
        resetStatistics()

        // ADR-017: Pass sampleRate directly; CalibraVAD handles resampling internally
        Task {
            let hwRate = AudioSessionManager.hardwareSampleRate

            for await buffer in recorder.audioBuffers {
                let startTime = CFAbsoluteTimeGetCurrent()

                let ratio = vad.getVADRatio(samples: buffer.samples, sampleRate: hwRate)

                let endTime = CFAbsoluteTimeGetCurrent()
                let latencyMs = Int((endTime - startTime) * 1000)

                if ratio < 0 {
                    continue
                }

                let amplitude = calculateRMS(samples: buffer.samples)
                let isVoice = ratio > threshold
                let frameDuration: Float = 0.032

                await MainActor.run {
                    self.vadRatio = ratio
                    self.activityLevel = self.classifyLevel(ratio: ratio)
                    self.lastProcessingLatencyMs = latencyMs

                    if isVoice {
                        self.voiceTimeSeconds += frameDuration
                    } else {
                        self.silenceTimeSeconds += frameDuration
                    }

                    let sample = WaveformSample(amplitude: amplitude, isVoice: isVoice)
                    self.waveformSamples.append(sample)
                    if self.waveformSamples.count > self.maxWaveformSamples {
                        self.waveformSamples.removeFirst()
                    }
                }
            }
        }

        recorder.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false
        vad?.reset()
    }

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        vad?.release()
        vad = nil
    }

    private func calculateRMS(samples: [Float]) -> Float {
        guard !samples.isEmpty else { return 0 }
        let sumSquares = samples.reduce(0) { $0 + $1 * $1 }
        return sqrt(sumSquares / Float(samples.count))
    }

    private func classifyLevel(ratio: Float) -> VoiceActivityLevel {
        if ratio < VADConstants.noSingingThreshold {
            return .none
        } else if ratio < VADConstants.partialSingingThreshold {
            return .partial
        } else {
            return .full
        }
    }
}
