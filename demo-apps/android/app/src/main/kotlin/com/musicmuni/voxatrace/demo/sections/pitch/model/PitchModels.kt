package com.musicmuni.voxatrace.demo.sections.pitch.model

import com.musicmuni.voxatrace.calibra.model.PitchAlgorithm
import com.musicmuni.voxatrace.calibra.model.PitchPreset
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
            PitchAlgorithmInfo(PitchAlgorithm.SWIFT_F0, "SwiftF0", "Neural network, higher accuracy (requires model)")
        )
    }
}

/**
 * Information about pitch presets.
 */
data class PitchPresetInfo(
    val preset: PitchPreset,
    val name: String
) {
    companion object {
        val all = listOf(
            PitchPresetInfo(PitchPreset.RESPONSIVE, "Responsive"),
            PitchPresetInfo(PitchPreset.BALANCED, "Balanced"),
            PitchPresetInfo(PitchPreset.PRECISE, "Precise")
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
            VoiceTypeInfo(VoiceType.WesternSoprano, "Soprano"),
            VoiceTypeInfo(VoiceType.WesternAlto, "Alto"),
            VoiceTypeInfo(VoiceType.WesternTenor, "Tenor"),
            VoiceTypeInfo(VoiceType.WesternBass, "Bass"),
            VoiceTypeInfo(VoiceType.CarnaticMale, "Carnatic M"),
            VoiceTypeInfo(VoiceType.CarnaticFemale, "Carnatic F")
        )
    }
}
