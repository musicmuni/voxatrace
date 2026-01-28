package com.musicmuni.voxatrace.demo.calibra.pitch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.musicmuni.voxatrace.calibra.model.PitchAlgorithm
import com.musicmuni.voxatrace.calibra.model.PitchPoint
import com.musicmuni.voxatrace.calibra.model.PitchPreset
import com.musicmuni.voxatrace.sonix.AudioSessionManager
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixResampler
import kotlinx.coroutines.launch

/**
 * PitchPoint properties inspector demo.
 *
 * Showcases all computed properties of PitchPoint in real-time:
 * - pitch: Detected frequency in Hz
 * - isSinging: Voice activity detection
 * - note: Musical note name (A4, C#5, etc.)
 * - midiNote: MIDI note number
 * - centsOff: Deviation from nearest note
 * - tuning: SILENT, FLAT, IN_TUNE, SHARP
 * - reliability: Detection confidence
 */
@Composable
fun PitchPointExplorerDemo() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var detector by remember { mutableStateOf<CalibraPitch.Detector?>(null) }
    var recorder by remember { mutableStateOf<SonixRecorder?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var currentPitch by remember { mutableStateOf<PitchPoint?>(null) }
    var amplitude by remember { mutableFloatStateOf(0f) }

    fun startRecording() {
        scope.launch {
            val recordPath = "${context.cacheDir}/pitch_explorer.m4a"
            recorder?.release()
            recorder = SonixRecorder.create(recordPath, "m4a", "voice")

            detector?.release()
            detector = CalibraPitch.DetectorBuilder()
                .algorithm(PitchAlgorithm.YIN)
                .preset(PitchPreset.BALANCED)
                .build()

            recorder?.start()
            isRecording = true

            launch {
                recorder?.audioBuffers?.collect { buffer ->
                    val det = detector ?: return@collect

                    val hwRate = AudioSessionManager.hardwareSampleRate.toInt()
                    val samples16k = SonixResampler.resample(
                        samples = buffer.data,
                        fromRate = hwRate,
                        toRate = 16000
                    )

                    currentPitch = det.detect(samples16k)
                    amplitude = det.getAmplitude(samples16k)
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
            text = "PitchPoint Properties",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "See all computed properties of PitchPoint in real-time",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

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
                if (currentPitch != null && currentPitch!!.isSinging) {
                    Text(
                        text = currentPitch!!.note ?: "-",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "%.1f Hz".format(currentPitch!!.pitch),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tuning indicator bar
                    TuningBar(
                        centsOff = currentPitch!!.centsOff,
                        tuning = currentPitch!!.tuning
                    )
                } else {
                    Text(
                        text = "-",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "Not singing",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        HorizontalDivider()

        // Properties grid
        Text(
            text = "Properties Inspector",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PropertyRow("pitch", currentPitch?.let { "%.1f Hz".format(it.pitch) } ?: "-")
                PropertyRow("isSinging", currentPitch?.isSinging?.toString() ?: "-")
                PropertyRow("note", currentPitch?.note ?: "-")
                PropertyRow("midiNote", currentPitch?.let { if (it.isSinging) it.midiNote.toString() else "-" } ?: "-")
                PropertyRow("centsOff", currentPitch?.let { if (it.isSinging) "${it.centsOff}" else "-" } ?: "-")
                PropertyRow("tuning", currentPitch?.let { tuningString(it.tuning) } ?: "-")
                PropertyRow("reliability", currentPitch?.let { "%.2f".format(it.reliability) } ?: "-")
                PropertyRow("confidence", currentPitch?.let { "%.2f".format(it.confidence) } ?: "-")
            }
        }

        HorizontalDivider()

        // Control button
        Button(
            onClick = { if (isRecording) stopRecording() else startRecording() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRecording) "Stop" else "Start")
        }

        // API documentation
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
                Text("• pitch: Float - Detected pitch in Hz (-1 if silent)", style = MaterialTheme.typography.bodySmall)
                Text("• isSinging: Boolean - True if voiced audio", style = MaterialTheme.typography.bodySmall)
                Text("• note: String? - Musical note (A4, C#5)", style = MaterialTheme.typography.bodySmall)
                Text("• midiNote: Int - MIDI number (69 = A4)", style = MaterialTheme.typography.bodySmall)
                Text("• centsOff: Int - Deviation (-50 to +50)", style = MaterialTheme.typography.bodySmall)
                Text("• tuning: Tuning - SILENT/FLAT/IN_TUNE/SHARP", style = MaterialTheme.typography.bodySmall)
                Text("• reliability: Float - Detection confidence", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun PropertyRow(name: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
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
private fun TuningBar(centsOff: Int, tuning: PitchPoint.Tuning) {
    val color = when (tuning) {
        PitchPoint.Tuning.IN_TUNE -> Color.Green
        PitchPoint.Tuning.FLAT, PitchPoint.Tuning.SHARP -> Color(0xFFFF9800)
        else -> Color.Gray
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress bar representation
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        ) {
            LinearProgressIndicator(
                progress = { ((centsOff + 50) / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .align(Alignment.Center),
                color = color
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("-50¢", style = MaterialTheme.typography.labelSmall)
            Text("0", style = MaterialTheme.typography.labelSmall, color = Color.Green)
            Text("+50¢", style = MaterialTheme.typography.labelSmall)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = tuningString(tuning),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

private fun tuningString(tuning: PitchPoint.Tuning): String {
    return when (tuning) {
        PitchPoint.Tuning.SILENT -> "SILENT"
        PitchPoint.Tuning.FLAT -> "FLAT"
        PitchPoint.Tuning.IN_TUNE -> "IN TUNE"
        PitchPoint.Tuning.SHARP -> "SHARP"
    }
}
