import Foundation
import VoxaTrace

// MARK: - Constants

/// VAD threshold constants.
enum VADConstants {
    static let noSingingThreshold: Float = 0.3
    static let partialSingingThreshold: Float = 0.6
}

// MARK: - Waveform Sample

/// Sample data for waveform visualization.
struct WaveformSample {
    let amplitude: Float
    let isVoice: Bool
}

// MARK: - Backend Info

/// Backend information for display.
struct VADBackendInfo {
    let backend: VADBackend
    let name: String
    let description: String
    let guidance: String
    let latency: String

    static let all: [VADBackendInfo] = [
        VADBackendInfo(
            backend: .speech,
            name: "Speech",
            description: "Silero neural network",
            guidance: "Best for: Conversations, podcasts, voice commands",
            latency: "~32ms"
        ),
        VADBackendInfo(
            backend: .general,
            name: "General",
            description: "RMS-based detection",
            guidance: "Best for: Simple detection, no ML dependency needed",
            latency: "~1ms"
        ),
        VADBackendInfo(
            backend: .singingRealtime,
            name: "Singing RT",
            description: "SwiftF0 pitch-based",
            guidance: "Best for: Karaoke, singing games, low-latency apps",
            latency: "~32ms"
        ),
        VADBackendInfo(
            backend: .singing,
            name: "Singing",
            description: "Essentia YAMNet classifier",
            guidance: "Best for: Music apps, vocal extraction, highest accuracy",
            latency: "~960ms"
        )
    ]

    static func info(for backend: VADBackend) -> VADBackendInfo {
        all.first { $0.backend == backend } ?? all[0]
    }
}
