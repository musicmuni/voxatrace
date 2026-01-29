package com.musicmuni.voxatrace.demo.sections.singalong.model

import com.musicmuni.voxatrace.calibra.model.LessonMaterial
import com.musicmuni.voxatrace.calibra.model.SessionConfig

/**
 * Available session presets for practice modes.
 */
enum class SessionPreset(val displayName: String, val description: String) {
    DEFAULT("Default", "Balanced, auto-advancing"),
    PRACTICE("Practice", "70% threshold, 3 attempts, best score"),
    KARAOKE("Karaoke", "Relaxed, always advances"),
    PERFORMANCE("Performance", "Precise, one attempt");

    val config: SessionConfig
        get() = when (this) {
            DEFAULT -> SessionConfig.DEFAULT
            PRACTICE -> SessionConfig.PRACTICE
            KARAOKE -> SessionConfig.KARAOKE
            PERFORMANCE -> SessionConfig.PERFORMANCE
        }
}

/**
 * Parsed lesson data ready for CalibraLiveEval.
 */
data class LessonData(
    val reference: LessonMaterial,
    val audioPath: String
)

/**
 * Represents the current state of the Singalong practice UI.
 */
sealed class SingalongUIState {
    data object Idle : SingalongUIState()
    data object Loading : SingalongUIState()
    data object Ready : SingalongUIState()
    data object Practicing : SingalongUIState()
    data object Summary : SingalongUIState()
    data class Error(val message: String) : SingalongUIState()
}
