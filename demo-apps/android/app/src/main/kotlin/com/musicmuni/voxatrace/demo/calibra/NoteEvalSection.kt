package com.musicmuni.voxatrace.demo.calibra

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Note-level evaluation section.
 *
 * Demonstrates:
 * - CalibraNoteEval for evaluating individual note accuracy
 * - Expected vs sung pitch comparison
 * - Tuning and timing metrics
 */
@Composable
fun NoteEvalSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Note Evaluation",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Evaluate individual note accuracy against expected pitches",
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
                    text = "Features",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text("• Define expected notes with pitch and duration", style = MaterialTheme.typography.bodySmall)
                Text("• Compare sung notes against expectation", style = MaterialTheme.typography.bodySmall)
                Text("• Calculate pitch accuracy (cents deviation)", style = MaterialTheme.typography.bodySmall)
                Text("• Measure timing accuracy (onset/offset)", style = MaterialTheme.typography.bodySmall)
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
                        val eval = CalibraNoteEval.Builder()
                            .expectedNotes(notes)
                            .toleranceCents(50)
                            .build()
                        val result = eval.evaluate(pitchContour)
                        // result.noteResults[i].pitchAccuracy
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "Note: Full implementation requires note definition and contour extraction.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
