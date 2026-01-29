package com.musicmuni.voxatrace.demo.sections.singafter.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Singafter (Call and Response) View.
 */
@Composable
fun SingafterView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Sing After (Call & Response)",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Listen to a phrase, then sing it back",
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
                Text("1. Play reference phrase", style = MaterialTheme.typography.bodySmall)
                Text("2. Listen and memorize", style = MaterialTheme.typography.bodySmall)
                Text("3. Record your attempt", style = MaterialTheme.typography.bodySmall)
                Text("4. Get pitch accuracy score", style = MaterialTheme.typography.bodySmall)
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
                        // 1. Play reference segment
                        player.seek(segment.startTime)
                        player.play()

                        // 2. Record student response
                        recorder.start()

                        // 3. Evaluate
                        val result = CalibraSegmentEvaluator.evaluate(
                            reference, student
                        )
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "Note: Full implementation similar to Singalong but with separate listen/record phases.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
