package com.musicmuni.voxatrace.demo.calibra.pitch

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
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.model.*
import com.musicmuni.voxatrace.sonix.AudioSessionManager
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixResampler
import kotlinx.coroutines.launch

/**
 * Real-time pitch detection demo with modern Builder API.
 *
 * Showcases:
 * - PitchPreset (RESPONSIVE, BALANCED, PRECISE)
 * - VoiceType (Western, Carnatic, Hindustani, Pop, IndianFilm)
 * - QuietHandling (SENSITIVE, NORMAL, NOISY)
 * - DetectionStrictness (STRICT, BALANCED, LENIENT)
 * - DetectorBuilder API with YIN and SwiftF0 algorithms
 */
@Composable
fun RealtimePitchDemo() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Configuration state
    var selectedAlgorithm by remember { mutableIntStateOf(0) } // YIN default (no model needed)
    var selectedPreset by remember { mutableIntStateOf(1) } // BALANCED
    var selectedVoiceType by remember { mutableIntStateOf(0) } // Auto

    // Runtime state
    var detector by remember { mutableStateOf<CalibraPitch.Detector?>(null) }
    var recorder by remember { mutableStateOf<SonixRecorder?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var currentPitch by remember { mutableStateOf<PitchPoint?>(null) }
    var amplitude by remember { mutableFloatStateOf(0f) }

    val algorithms = listOf(
        "YIN" to "Traditional algorithm, no model needed",
        "SwiftF0" to "Neural network, higher accuracy (requires model)"
    )

    val presets = listOf(
        PitchPreset.RESPONSIVE to "Responsive",
        PitchPreset.BALANCED to "Balanced",
        PitchPreset.PRECISE to "Precise"
    )

    val voiceTypes = listOf(
        VoiceType.Auto to "Auto",
        VoiceType.Western.Soprano to "Soprano",
        VoiceType.Western.Alto to "Alto",
        VoiceType.Western.Tenor to "Tenor",
        VoiceType.Western.Bass to "Bass",
        VoiceType.Carnatic.Male to "Carnatic M",
        VoiceType.Carnatic.Female to "Carnatic F"
    )

    fun createDetector(): CalibraPitch.Detector {
        val algorithm = if (selectedAlgorithm == 0) PitchAlgorithm.YIN else PitchAlgorithm.SWIFT_F0
        val preset = presets[selectedPreset].first
        val voiceType = voiceTypes[selectedVoiceType].first

        val builder = CalibraPitch.DetectorBuilder()
            .algorithm(algorithm)
            .preset(preset)
            .voiceType(voiceType)

        // SwiftF0 requires model provider - for now use YIN only in demo
        // In production, add: .modelProvider { ModelLoader.loadSwiftF0() }

        return builder.build()
    }

    fun startRecording() {
        scope.launch {
            val recordPath = "${context.cacheDir}/realtime_pitch.m4a"
            recorder?.release()
            recorder = SonixRecorder.create(recordPath, "m4a", "voice")

            detector?.release()
            detector = createDetector()

            recorder?.start()
            isRecording = true

            // Process audio buffers
            launch {
                recorder?.audioBuffers?.collect { buffer ->
                    val det = detector ?: return@collect

                    // Resample to 16kHz for pitch detection
                    val hwRate = AudioSessionManager.hardwareSampleRate.toInt()
                    val samples16k = SonixResampler.resample(
                        samples = buffer.data,
                        fromRate = hwRate,
                        toRate = 16000
                    )

                    // Detect pitch using modern API
                    val result = det.detect(samples16k)
                    val amp = det.getAmplitude(samples16k)

                    currentPitch = result
                    amplitude = amp
                }
            }
        }
    }

    fun stopRecording() {
        scope.launch {
            recorder?.stop()
            recorder?.release()
            recorder = null
            isRecording = false
            currentPitch = null
            amplitude = 0f
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            recorder?.release()
            detector?.release()
        }
    }

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
            algorithms.forEachIndexed { index, (name, _) ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = algorithms.size),
                    onClick = {
                        selectedAlgorithm = index
                        if (isRecording) {
                            stopRecording()
                        }
                    },
                    selected = selectedAlgorithm == index,
                    enabled = index == 0 // Only YIN for now (SwiftF0 needs model)
                ) {
                    Text(name, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        Text(
            text = algorithms[selectedAlgorithm].second,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Preset picker
        Text("Preset:", style = MaterialTheme.typography.labelMedium)
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            presets.forEachIndexed { index, (_, name) ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = presets.size),
                    onClick = { selectedPreset = index },
                    selected = selectedPreset == index
                ) {
                    Text(name, style = MaterialTheme.typography.labelSmall)
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
                value = voiceTypes[selectedVoiceType].second,
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
                voiceTypes.forEachIndexed { index, (_, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selectedVoiceType = index
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
            onClick = { if (isRecording) stopRecording() else startRecording() },
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
        PitchPoint.Tuning.FLAT -> "FLAT ($centsOff¢)"
        PitchPoint.Tuning.SHARP -> "SHARP (+$centsOff¢)"
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
                    // point.pitch, point.note, point.tuning...
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
