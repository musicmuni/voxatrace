package com.musicmuni.voxatrace.demo.sections.singafter.model

import com.musicmuni.voxatrace.calibra.model.LessonMaterial
import com.musicmuni.voxatrace.calibra.model.PracticePhase

/**
 * Represents the current state of the Singafter practice UI.
 */
sealed class SingafterUIState {
    data object Idle : SingafterUIState()
    data object Loading : SingafterUIState()
    data object Ready : SingafterUIState()
    data object Practicing : SingafterUIState()
    data object Summary : SingafterUIState()
    data class Error(val message: String) : SingafterUIState()
}

/**
 * Parsed singafter lesson data ready for CalibraLiveEval.
 */
data class SingafterLessonData(
    val reference: LessonMaterial,
    val audioPath: String,
    val phrasePairs: List<PhrasePair>
)

/**
 * Phrase pair for singafter practice (teacher + student segments).
 */
data class PhrasePair(
    val index: Int,
    val lyrics: String,
    val teacherStartTime: Float,
    val teacherEndTime: Float,
    val studentStartTime: Float,
    val studentEndTime: Float,
    val teacherId: Int
)

/**
 * Extended practice phase for UI display.
 */
val PracticePhase.displayName: String
    get() = when (this) {
        PracticePhase.IDLE -> "Ready"
        PracticePhase.LISTENING -> "Listen"
        PracticePhase.SINGING -> "Sing"
        PracticePhase.EVALUATED -> "Score"
    }
