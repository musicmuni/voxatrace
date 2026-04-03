package com.musicmuni.voxatrace.demo.sections.pitchanalysis.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.musicmuni.voxatrace.common.MusicTheory
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.tona.PitchAnalysis
import com.musicmuni.voxatrace.tona.PitchDetection
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Pitch Analysis View - histogram and tonal segment labeling via PitchAnalysis APIs.
 * Uses composable-local state (Pattern B) — no ViewModel.
 */
@Composable
fun PitchAnalysisView() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // State
    var isAnalyzing by remember { mutableStateOf(false) }
    var histogramBinCenters by remember { mutableStateOf(floatArrayOf()) }
    var histogramValues by remember { mutableStateOf(floatArrayOf()) }
    var tonalSegments by remember { mutableStateOf<List<TonalSegmentUi>>(emptyList()) }

    fun analyzeOffline() {
        scope.launch {
            isAnalyzing = true
            histogramBinCenters = floatArrayOf()
            histogramValues = floatArrayOf()
            tonalSegments = emptyList()

            try {
                val audioFile = withContext(Dispatchers.IO) {
                    val file = File(context.cacheDir, "Alankaar 01_voice.m4a")
                    if (!file.exists()) {
                        context.assets.open("Alankaar 01_voice.m4a").use { input ->
                            FileOutputStream(file).use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                    file
                }

                val audioData = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(audioFile.absolutePath)
                }

                if (audioData == null) {
                    Napier.e("Failed to decode audio file")
                    isAnalyzing = false
                    return@launch
                }

                // Extract pitch contour
                val extractor = PitchDetection.createContourExtractor()
                val contour = withContext(Dispatchers.IO) {
                    extractor.extract(audioData.samples, audioData.sampleRate)
                }
                extractor.release()

                val tonicHz = 261.63f

                // Compute histogram
                val histogram = withContext(Dispatchers.IO) {
                    PitchAnalysis.computeHistogram(contour, tonicHz)
                }

                if (histogram != null) {
                    histogramBinCenters = histogram.binCenters
                    histogramValues = histogram.values
                }

                // Label by mean pitch
                val intervals = MusicTheory.EQ_TEMPERED_INTERVALS_CENTS
                    .map { it.toFloat() }
                    .toFloatArray()

                val segments = withContext(Dispatchers.IO) {
                    PitchAnalysis.labelByMeanPitch(contour, tonicHz, intervals)
                }

                if (segments != null) {
                    tonalSegments = segments.map { seg ->
                        TonalSegmentUi(
                            label = seg.label ?: "?",
                            startTime = seg.startSeconds,
                            endTime = seg.endSeconds
                        )
                    }
                }

            } catch (e: Exception) {
                Napier.e("Pitch analysis failed", e)
            } finally {
                isAnalyzing = false
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Pitch Analysis",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Pitch histogram and tonal segment labeling",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            onClick = { analyzeOffline() },
            enabled = !isAnalyzing
        ) {
            Text("Analyze Alankaar Voice")
        }

        if (isAnalyzing) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Histogram display
        if (histogramValues.isNotEmpty()) {
            HistogramCard(
                binCenters = histogramBinCenters,
                values = histogramValues
            )
        }

        // Tonal segments display
        if (tonalSegments.isNotEmpty()) {
            TonalSegmentsCard(segments = tonalSegments)
        }

        ApiInfoCard()
    }
}

private data class TonalSegmentUi(
    val label: String,
    val startTime: Float,
    val endTime: Float
)

@Composable
private fun HistogramCard(
    binCenters: FloatArray,
    values: FloatArray
) {
    val maxValue = values.maxOrNull() ?: 1f

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Pitch Histogram",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            // Simple bar chart as stacked rows
            values.forEachIndexed { index, value ->
                if (value > 0f && index < binCenters.size) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "%.0f".format(binCenters[index]),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.width(40.dp)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(fraction = (value / maxValue).coerceIn(0f, 1f))
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.shapes.extraSmall
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TonalSegmentsCard(segments: List<TonalSegmentUi>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Tonal Segments",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Label",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Start",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Duration",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
            }

            segments.forEach { segment ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = segment.label,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "%.2f s".format(segment.startTime),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "%.2f s".format(segment.endTime - segment.startTime),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
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
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "APIs Demonstrated:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "PitchAnalysis.computeHistogram() - pitch distribution",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "PitchAnalysis.labelByMeanPitch() - tonal segment labeling",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "PitchAnalysis.estimateTuningOffset() - tuning estimation",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "PitchDetection.createContourExtractor() - pitch extraction",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "SonixDecoder.decode() - audio file loading",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
