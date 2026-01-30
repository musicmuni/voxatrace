package com.musicmuni.voxatrace.demo.sections.pitch.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.musicmuni.voxatrace.demo.sections.pitch.viewmodel.PitchExtractionViewModel
import com.musicmuni.voxatrace.demo.sections.shared.PitchGraphView

/**
 * Batch pitch extraction view with ContourCleanup presets.
 */
@Composable
fun PitchExtractionView(viewModel: PitchExtractionViewModel = viewModel()) {
    val context = LocalContext.current

    val selectedAlgorithm by viewModel.selectedAlgorithm.collectAsStateWithLifecycle()
    val selectedPreset by viewModel.selectedPreset.collectAsStateWithLifecycle()
    val selectedVoiceType by viewModel.selectedVoiceType.collectAsStateWithLifecycle()
    val selectedCleanup by viewModel.selectedCleanup.collectAsStateWithLifecycle()
    val hopMs by viewModel.hopMs.collectAsStateWithLifecycle()
    val isExtracting by viewModel.isExtracting.collectAsStateWithLifecycle()
    val contour by viewModel.contour.collectAsStateWithLifecycle()
    val duration by viewModel.duration.collectAsStateWithLifecycle()
    val voicedRatio by viewModel.voicedRatio.collectAsStateWithLifecycle()
    val meanPitchHz by viewModel.meanPitchHz.collectAsStateWithLifecycle()
    val minPitchHz by viewModel.minPitchHz.collectAsStateWithLifecycle()
    val maxPitchHz by viewModel.maxPitchHz.collectAsStateWithLifecycle()
    val rangeSemitones by viewModel.rangeSemitones.collectAsStateWithLifecycle()
    val sampleCount by viewModel.sampleCount.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Configuration section
        ConfigurationCard(
            viewModel = viewModel,
            selectedAlgorithm = selectedAlgorithm,
            selectedPreset = selectedPreset,
            selectedVoiceType = selectedVoiceType,
            selectedCleanup = selectedCleanup,
            hopMs = hopMs,
            isExtracting = isExtracting
        )

        HorizontalDivider()

        // Extract button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.extractPitch(context) },
                enabled = !isExtracting
            ) {
                Text("Extract from Sample Voice")
            }

            if (isExtracting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }
        }

        // Results
        contour?.let {
            // Statistics section
            StatisticsCard(
                duration = duration,
                voicedRatio = voicedRatio,
                sampleCount = sampleCount,
                meanPitchHz = meanPitchHz,
                minPitchHz = minPitchHz,
                maxPitchHz = maxPitchHz,
                rangeSemitones = rangeSemitones
            )

            // Pitch graph
            PitchGraphSection(
                pitchesHz = it.pitchesHz,
                times = it.times
            )
        }

        HorizontalDivider()

        // API info
        ApiInfoCard()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ConfigurationCard(
    viewModel: PitchExtractionViewModel,
    selectedAlgorithm: Int,
    selectedPreset: Int,
    selectedVoiceType: Int,
    selectedCleanup: Int,
    hopMs: Int,
    isExtracting: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Configuration",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        // Algorithm section
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
                        enabled = !isExtracting
                    ) {
                        Text(info.name)
                    }
                }
            }
        }

        // Preset section with chips
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Preset",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.presets.forEachIndexed { index, info ->
                    FilterChip(
                        selected = selectedPreset == index,
                        onClick = { viewModel.setSelectedPreset(index) },
                        label = { Text(info.name) },
                        enabled = !isExtracting
                    )
                }
            }
        }

        // Voice Type dropdown
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Voice Type",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            var voiceTypeExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = voiceTypeExpanded,
                onExpandedChange = { if (!isExtracting) voiceTypeExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = viewModel.voiceTypes[selectedVoiceType].name,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    enabled = !isExtracting,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = voiceTypeExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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
        }

        // Cleanup section with chips
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Cleanup",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.cleanupPresets.forEachIndexed { index, info ->
                    FilterChip(
                        selected = selectedCleanup == index,
                        onClick = { viewModel.setSelectedCleanup(index) },
                        label = { Text(info.name) },
                        enabled = !isExtracting
                    )
                }
            }
            Text(
                text = viewModel.cleanupPresets[selectedCleanup].description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Hop size
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Hop Size",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${hopMs} ms",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            }
            Slider(
                value = hopMs.toFloat(),
                onValueChange = { viewModel.setHopMs(it.toInt()) },
                valueRange = 5f..50f,
                steps = 8,
                enabled = !isExtracting,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun StatisticsCard(
    duration: Float,
    voicedRatio: Float,
    sampleCount: Int,
    meanPitchHz: Float,
    minPitchHz: Float,
    maxPitchHz: Float,
    rangeSemitones: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            // First row: Duration, Voiced, Samples
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCell(title = "Duration", value = "%.2fs".format(duration))
                StatCell(title = "Voiced", value = "%.0f%%".format(voicedRatio * 100))
                StatCell(title = "Samples", value = "$sampleCount")
            }

            // Second row: Mean, Min, Max
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCell(
                    title = "Mean",
                    value = "${meanPitchHz.toInt()} Hz"
                )
                StatCell(
                    title = "Min",
                    value = "${minPitchHz.toInt()} Hz\n(${PitchExtractionViewModel.hzToNoteName(minPitchHz)})"
                )
                StatCell(
                    title = "Max",
                    value = "${maxPitchHz.toInt()} Hz\n(${PitchExtractionViewModel.hzToNoteName(maxPitchHz)})"
                )
            }

            // Pitch range
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Pitch Range:", style = MaterialTheme.typography.bodySmall)
                Text(
                    "%.1f semitones".format(rangeSemitones),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun StatCell(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PitchGraphSection(pitchesHz: FloatArray, times: FloatArray) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Pitch Contour",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        PitchGraphView(
            pitchesHz = pitchesHz,
            times = times,
            title = "Sample Voice",
            height = 200.dp
        )
    }
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
                text = "APIs Demonstrated:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "• CalibraPitch.createContourExtractor(config: .scoring)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• extractor.extract(audio:sampleRate:) → PitchContour",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• ContourCleanup: .raw, .scoring, .display",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "• PitchContour: duration, voicedRatio, pitchesHz, times",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
