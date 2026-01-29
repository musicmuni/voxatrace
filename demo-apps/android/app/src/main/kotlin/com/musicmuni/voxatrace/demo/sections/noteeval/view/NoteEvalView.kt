package com.musicmuni.voxatrace.demo.sections.noteeval.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Note Evaluation View.
 *
 * Evaluates individual note accuracy - pitch correctness,
 * timing, and duration for discrete note-based lessons.
 */
@Composable
fun NoteEvalView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Note Evaluation",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Evaluates individual note pitch and timing accuracy",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "How It Works",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text("1. Define reference notes with pitches and timings", style = MaterialTheme.typography.bodySmall)
                Text("2. Record student performance", style = MaterialTheme.typography.bodySmall)
                Text("3. Extract pitch contour from recording", style = MaterialTheme.typography.bodySmall)
                Text("4. Score each note for pitch accuracy", style = MaterialTheme.typography.bodySmall)
            }
        }

        // API info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "API Usage:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = """
                        // Create evaluator with reference notes
                        val evaluator = CalibraNoteEval.create(
                            referenceKeyHz = 440.0,
                            studentKeyHz = 440.0
                        )

                        // Define expected notes
                        val notes = listOf(
                            Note(pitch = 60, startTime = 0.0, endTime = 1.0),
                            Note(pitch = 62, startTime = 1.0, endTime = 2.0)
                        )

                        // Evaluate student contour against notes
                        val result = evaluator.evaluate(
                            studentContour = contour,
                            referenceNotes = notes
                        )

                        // Per-note scores
                        result.noteResults.forEach { note ->
                            println("Pitch: ${'$'}{note.pitchScore}")
                        }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "Note: Best for lessons with discrete, clearly-defined notes.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
