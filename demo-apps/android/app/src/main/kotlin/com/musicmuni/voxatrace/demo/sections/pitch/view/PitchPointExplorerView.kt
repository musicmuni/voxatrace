package com.musicmuni.voxatrace.demo.sections.pitch.view

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.musicmuni.voxatrace.calibra.model.PitchPoint
import com.musicmuni.voxatrace.demo.sections.pitch.viewmodel.PitchPointExplorerViewModel

/**
 * PitchPoint properties explorer view - shows all computed properties in real-time.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PitchPointExplorerView(viewModel: PitchPointExplorerViewModel = viewModel()) {
    val context = LocalContext.current
    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    val selectedAlgorithm by viewModel.selectedAlgorithm.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()
    val currentPitch by viewModel.currentPitch.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onDisappear()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "PitchPoint Properties",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "See all computed properties of PitchPoint in real-time.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Algorithm picker
        AlgorithmPickerSection(
            viewModel = viewModel,
            selectedAlgorithm = selectedAlgorithm,
            isRecording = isRecording
        )

        // Main display
        PitchDisplaySection(currentPitch = currentPitch)

        HorizontalDivider()

        // Properties inspector
        PropertiesInspector(
            viewModel = viewModel,
            currentPitch = currentPitch
        )

        HorizontalDivider()

        // Control
        Button(
            onClick = {
                if (audioPermissionState.status.isGranted) {
                    viewModel.toggleRecording(context)
                } else {
                    audioPermissionState.launchPermissionRequest()
                }
            }
        ) {
            Text(if (isRecording) "Stop" else "Start")
        }

        HorizontalDivider()

        // API info section
        ApiInfoSection()
    }
}

@Composable
private fun AlgorithmPickerSection(
    viewModel: PitchPointExplorerViewModel,
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
private fun PitchDisplaySection(currentPitch: PitchPoint?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Large note display
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentPitch != null && currentPitch.isSinging) {
                    Text(
                        text = currentPitch.note ?: "-",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue
                    )
                    Text(
                        text = String.format("%.1f Hz", currentPitch.pitch),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "-",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Text(
                        text = "Not singing",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Tuning indicator
        if (currentPitch != null && currentPitch.isSinging) {
            TuningIndicatorView(
                centsOff = currentPitch.centsOff,
                tuning = currentPitch.tuning
            )
        }
    }
}

@Composable
private fun TuningIndicatorView(centsOff: Int, tuning: PitchPoint.Tuning) {
    val color = when (tuning) {
        PitchPoint.Tuning.IN_TUNE -> Color(0xFF4CAF50)
        PitchPoint.Tuning.FLAT, PitchPoint.Tuning.SHARP -> Color(0xFFFF9800)
        else -> Color.Gray
    }

    val label = when (tuning) {
        PitchPoint.Tuning.IN_TUNE -> "IN TUNE"
        PitchPoint.Tuning.FLAT -> "FLAT ($centsOff)"
        PitchPoint.Tuning.SHARP -> "SHARP (+$centsOff)"
        else -> "-"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PropertiesInspector(
    viewModel: PitchPointExplorerViewModel,
    currentPitch: PitchPoint?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Properties Inspector",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            // Grid layout for properties
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    PropertyCell(
                        name = "pitch",
                        value = currentPitch?.let { String.format("%.1f Hz", it.pitch) } ?: "-",
                        modifier = Modifier.weight(1f)
                    )
                    PropertyCell(
                        name = "isSinging",
                        value = currentPitch?.isSinging?.toString() ?: "-",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    PropertyCell(
                        name = "note",
                        value = currentPitch?.note ?: "-",
                        modifier = Modifier.weight(1f)
                    )
                    PropertyCell(
                        name = "midiNote",
                        value = if (currentPitch?.isSinging == true) "${currentPitch.midiNote}" else "-",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    PropertyCell(
                        name = "centsOff",
                        value = if (currentPitch?.isSinging == true) "${currentPitch.centsOff}" else "-",
                        modifier = Modifier.weight(1f)
                    )
                    PropertyCell(
                        name = "tuning",
                        value = currentPitch?.let { viewModel.tuningString(it.tuning) } ?: "-",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    PropertyCell(
                        name = "reliability",
                        value = currentPitch?.let { String.format("%.2f", it.reliability) } ?: "-",
                        modifier = Modifier.weight(1f)
                    )
                    PropertyCell(
                        name = "confidence",
                        value = currentPitch?.let { String.format("%.2f", it.confidence) } ?: "-",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PropertyCell(
    name: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(4.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
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
                text = "PitchPoint Properties:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "• pitch: Float - Detected pitch in Hz (-1 if not singing)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• isSinging: Bool - True if voiced audio detected",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• note: String? - Musical note name (e.g., \"A4\", \"C#5\")",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• midiNote: Int - MIDI note number (e.g., 69 for A4)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• centsOff: Int - Deviation from note (-50 to +50)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• tuning: Tuning - SILENT, FLAT, IN_TUNE, or SHARP",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• reliability: Float - Detection confidence (0.0-1.0)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
