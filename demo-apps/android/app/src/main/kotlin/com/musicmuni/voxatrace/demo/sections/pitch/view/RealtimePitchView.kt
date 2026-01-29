package com.musicmuni.voxatrace.demo.sections.pitch.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.musicmuni.voxatrace.calibra.model.PitchPoint
import com.musicmuni.voxatrace.demo.sections.pitch.viewmodel.RealtimePitchViewModel

/**
 * Real-time pitch detection view.
 */
@Composable
fun RealtimePitchView(viewModel: RealtimePitchViewModel = viewModel()) {
    val context = LocalContext.current

    val selectedAlgorithm by viewModel.selectedAlgorithm.collectAsStateWithLifecycle()
    val selectedPreset by viewModel.selectedPreset.collectAsStateWithLifecycle()
    val selectedVoiceType by viewModel.selectedVoiceType.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()
    val currentPitch by viewModel.currentPitch.collectAsStateWithLifecycle()
    val amplitude by viewModel.amplitude.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Real-time Pitch Detection",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Detects pitch using modern Builder API with configurable presets",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Algorithm picker
        Text("Algorithm:", style = MaterialTheme.typography.labelMedium)
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            viewModel.algorithms.forEachIndexed { index, info ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = viewModel.algorithms.size),
                    onClick = { viewModel.setSelectedAlgorithm(index) },
                    selected = selectedAlgorithm == index,
                    enabled = index == 0 // Only YIN for now
                ) {
                    Text(info.name, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        Text(
            text = viewModel.algorithms[selectedAlgorithm].description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Preset picker
        Text("Preset:", style = MaterialTheme.typography.labelMedium)
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            viewModel.presets.forEachIndexed { index, info ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = viewModel.presets.size),
                    onClick = { viewModel.setSelectedPreset(index) },
                    selected = selectedPreset == index
                ) {
                    Text(info.name, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        // Voice type dropdown
        var voiceTypeExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = voiceTypeExpanded,
            onExpandedChange = { voiceTypeExpanded = it }
        ) {
            OutlinedTextField(
                value = viewModel.voiceTypes[selectedVoiceType].name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Voice Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = voiceTypeExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = voiceTypeExpanded,
                onDismissRequest = { voiceTypeExpanded = false }
            ) {
                viewModel.voiceTypes.forEachIndexed { index, info ->
                    DropdownMenuItem(
                        text = { Text(info.name) },
                        onClick = {
                            viewModel.setSelectedVoiceType(index)
                            voiceTypeExpanded = false
                        }
                    )
                }
            }
        }

        HorizontalDivider()

        // Live display
        PitchDisplayCard(
            pitch = currentPitch,
            amplitude = amplitude
        )

        // Control button
        Button(
            onClick = {
                if (isRecording) viewModel.stopRecording()
                else viewModel.startRecording(context)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRecording) "Stop" else "Start")
        }

        // API info
        ApiInfoCard()
    }
}

@Composable
private fun PitchDisplayCard(
    pitch: PitchPoint?,
    amplitude: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Note display
            if (pitch != null && pitch.isSinging) {
                Text(
                    text = pitch.note ?: "-",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "%.1f Hz".format(pitch.pitch),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tuning indicator
                TuningIndicator(centsOff = pitch.centsOff, tuning = pitch.tuning)
            } else {
                Text(
                    text = "-",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Not singing",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Amplitude bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Level:", style = MaterialTheme.typography.bodySmall)
                LinearProgressIndicator(
                    progress = { amplitude.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                )
            }
        }
    }
}

@Composable
private fun TuningIndicator(centsOff: Int, tuning: PitchPoint.Tuning) {
    val color = when (tuning) {
        PitchPoint.Tuning.IN_TUNE -> Color.Green
        PitchPoint.Tuning.FLAT, PitchPoint.Tuning.SHARP -> Color(0xFFFF9800)
        else -> Color.Gray
    }

    val label = when (tuning) {
        PitchPoint.Tuning.IN_TUNE -> "IN TUNE"
        PitchPoint.Tuning.FLAT -> "FLAT ($centsOff)"
        PitchPoint.Tuning.SHARP -> "SHARP (+$centsOff)"
        else -> "-"
    }

    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = color,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun ApiInfoCard() {
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
                    val detector = CalibraPitch.DetectorBuilder()
                        .algorithm(PitchAlgorithm.YIN)
                        .preset(PitchPreset.BALANCED)
                        .voiceType(VoiceType.Auto)
                        .build()
                    val point = detector.detect(samples16k)
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
