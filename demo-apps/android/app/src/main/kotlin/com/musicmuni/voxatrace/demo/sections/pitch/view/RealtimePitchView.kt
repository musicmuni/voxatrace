package com.musicmuni.voxatrace.demo.sections.pitch.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.musicmuni.voxatrace.calibra.model.PitchPoint
import com.musicmuni.voxatrace.demo.sections.pitch.viewmodel.RealtimePitchViewModel
import com.musicmuni.voxatrace.demo.sections.shared.PitchGraphView
import kotlin.math.log2
import kotlin.math.roundToInt

/**
 * Real-time pitch detection view.
 */
@Composable
fun RealtimePitchView(viewModel: RealtimePitchViewModel = viewModel()) {
    val context = LocalContext.current

    val selectedAlgorithm by viewModel.selectedAlgorithm.collectAsStateWithLifecycle()
    val selectedPreset by viewModel.selectedPreset.collectAsStateWithLifecycle()
    val selectedVoiceType by viewModel.selectedVoiceType.collectAsStateWithLifecycle()
    val selectedQuietHandling by viewModel.selectedQuietHandling.collectAsStateWithLifecycle()
    val selectedStrictness by viewModel.selectedStrictness.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()
    val currentPitch by viewModel.currentPitch.collectAsStateWithLifecycle()
    val amplitude by viewModel.amplitude.collectAsStateWithLifecycle()
    val pitchHistory by viewModel.pitchHistory.collectAsStateWithLifecycle()
    val recordedPitches by viewModel.recordedPitches.collectAsStateWithLifecycle()
    val showGraph by viewModel.showGraph.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Configuration section
        ConfigurationSection(
            viewModel = viewModel,
            selectedAlgorithm = selectedAlgorithm,
            selectedPreset = selectedPreset,
            selectedVoiceType = selectedVoiceType,
            selectedQuietHandling = selectedQuietHandling,
            selectedStrictness = selectedStrictness,
            isRecording = isRecording
        )

        HorizontalDivider()

        // Live detection section
        Text(
            text = "Real-time Detection",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        // Live display
        PitchDisplayCard(
            pitch = currentPitch,
            amplitude = amplitude,
            sampleCount = recordedPitches.size
        )

        // RMS indicator
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("RMS:", style = MaterialTheme.typography.bodySmall)
            LinearProgressIndicator(
                progress = { amplitude.coerceIn(0f, 1f) },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp),
            )
        }

        // Scrolling pitch contour (only when recording)
        if (isRecording && pitchHistory.isNotEmpty()) {
            ScrollingPitchContourView(
                pitchHistory = pitchHistory,
                maxPoints = viewModel.maxHistoryPoints,
                height = 120.dp
            )
        }

        // Control buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.toggleRecording(context) },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isRecording) "Stop" else "Start")
            }

            if (showGraph && recordedPitches.isNotEmpty()) {
                OutlinedButton(
                    onClick = { viewModel.clearRecording() }
                ) {
                    Text("Clear")
                }
            }
        }

        // Session graph (shown after stopping)
        if (showGraph && recordedPitches.isNotEmpty()) {
            HorizontalDivider()
            SessionGraphSection(recordedPitches = recordedPitches)
        }

        HorizontalDivider()

        // API info
        ApiInfoCard()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ConfigurationSection(
    viewModel: RealtimePitchViewModel,
    selectedAlgorithm: Int,
    selectedPreset: Int,
    selectedVoiceType: Int,
    selectedQuietHandling: Int,
    selectedStrictness: Int,
    isRecording: Boolean
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
                        selected = selectedAlgorithm == index
                    ) {
                        Text(info.name)
                    }
                }
            }
            Text(
                text = viewModel.algorithms[selectedAlgorithm].description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
                        label = { Text(info.name) }
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
                onExpandedChange = { voiceTypeExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = viewModel.voiceTypes[selectedVoiceType].name,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
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

        // Environment and Strictness in a more compact layout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Environment section
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Environment",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    viewModel.quietHandlings.forEachIndexed { index, info ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedQuietHandling == index,
                                onClick = { viewModel.setSelectedQuietHandling(index) }
                            )
                            Text(
                                text = info.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }

            // Strictness section
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Strictness",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    viewModel.strictnesses.forEachIndexed { index, info ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedStrictness == index,
                                onClick = { viewModel.setSelectedStrictness(index) }
                            )
                            Text(
                                text = info.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionGraphSection(recordedPitches: List<Float>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Session Pitch Contour",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        val voicedCount = recordedPitches.count { it > 0 }
        Text(
            text = "$voicedCount voiced frames out of ${recordedPitches.size} total",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        PitchGraphView(
            pitchesHz = recordedPitches.toFloatArray(),
            title = "Recorded Session",
            height = 200.dp
        )
    }
}

/**
 * Scrolling pitch contour view for real-time visualization.
 */
@Composable
private fun ScrollingPitchContourView(
    pitchHistory: List<Float>,
    maxPoints: Int,
    height: androidx.compose.ui.unit.Dp
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
    val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    val lineColor = MaterialTheme.colorScheme.primary
    val centerLineColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)

    // Fixed Y-axis range: B2 (MIDI 47) to B4 (MIDI 71)
    val minMidi = 47f
    val maxMidi = 71f

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .background(surfaceColor)
    ) {
        val leftMargin = 40f
        val rightMargin = 10f
        val topMargin = 8f
        val bottomMargin = 8f

        val graphWidth = size.width - leftMargin - rightMargin
        val graphHeight = size.height - topMargin - bottomMargin
        val centerX = leftMargin + graphWidth / 2

        fun midiToY(midi: Float): Float {
            val normalizedMidi = (midi - minMidi) / (maxMidi - minMidi)
            return (topMargin + graphHeight * (1 - normalizedMidi))
        }

        // Draw horizontal grid lines
        for (midi in minMidi.toInt()..maxMidi.toInt() step 2) {
            val y = midiToY(midi.toFloat())
            drawLine(
                color = gridColor,
                start = Offset(leftMargin, y),
                end = Offset(size.width - rightMargin, y),
                strokeWidth = 0.5f
            )
        }

        // Draw center vertical line
        drawLine(
            color = centerLineColor,
            start = Offset(centerX, topMargin),
            end = Offset(centerX, size.height - bottomMargin),
            strokeWidth = 1f
        )

        // Draw pitch contour
        if (pitchHistory.isEmpty()) return@Canvas

        val pointSpacing = graphWidth / maxPoints
        val path = Path()
        var isDrawing = false

        pitchHistory.forEachIndexed { index, pitchHz ->
            val pointsFromEnd = pitchHistory.size - 1 - index
            val x = centerX - pointsFromEnd * pointSpacing

            if (x < leftMargin) return@forEachIndexed

            val isVoiced = pitchHz > 0
            if (isVoiced) {
                val midi = 69f + 12f * log2(pitchHz / 440f)
                if (!midi.isNaN() && midi.isFinite()) {
                    val clampedMidi = midi.coerceIn(minMidi, maxMidi)
                    val y = midiToY(clampedMidi)

                    if (isDrawing) {
                        path.lineTo(x, y)
                    } else {
                        path.moveTo(x, y)
                        isDrawing = true
                    }
                } else {
                    isDrawing = false
                }
            } else {
                isDrawing = false
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2f)
        )

        // Draw current pitch dot
        pitchHistory.lastOrNull()?.let { lastPitch ->
            if (lastPitch > 0) {
                val midi = 69f + 12f * log2(lastPitch / 440f)
                if (!midi.isNaN() && midi.isFinite()) {
                    val clampedMidi = midi.coerceIn(minMidi, maxMidi)
                    val y = midiToY(clampedMidi)
                    drawCircle(
                        color = lineColor,
                        radius = 4f,
                        center = Offset(centerX, y)
                    )
                }
            }
        }
    }
}

@Composable
private fun PitchDisplayCard(
    pitch: PitchPoint?,
    amplitude: Float,
    sampleCount: Int = 0
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
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Pitch column
                Column(horizontalAlignment = Alignment.Start) {
                    Text("Pitch", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (pitch != null && pitch.isSinging) {
                        Text(
                            text = "%.1f Hz".format(pitch.pitch),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "-",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                // Note column
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Note", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (pitch != null && pitch.isSinging) {
                        Text(
                            text = pitch.note ?: "-",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = "-",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                // Tuning column
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tuning", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (pitch != null && pitch.isSinging) {
                        val centsText = if (pitch.centsOff > 0) "+${pitch.centsOff}" else "${pitch.centsOff}"
                        val tuningColor = when (pitch.tuning) {
                            PitchPoint.Tuning.IN_TUNE -> Color.Green
                            PitchPoint.Tuning.FLAT, PitchPoint.Tuning.SHARP -> Color(0xFFFF9800)
                            else -> Color.Gray
                        }
                        Text(
                            text = "${centsText}¢",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = tuningColor
                        )
                    } else {
                        Text(
                            text = "-",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                // Samples column
                Column(horizontalAlignment = Alignment.End) {
                    Text("Samples", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "$sampleCount",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
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
