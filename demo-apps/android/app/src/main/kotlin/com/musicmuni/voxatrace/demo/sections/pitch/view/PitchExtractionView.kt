package com.musicmuni.voxatrace.demo.sections.pitch.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.ContourExtractorConfig
import com.musicmuni.voxatrace.calibra.model.PitchAlgorithm
import com.musicmuni.voxatrace.calibra.model.PitchContour
import com.musicmuni.voxatrace.calibra.model.PitchPreset
import com.musicmuni.voxatrace.sonix.SonixDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Batch pitch extraction view.
 */
@Composable
fun PitchExtractionView() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedPreset by remember { mutableIntStateOf(1) }
    var isExtracting by remember { mutableStateOf(false) }
    var contour by remember { mutableStateOf<PitchContour?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var stats by remember { mutableStateOf<ExtractionStats?>(null) }

    val presets = listOf(
        PitchPreset.RESPONSIVE to "Responsive",
        PitchPreset.BALANCED to "Balanced",
        PitchPreset.PRECISE to "Precise"
    )

    fun extractPitch() {
        scope.launch {
            isExtracting = true
            error = null
            contour = null
            stats = null

            try {
                val audioPath = withContext(Dispatchers.IO) {
                    val file = File(context.cacheDir, "sample.m4a")
                    if (!file.exists()) {
                        context.assets.open("sample.m4a").use { input ->
                            FileOutputStream(file).use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                    file.absolutePath
                }

                val audioData = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(audioPath)
                }

                if (audioData == null) {
                    error = "Failed to decode audio"
                    isExtracting = false
                    return@launch
                }

                val extractorConfig = ContourExtractorConfig.Builder()
                    .pitchPreset(presets[selectedPreset].first)
                    .algorithm(PitchAlgorithm.YIN)
                    .sampleRate(audioData.sampleRate)
                    .hopMs(10)
                    .build()
                val extractor = CalibraPitch.createContourExtractor(extractorConfig)

                val result = withContext(Dispatchers.Default) {
                    extractor.extract(audioData.samples, audioData.sampleRate)
                }

                contour = result

                // Calculate stats
                val validPitches = result.pitchesMidi.filter { it > 0 }
                stats = ExtractionStats(
                    totalFrames = result.pitchesMidi.size,
                    voicedFrames = validPitches.size,
                    minMidi = validPitches.minOrNull() ?: 0f,
                    maxMidi = validPitches.maxOrNull() ?: 0f
                )

                extractor.release()
            } catch (e: Exception) {
                error = e.message ?: "Extraction failed"
            } finally {
                isExtracting = false
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Pitch Extraction",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Extract complete pitch contour from audio files",
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

        HorizontalDivider()

        // Results
        stats?.let { s ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Extraction Result",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total", style = MaterialTheme.typography.labelSmall)
                            Text("${s.totalFrames}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Column {
                            Text("Voiced", style = MaterialTheme.typography.labelSmall)
                            Text("${s.voicedFrames}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Column {
                            Text("Range", style = MaterialTheme.typography.labelSmall)
                            Text("%.0f-%.0f".format(s.minMidi, s.maxMidi), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        error?.let { e ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = e,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Extract button
        Button(
            onClick = { extractPitch() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isExtracting
        ) {
            if (isExtracting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(if (isExtracting) "Extracting..." else "Extract from sample.m4a")
        }
    }
}

private data class ExtractionStats(
    val totalFrames: Int,
    val voicedFrames: Int,
    val minMidi: Float,
    val maxMidi: Float
)
