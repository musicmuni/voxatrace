import Foundation
import VoxaTrace

// MARK: - Algorithm Info

/// Pitch detection algorithm configuration.
struct PitchAlgorithmInfo {
    let algorithm: PitchAlgorithm
    let name: String
    let description: String
}

extension PitchAlgorithmInfo {
    static let all: [PitchAlgorithmInfo] = [
        PitchAlgorithmInfo(algorithm: .yin, name: "YIN", description: "Traditional algorithm, no model needed"),
        PitchAlgorithmInfo(algorithm: .swiftF0, name: "SwiftF0", description: "Neural network, higher accuracy")
    ]
}

// MARK: - Preset Info

/// Pitch detection preset configuration.
struct PitchPresetInfo {
    let config: PitchDetectorConfig
    let name: String
}

extension PitchPresetInfo {
    static let all: [PitchPresetInfo] = [
        PitchPresetInfo(config: PitchDetectorConfig.Builder().bufferSize(1024).tolerance(0.20).build(), name: "Responsive"),
        PitchPresetInfo(config: .balanced, name: "Balanced"),
        PitchPresetInfo(config: .precise, name: "Precise")
    ]
}

// MARK: - Voice Type Info

/// Voice type configuration for pitch detection.
struct VoiceTypeInfo {
    let type: VoiceType
    let name: String
}

extension VoiceTypeInfo {
    static let all: [VoiceTypeInfo] = [
        VoiceTypeInfo(type: .auto, name: "Auto"),
        VoiceTypeInfo(type: .westernSoprano, name: "Western Soprano"),
        VoiceTypeInfo(type: .westernAlto, name: "Western Alto"),
        VoiceTypeInfo(type: .westernTenor, name: "Western Tenor"),
        VoiceTypeInfo(type: .westernBass, name: "Western Bass"),
        VoiceTypeInfo(type: .carnaticMale, name: "Carnatic Male"),
        VoiceTypeInfo(type: .carnaticFemale, name: "Carnatic Female"),
        VoiceTypeInfo(type: .hindustaniMale, name: "Hindustani Male"),
        VoiceTypeInfo(type: .hindustaniFemale, name: "Hindustani Female"),
        VoiceTypeInfo(type: .popMale, name: "Pop Male"),
        VoiceTypeInfo(type: .popFemale, name: "Pop Female"),
        VoiceTypeInfo(type: .indianFilmMale, name: "Indian Film Male"),
        VoiceTypeInfo(type: .indianFilmFemale, name: "Indian Film Female")
    ]
}

// MARK: - Quiet Handling Info

/// Quiet environment handling configuration.
struct QuietHandlingInfo {
    let handling: QuietHandling
    let name: String
}

extension QuietHandlingInfo {
    static let all: [QuietHandlingInfo] = [
        QuietHandlingInfo(handling: .sensitive, name: "Sensitive"),
        QuietHandlingInfo(handling: .normal, name: "Normal"),
        QuietHandlingInfo(handling: .noisy, name: "Noisy")
    ]
}

// MARK: - Strictness Info

/// Detection strictness configuration.
struct StrictnessInfo {
    let strictness: DetectionStrictness
    let name: String
}

extension StrictnessInfo {
    static let all: [StrictnessInfo] = [
        StrictnessInfo(strictness: .strict, name: "Strict"),
        StrictnessInfo(strictness: .balanced, name: "Balanced"),
        StrictnessInfo(strictness: .lenient, name: "Lenient")
    ]
}

// MARK: - Cleanup Preset Info

/// Contour cleanup preset configuration.
struct CleanupPresetInfo {
    let cleanup: ContourCleanup
    let name: String
    let description: String
}

extension CleanupPresetInfo {
    static let all: [CleanupPresetInfo] = [
        CleanupPresetInfo(cleanup: .raw, name: "RAW", description: "No post-processing"),
        CleanupPresetInfo(cleanup: .scoring, name: "SCORING", description: "Octave + boundary + blip fix"),
        CleanupPresetInfo(cleanup: .display, name: "DISPLAY", description: "Scoring + smoothing")
    ]
}

// MARK: - Extraction Preset Info

/// Pitch extraction preset configuration.
struct ExtractionPresetInfo {
    let preset: PitchPreset
    let name: String
}

extension ExtractionPresetInfo {
    static let all: [ExtractionPresetInfo] = [
        ExtractionPresetInfo(preset: .responsive, name: "Responsive"),
        ExtractionPresetInfo(preset: .balanced, name: "Balanced"),
        ExtractionPresetInfo(preset: .precise, name: "Precise")
    ]
}
