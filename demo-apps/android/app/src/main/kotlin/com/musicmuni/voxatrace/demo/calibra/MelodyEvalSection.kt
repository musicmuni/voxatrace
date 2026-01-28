package com.musicmuni.voxatrace.demo.calibra

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Melody evaluation section.
 *
 * Demonstrates:
 * - CalibraMelodyEval for comparing student vs reference melodies
 * - Pitch contour extraction and alignment
 * - Melody accuracy scoring
 */
@Composable
fun MelodyEvalSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Melody Evaluation",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Compare student singing against a reference melody",
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
                Text("• Extract pitch contour from reference audio", style = MaterialTheme.typography.bodySmall)
                Text("• Record and extract student pitch contour", style = MaterialTheme.typography.bodySmall)
                Text("• Align and compare contours using DTW", style = MaterialTheme.typography.bodySmall)
                Text("• Calculate accuracy score (0-100)", style = MaterialTheme.typography.bodySmall)
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
                        val reference = LessonMaterial.fromAudio(...)
                        val student = LessonMaterial.fromAudio(...)
                        val extractor = CalibraPitch.createContourExtractor()
                        val result = CalibraMelodyEval.evaluate(
                            reference, student, extractor
                        )
                        // result.overallScore: 0.0-1.0
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "Note: Full implementation requires audio file loading and contour extraction.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
