package com.musicmuni.voxatrace.demo.sections.pitch.view

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.musicmuni.voxatrace.demo.components.OptionChip
import com.musicmuni.voxatrace.demo.sections.pitch.viewmodel.ContourCleanupViewModel
import com.musicmuni.voxatrace.demo.sections.shared.ContourData
import com.musicmuni.voxatrace.demo.sections.shared.PitchGraphView

/**
 * Contour cleanup comparison view - compares RAW vs SCORING vs DISPLAY presets.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContourCleanupView(viewModel: ContourCleanupViewModel = viewModel()) {
    val context = LocalContext.current
    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    val selectedAlgorithm by viewModel.selectedAlgorithm.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()
    val isProcessing by viewModel.isProcessing.collectAsStateWithLifecycle()
    val rawContour by viewModel.rawContour.collectAsStateWithLifecycle()
    val scoringContour by viewModel.scoringContour.collectAsStateWithLifecycle()
    val displayContour by viewModel.displayContour.collectAsStateWithLifecycle()
    val showRaw by viewModel.showRaw.collectAsStateWithLifecycle()
    val showScoring by viewModel.showScoring.collectAsStateWithLifecycle()
    val showDisplay by viewModel.showDisplay.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onDisappear()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Algorithm picker section
        AlgorithmPickerSection(
            viewModel = viewModel,
            selectedAlgorithm = selectedAlgorithm,
            isRecording = isRecording
        )

        // Recording section
        RecordingSection(
            isRecording = isRecording,
            isProcessing = isProcessing,
            hasResults = rawContour != null,
            onToggleRecording = {
                if (audioPermissionState.status.isGranted) {
                    viewModel.toggleRecording(context)
                } else {
                    audioPermissionState.launchPermissionRequest()
                }
            },
            onClearResults = { viewModel.clearResults() }
        )

        if (rawContour != null) {
            HorizontalDivider()

            // Display toggles section
            DisplayTogglesSection(
                showRaw = showRaw,
                showScoring = showScoring,
                showDisplay = showDisplay,
                onShowRawChange = { viewModel.setShowRaw(it) },
                onShowScoringChange = { viewModel.setShowScoring(it) },
                onShowDisplayChange = { viewModel.setShowDisplay(it) }
            )

            // Graph section
            GraphSection(
                rawContour = rawContour,
                scoringContour = scoringContour,
                displayContour = displayContour,
                showRaw = showRaw,
                showScoring = showScoring,
                showDisplay = showDisplay
            )

            // Statistics section
            StatisticsSection(
                viewModel = viewModel,
                rawContour = rawContour,
                scoringContour = scoringContour,
                displayContour = displayContour
            )
        }

        HorizontalDivider()

        // API info section
        ApiInfoSection()
    }
}

@Composable
private fun AlgorithmPickerSection(
    viewModel: ContourCleanupViewModel,
    selectedAlgorithm: Int,
    isRecording: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Algorithm",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            viewModel.algorithms.forEachIndexed { index, info ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = viewModel.algorithms.size),
                    onClick = { viewModel.setSelectedAlgorithm(index) },
                    selected = selectedAlgorithm == index,
                    enabled = !isRecording
                ) {
                    Text(info.name)
                }
            }
        }
    }
}

@Composable
private fun RecordingSection(
    isRecording: Boolean,
    isProcessing: Boolean,
    hasResults: Boolean,
    onToggleRecording: () -> Unit,
    onClearResults: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Record Audio Sample",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Record a vocal sample to compare cleanup presets.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onToggleRecording,
                enabled = !isProcessing
            ) {
                Text(if (isRecording) "Stop Recording" else "Start Recording")
            }

            if (isRecording) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color.Red, CircleShape)
                )
                Text(
                    text = "Recording...",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red
                )
            }

            if (isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Text(
                    text = "Processing...",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        if (hasResults) {
            OutlinedButton(onClick = onClearResults) {
                Text("Clear & Record New")
            }
        }
    }
}

@Composable
private fun DisplayTogglesSection(
    showRaw: Boolean,
    showScoring: Boolean,
    showDisplay: Boolean,
    onShowRawChange: (Boolean) -> Unit,
    onShowScoringChange: (Boolean) -> Unit,
    onShowDisplayChange: (Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Display Presets",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OptionChip(
                selected = showRaw,
                onClick = { onShowRawChange(!showRaw) },
                label = "RAW"
            )
            OptionChip(
                selected = showScoring,
                onClick = { onShowScoringChange(!showScoring) },
                label = "SCORING"
            )
            OptionChip(
                selected = showDisplay,
                onClick = { onShowDisplayChange(!showDisplay) },
                label = "DISPLAY"
            )
        }
    }
}

@Composable
private fun GraphSection(
    rawContour: com.musicmuni.voxatrace.calibra.model.PitchContour?,
    scoringContour: com.musicmuni.voxatrace.calibra.model.PitchContour?,
    displayContour: com.musicmuni.voxatrace.calibra.model.PitchContour?,
    showRaw: Boolean,
    showScoring: Boolean,
    showDisplay: Boolean
) {
    val contours = remember(rawContour, scoringContour, displayContour, showRaw, showScoring, showDisplay) {
        buildList {
            if (showRaw && rawContour != null) {
                add(ContourData(rawContour.pitchesHz.toList(), Color.Gray, "RAW"))
            }
            if (showScoring && scoringContour != null) {
                add(ContourData(scoringContour.pitchesHz.toList(), Color(0xFF4CAF50), "SCORING"))
            }
            if (showDisplay && displayContour != null) {
                add(ContourData(displayContour.pitchesHz.toList(), Color.Blue, "DISPLAY"))
            }
        }
    }

    if (contours.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.small
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Select at least one preset to display",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        PitchGraphView(
            contours = contours,
            title = "Cleanup Comparison",
            height = 250
        )
    }
}

@Composable
private fun StatisticsSection(
    viewModel: ContourCleanupViewModel,
    rawContour: com.musicmuni.voxatrace.calibra.model.PitchContour?,
    scoringContour: com.musicmuni.voxatrace.calibra.model.PitchContour?,
    displayContour: com.musicmuni.voxatrace.calibra.model.PitchContour?
) {
    val rawStats = remember(rawContour) { viewModel.contourStats(rawContour) }
    val scoringStats = remember(scoringContour) { viewModel.contourStats(scoringContour) }
    val displayStats = remember(displayContour) { viewModel.contourStats(displayContour) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Comparison Statistics",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatHeader("Preset", Modifier.weight(1f))
                StatHeader("Voiced", Modifier.weight(1f))
                StatHeader("Octave Err", Modifier.weight(1f))
                StatHeader("Blips", Modifier.weight(1f))
            }

            // RAW row
            StatRow(
                preset = "RAW",
                voicedCount = rawStats.voicedCount,
                octaveErrors = rawStats.octaveErrors,
                blips = rawStats.blips,
                referenceOctaveErrors = rawStats.octaveErrors,
                referenceBlips = rawStats.blips
            )

            // SCORING row
            StatRow(
                preset = "SCORING",
                voicedCount = scoringStats.voicedCount,
                octaveErrors = scoringStats.octaveErrors,
                blips = scoringStats.blips,
                referenceOctaveErrors = rawStats.octaveErrors,
                referenceBlips = rawStats.blips
            )

            // DISPLAY row
            StatRow(
                preset = "DISPLAY",
                voicedCount = displayStats.voicedCount,
                octaveErrors = displayStats.octaveErrors,
                blips = displayStats.blips,
                referenceOctaveErrors = rawStats.octaveErrors,
                referenceBlips = rawStats.blips
            )
        }
    }
}

@Composable
private fun StatHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}

@Composable
private fun StatRow(
    preset: String,
    voicedCount: Int,
    octaveErrors: Int,
    blips: Int,
    referenceOctaveErrors: Int,
    referenceBlips: Int
) {
    val octaveColor = if (octaveErrors < referenceOctaveErrors) Color(0xFF4CAF50) else LocalContentColor.current
    val blipsColor = if (blips < referenceBlips) Color(0xFF4CAF50) else LocalContentColor.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = preset,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "$voicedCount",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "$octaveErrors",
            style = MaterialTheme.typography.bodySmall,
            color = octaveColor,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "$blips",
            style = MaterialTheme.typography.bodySmall,
            color = blipsColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ApiInfoSection() {
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
                text = "APIs Demonstrated:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "• CalibraPitch.PostProcess.cleanup(contour, options: .scoring)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• ContourCleanup.RAW, .SCORING, .DISPLAY",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• CalibraPitch.PostProcess.fixOctaveErrors(contour)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• CalibraPitch.PostProcess.removeBlips(contour, minDurationMs:)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
