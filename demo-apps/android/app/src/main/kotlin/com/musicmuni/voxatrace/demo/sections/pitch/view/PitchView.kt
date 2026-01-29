package com.musicmuni.voxatrace.demo.sections.pitch.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Container view for all pitch-related demos.
 *
 * Provides a picker to switch between:
 * - Realtime: Live pitch detection with YIN/SwiftF0
 * - Extraction: Batch pitch extraction from files
 * - Cleanup: Contour cleanup preset comparison
 * - PitchPoint: Properties inspector
 */
@Composable
fun PitchView() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    val demos = listOf("Realtime", "Extraction", "Cleanup", "PitchPoint")

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Demo picker
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            demos.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = demos.size),
                    onClick = { selectedDemo = index },
                    selected = selectedDemo == index
                ) {
                    Text(label, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        // Selected demo content
        when (selectedDemo) {
            0 -> RealtimePitchView()
            1 -> PitchExtractionView()
            2 -> ContourCleanupView()
            3 -> PitchPointExplorerView()
        }
    }
}
