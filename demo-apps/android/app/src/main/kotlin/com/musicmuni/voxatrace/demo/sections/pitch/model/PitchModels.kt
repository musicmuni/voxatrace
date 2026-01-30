package com.musicmuni.voxatrace.demo.sections.pitch.model

import com.musicmuni.voxatrace.calibra.model.ContourCleanup
import com.musicmuni.voxatrace.calibra.model.DetectionStrictness
import com.musicmuni.voxatrace.calibra.model.PitchAlgorithm
import com.musicmuni.voxatrace.calibra.model.PitchDetectorConfig
import com.musicmuni.voxatrace.calibra.model.PitchPreset
import com.musicmuni.voxatrace.calibra.model.QuietHandling
import com.musicmuni.voxatrace.calibra.model.VoiceType

/**
 * Information about pitch algorithms.
 */
data class PitchAlgorithmInfo(
    val algorithm: PitchAlgorithm,
    val name: String,
    val description: String
) {
    companion object {
        val all = listOf(
            PitchAlgorithmInfo(PitchAlgorithm.YIN, "YIN", "Traditional algorithm, no model needed"),
            PitchAlgorithmInfo(PitchAlgorithm.SWIFT_F0, "SwiftF0", "Neural network, higher accuracy")
        )
    }
}

/**
 * Information about pitch presets.
 */
data class PitchPresetInfo(
    val preset: PitchPreset,
    val name: String,
    val config: PitchDetectorConfig
) {
    companion object {
        val all = listOf(
            PitchPresetInfo(
                PitchPreset.RESPONSIVE,
                "Responsive",
                PitchDetectorConfig.Builder().bufferSize(1024).tolerance(0.20f).build()
            ),
            PitchPresetInfo(
                PitchPreset.BALANCED,
                "Balanced",
                PitchDetectorConfig.BALANCED
            ),
            PitchPresetInfo(
                PitchPreset.PRECISE,
                "Precise",
                PitchDetectorConfig.PRECISE
            )
        )
    }
}

/**
 * Information about voice types.
 */
data class VoiceTypeInfo(
    val voiceType: VoiceType,
    val name: String
) {
    companion object {
        val all = listOf(
            VoiceTypeInfo(VoiceType.Auto, "Auto"),
            VoiceTypeInfo(VoiceType.WesternSoprano, "Western Soprano"),
            VoiceTypeInfo(VoiceType.WesternAlto, "Western Alto"),
            VoiceTypeInfo(VoiceType.WesternTenor, "Western Tenor"),
            VoiceTypeInfo(VoiceType.WesternBass, "Western Bass"),
            VoiceTypeInfo(VoiceType.CarnaticMale, "Carnatic Male"),
            VoiceTypeInfo(VoiceType.CarnaticFemale, "Carnatic Female"),
            VoiceTypeInfo(VoiceType.HindustaniMale, "Hindustani Male"),
            VoiceTypeInfo(VoiceType.HindustaniFemale, "Hindustani Female"),
            VoiceTypeInfo(VoiceType.PopMale, "Pop Male"),
            VoiceTypeInfo(VoiceType.PopFemale, "Pop Female"),
            VoiceTypeInfo(VoiceType.IndianFilmMale, "Indian Film Male"),
            VoiceTypeInfo(VoiceType.IndianFilmFemale, "Indian Film Female")
        )
    }
}

/**
 * Information about quiet handling (environment noise).
 */
data class QuietHandlingInfo(
    val handling: QuietHandling,
    val name: String
) {
    companion object {
        val all = listOf(
            QuietHandlingInfo(QuietHandling.SENSITIVE, "Sensitive"),
            QuietHandlingInfo(QuietHandling.NORMAL, "Normal"),
            QuietHandlingInfo(QuietHandling.NOISY, "Noisy")
        )
    }
}

/**
 * Information about detection strictness.
 */
data class StrictnessInfo(
    val strictness: DetectionStrictness,
    val name: String
) {
    companion object {
        val all = listOf(
            StrictnessInfo(DetectionStrictness.STRICT, "Strict"),
            StrictnessInfo(DetectionStrictness.BALANCED, "Balanced"),
            StrictnessInfo(DetectionStrictness.LENIENT, "Lenient")
        )
    }
}

/**
 * Information about contour cleanup presets.
 */
data class CleanupPresetInfo(
    val cleanup: ContourCleanup,
    val name: String,
    val description: String
) {
    companion object {
        val all = listOf(
            CleanupPresetInfo(ContourCleanup.RAW, "RAW", "No post-processing"),
            CleanupPresetInfo(ContourCleanup.SCORING, "SCORING", "Octave + boundary + blip fix"),
            CleanupPresetInfo(ContourCleanup.DISPLAY, "DISPLAY", "Scoring + smoothing")
        )
    }
}

/**
 * Information about extraction presets.
 */
data class ExtractionPresetInfo(
    val preset: PitchPreset,
    val name: String
) {
    companion object {
        val all = listOf(
            ExtractionPresetInfo(PitchPreset.RESPONSIVE, "Responsive"),
            ExtractionPresetInfo(PitchPreset.BALANCED, "Balanced"),
            ExtractionPresetInfo(PitchPreset.PRECISE, "Precise")
        )
    }
}
