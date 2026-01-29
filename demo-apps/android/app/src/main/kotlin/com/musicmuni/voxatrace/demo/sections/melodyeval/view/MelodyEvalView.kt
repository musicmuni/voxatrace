package com.musicmuni.voxatrace.demo.sections.melodyeval.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Melody Evaluation View.
 *
 * Evaluates how well a student's melody matches a reference melody
 * using DTW-based alignment and scoring.
 */
@Composable
fun MelodyEvalView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Melody Evaluation",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Compares two melodies using DTW alignment",
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
                Text("1. Load reference melody (from file or segments)", style = MaterialTheme.typography.bodySmall)
                Text("2. Record or load student melody", style = MaterialTheme.typography.bodySmall)
                Text("3. Extract pitch contours from both", style = MaterialTheme.typography.bodySmall)
                Text("4. DTW aligns and scores similarity", style = MaterialTheme.typography.bodySmall)
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
                        // Create evaluator with segments
                        val evaluator = CalibraMelodyEval.create(
                            segments = refSegments,
                            referenceKeyHz = 440.0,
                            studentKeyHz = 440.0
                        )

                        // Evaluate student pitch contour
                        val result = evaluator.evaluate(
                            studentContour
                        )

                        // Get scores per segment
                        result.segmentResults.forEach { seg ->
                            println("Score: ${'$'}{seg.score}")
                        }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "Note: This performs offline evaluation on pre-recorded audio.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
