package com.musicmuni.voxatrace.demo.calibra.pitch

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.musicmuni.voxatrace.calibra.model.ContourCleanup

/**
 * Contour cleanup preset comparison demo.
 *
 * Demonstrates how different cleanup presets affect pitch contours:
 * - RAW: No processing
 * - SCORING: Optimized for melody evaluation
 * - DISPLAY: Smoothed for visualization
 */
@Composable
fun ContourCleanupDemo() {
    var selectedCleanup by remember { mutableIntStateOf(1) } // SCORING default

    val cleanupPresets = listOf(
        ContourCleanup.RAW to CleanupInfo(
            name = "RAW",
            description = "No processing - raw pitch detections",
            color = Color(0xFF2196F3)
        ),
        ContourCleanup.SCORING to CleanupInfo(
            name = "SCORING",
            description = "Optimized for melody evaluation - removes outliers, corrects octaves",
            color = Color(0xFF4CAF50)
        ),
        ContourCleanup.DISPLAY to CleanupInfo(
            name = "DISPLAY",
            description = "Smoothed for visualization - gentle filtering",
            color = Color(0xFFFF9800)
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Contour Cleanup Presets",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Compare how different cleanup presets affect pitch contour processing",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Cleanup preset picker
        Text("Cleanup Preset:", style = MaterialTheme.typography.labelMedium)
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            cleanupPresets.forEachIndexed { index, (_, info) ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = cleanupPresets.size),
                    onClick = { selectedCleanup = index },
                    selected = selectedCleanup == index
                ) {
                    Text(info.name, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        HorizontalDivider()

        // Selected preset info
        val selectedInfo = cleanupPresets[selectedCleanup].second
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = selectedInfo.color.copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = selectedInfo.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = selectedInfo.color
                )
                Text(
                    text = selectedInfo.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Processing details
                when (selectedCleanup) {
                    0 -> RawPresetDetails()
                    1 -> ScoringPresetDetails()
                    2 -> DisplayPresetDetails()
                }
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
                        // Apply cleanup to extracted contour
                        val cleaned = CalibraPitch.PostProcess.cleanup(
                            contour,
                            ContourCleanup.SCORING
                        )

                        // Or use ContourExtractorBuilder
                        val extractor = CalibraPitch.ContourExtractorBuilder()
                            .cleanup(ContourCleanup.SCORING)
                            .build()
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private data class CleanupInfo(
    val name: String,
    val description: String,
    val color: Color
)

@Composable
private fun RawPresetDetails() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Processing Steps:", style = MaterialTheme.typography.labelMedium)
        Text("• None - direct detector output", style = MaterialTheme.typography.bodySmall)
        Text("", style = MaterialTheme.typography.bodySmall)
        Text("Use when:", style = MaterialTheme.typography.labelMedium)
        Text("• Analyzing raw detection quality", style = MaterialTheme.typography.bodySmall)
        Text("• Custom post-processing pipeline", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ScoringPresetDetails() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Processing Steps:", style = MaterialTheme.typography.labelMedium)
        Text("• Octave correction (fixes doubling/halving)", style = MaterialTheme.typography.bodySmall)
        Text("• Outlier rejection (removes isolated points)", style = MaterialTheme.typography.bodySmall)
        Text("• Minimum duration filtering", style = MaterialTheme.typography.bodySmall)
        Text("", style = MaterialTheme.typography.bodySmall)
        Text("Use when:", style = MaterialTheme.typography.labelMedium)
        Text("• Melody evaluation and scoring", style = MaterialTheme.typography.bodySmall)
        Text("• Note-level accuracy assessment", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun DisplayPresetDetails() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Processing Steps:", style = MaterialTheme.typography.labelMedium)
        Text("• Gentle smoothing filter", style = MaterialTheme.typography.bodySmall)
        Text("• Preserves pitch contour shape", style = MaterialTheme.typography.bodySmall)
        Text("", style = MaterialTheme.typography.bodySmall)
        Text("Use when:", style = MaterialTheme.typography.labelMedium)
        Text("• Visualizing pitch graphs", style = MaterialTheme.typography.bodySmall)
        Text("• UI feedback displays", style = MaterialTheme.typography.bodySmall)
    }
}
