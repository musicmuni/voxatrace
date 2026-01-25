package com.musicmuni.voxatrace.demo.calibra.pitch

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.model.PitchAlgorithm
import com.musicmuni.voxatrace.calibra.model.PitchContour
import com.musicmuni.voxatrace.calibra.model.PitchPreset
import com.musicmuni.voxatrace.demo.util.AssetUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Batch pitch extraction demo.
 *
 * Demonstrates extracting a complete pitch contour from an audio file
 * using ContourExtractor with different presets.
 */
@Composable
fun PitchExtractionDemo() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedPreset by remember { mutableIntStateOf(1) } // BALANCED
    var isExtracting by remember { mutableStateOf(false) }
    var contour by remember { mutableStateOf<PitchContour?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

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

            try {
                // Load test audio from assets
                val audioPath = withContext(Dispatchers.IO) {
                    AssetUtils.copyAssetToCache(context, "test_audio.m4a")
                }

                if (audioPath == null) {
                    error = "No test audio file found in assets"
                    isExtracting = false
                    return@launch
                }

                // Create extractor with YIN (no model needed)
                val extractor = CalibraPitch.createContourExtractor(
                    preset = presets[selectedPreset].first,
                    algorithm = PitchAlgorithm.YIN,
                    sampleRate = 16000,
                    hopMs = 10
                )

                // Extract would require decoding the audio first
                // For demo purposes, show the API pattern
                error = "Demo: Add audio decoding to extract contour from file"

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
        if (contour != null) {
            ContourResultCard(contour!!)
        }

        if (error != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error!!,
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
            Text(if (isExtracting) "Extracting..." else "Extract from File")
        }

        // API info
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
                        val extractor = CalibraPitch.createContourExtractor(
                            preset = PitchPreset.BALANCED,
                            algorithm = PitchAlgorithm.YIN
                        )
                        val contour = extractor.extract(audioSamples)
                        extractor.release()
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ContourResultCard(contour: PitchContour) {
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
                StatItem("Points", "${contour.points.size}")
                StatItem("Duration", "%.1fs".format(contour.durationMs / 1000f))
                StatItem("Hop", "${contour.hopMs}ms")
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
