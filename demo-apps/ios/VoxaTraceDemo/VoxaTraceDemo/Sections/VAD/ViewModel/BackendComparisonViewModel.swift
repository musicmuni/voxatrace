import Foundation
import Combine
import VoxaTrace

/// ViewModel for comparing two VAD backends side-by-side.
///
/// ## VoxaTrace Integration (~10 lines)
/// ```swift
/// // 1. Create VADs for two backends (auto-loads bundled models)
/// let vadLeft = CalibraVAD.create(.singingRealtime())
/// let vadRight = CalibraVAD.create(.speech())
///
/// // 2. Process same audio through both
/// let ratioLeft = vadLeft.getVADRatio(samples: samples16k)
/// let ratioRight = vadRight.getVADRatio(samples: samples16k)
/// ```
@MainActor
final class BackendComparisonViewModel: ObservableObject {

    // MARK: - Configuration

    @Published var leftBackendIndex: Int = 2 {
        didSet { recreateVADs() }
    }
    @Published var rightBackendIndex: Int = 0 {
        didSet { recreateVADs() }
    }

    // MARK: - Published State - Left

    @Published private(set) var vadRatioLeft: Float = 0.0
    @Published private(set) var activityLevelLeft: VoiceActivityLevel = .none
    @Published private(set) var waveformSamplesLeft: [WaveformSample] = []
    @Published private(set) var latencyMsLeft: Int = 0

    // MARK: - Published State - Right

    @Published private(set) var vadRatioRight: Float = 0.0
    @Published private(set) var activityLevelRight: VoiceActivityLevel = .none
    @Published private(set) var waveformSamplesRight: [WaveformSample] = []
    @Published private(set) var latencyMsRight: Int = 0

    // MARK: - Published State - Recording

    @Published private(set) var isRecording = false

    // MARK: - Configuration Data

    let backends = VADBackendInfo.all
    let maxWaveformSamples = 100

    // MARK: - Private

    private var vadLeft: CalibraVAD?
    private var vadRight: CalibraVAD?
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

    // MARK: - Helpers

    func colorForLevel(_ level: VoiceActivityLevel) -> Color {
        switch level {
        case .none: return .gray
        case .partial: return .orange
        case .full: return .green
        @unknown default: return .gray
        }
    }

    func textForLevel(_ level: VoiceActivityLevel) -> String {
        switch level {
        case .none: return "None"
        case .partial: return "Partial"
        case .full: return "Full"
        @unknown default: return "Unknown"
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

    private func recreateVADs() {
        let wasRecording = isRecording
        if wasRecording {
            stopRecording()
        }

        vadLeft?.release()
        vadRight?.release()

        vadLeft = createVAD(for: backends[leftBackendIndex].backend)
        vadRight = createVAD(for: backends[rightBackendIndex].backend)

        resetStatistics()

        if wasRecording {
            startRecording()
        }
    }

    private func resetStatistics() {
        vadRatioLeft = 0.0
        vadRatioRight = 0.0
        activityLevelLeft = .none
        activityLevelRight = .none
        waveformSamplesLeft.removeAll()
        waveformSamplesRight.removeAll()
        latencyMsLeft = 0
        latencyMsRight = 0
    }

    private func startRecording() {
        if vadLeft == nil {
            vadLeft = createVAD(for: backends[leftBackendIndex].backend)
        }
        if vadRight == nil {
            vadRight = createVAD(for: backends[rightBackendIndex].backend)
        }

        let tempPath = FileManager.default.temporaryDirectory
            .appendingPathComponent("vad_compare_temp.m4a").path
        recorder = SonixRecorder.create(outputPath: tempPath, config: .voice, audioSession: .recording)

        guard let recorder = recorder,
              let vadLeft = vadLeft,
              let vadRight = vadRight else { return }

        isRecording = true
        resetStatistics()

        // ADR-017: Pass sampleRate directly; CalibraVAD handles resampling internally
        Task {
            let hwRate = AudioSessionManager.hardwareSampleRate

            for await buffer in recorder.audioBuffers {
                let startLeft = CFAbsoluteTimeGetCurrent()
                let ratioLeft = vadLeft.getVADRatio(samples: buffer.samples, sampleRate: hwRate)
                let endLeft = CFAbsoluteTimeGetCurrent()
                let latencyLeft = Int((endLeft - startLeft) * 1000)

                let startRight = CFAbsoluteTimeGetCurrent()
                let ratioRight = vadRight.getVADRatio(samples: buffer.samples, sampleRate: hwRate)
                let endRight = CFAbsoluteTimeGetCurrent()
                let latencyRight = Int((endRight - startRight) * 1000)

                if ratioLeft < 0 && ratioRight < 0 {
                    continue
                }

                let amplitude = calculateRMS(samples: buffer.samples)

                await MainActor.run {
                    if ratioLeft >= 0 {
                        self.vadRatioLeft = ratioLeft
                        self.activityLevelLeft = self.classifyLevel(ratio: ratioLeft)
                        self.latencyMsLeft = latencyLeft

                        let sampleLeft = WaveformSample(amplitude: amplitude, isVoice: ratioLeft > 0.5)
                        self.waveformSamplesLeft.append(sampleLeft)
                        if self.waveformSamplesLeft.count > self.maxWaveformSamples {
                            self.waveformSamplesLeft.removeFirst()
                        }
                    }

                    if ratioRight >= 0 {
                        self.vadRatioRight = ratioRight
                        self.activityLevelRight = self.classifyLevel(ratio: ratioRight)
                        self.latencyMsRight = latencyRight

                        let sampleRight = WaveformSample(amplitude: amplitude, isVoice: ratioRight > 0.5)
                        self.waveformSamplesRight.append(sampleRight)
                        if self.waveformSamplesRight.count > self.maxWaveformSamples {
                            self.waveformSamplesRight.removeFirst()
                        }
                    }
                }
            }
        }

        recorder.start()
    }

    private func stopRecording() {
        recorder?.stop()
        isRecording = false
        vadLeft?.reset()
        vadRight?.reset()
    }

    private func cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = nil

        vadLeft?.release()
        vadLeft = nil

        vadRight?.release()
        vadRight = nil
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

import SwiftUI

extension BackendComparisonViewModel {
    // SwiftUI Color helper that needs to be in an extension with SwiftUI import
}
