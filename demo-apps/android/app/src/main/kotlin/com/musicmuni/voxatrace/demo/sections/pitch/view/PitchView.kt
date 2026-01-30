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
        // Demo picker using ScrollableTabRow for better fit
        ScrollableTabRow(
            selectedTabIndex = selectedDemo,
            edgePadding = 0.dp,
            divider = {}
        ) {
            demos.forEachIndexed { index, label ->
                Tab(
                    selected = selectedDemo == index,
                    onClick = { selectedDemo = index },
                    text = { Text(label) }
                )
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
