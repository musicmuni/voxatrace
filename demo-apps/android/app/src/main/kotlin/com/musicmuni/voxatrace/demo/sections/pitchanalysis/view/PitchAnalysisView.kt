package com.musicmuni.voxatrace.demo.sections.pitchanalysis.view

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.musicmuni.voxatrace.common.MusicTheory
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.tona.PitchAnalysis
import com.musicmuni.voxatrace.tona.PitchDetection
import com.musicmuni.voxatrace.tona.model.HistogramConfig
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

private const val MAX_SEGMENTS_DISPLAY = 50

@Composable
fun PitchAnalysisView() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
                            FileOutputStream(file).use { output -> input.copyTo(output) }
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

                val extractorConfig = com.musicmuni.voxatrace.tona.model.ContourExtractorConfig.Builder()
                    .algorithm(com.musicmuni.voxatrace.tona.model.PitchAlgorithm.YIN)
                    .build()
                val extractor = PitchDetection.createContourExtractor(extractorConfig)
                val contour = withContext(Dispatchers.IO) {
                    extractor.extract(audioData.samples, audioData.sampleRate)
                }
                extractor.release()

                val tonicHz = 146.83f // D3

                // Folded histogram: 240 bins across one octave (5-cent resolution)
                val config = HistogramConfig.Builder()
                    .foldOctaves(true)
                    .numBins(240)
                    .build()

                val histogram = withContext(Dispatchers.IO) {
                    PitchAnalysis.computeHistogram(contour, tonicHz, config)
                }

                histogramBinCenters = histogram.binCenters
                histogramValues = histogram.values

                // Label by mean pitch, then merge consecutive same-label segments
                val intervals = MusicTheory.EQ_TEMPERED_INTERVALS_CENTS
                    .map { it.toFloat() }
                    .toFloatArray()

                val rawSegments = withContext(Dispatchers.IO) {
                    PitchAnalysis.labelByMeanPitch(contour, tonicHz, intervals)
                }

                // Merge consecutive segments with same label
                val merged = mutableListOf<TonalSegmentUi>()
                for (seg in rawSegments) {
                    val label = seg.label ?: "?"
                    val last = merged.lastOrNull()
                    if (last != null && last.label == label) {
                        merged[merged.lastIndex] = last.copy(endTime = seg.endSeconds)
                    } else {
                        merged.add(TonalSegmentUi(label = label, startTime = seg.startSeconds, endTime = seg.endSeconds))
                    }
                }
                tonalSegments = merged.take(MAX_SEGMENTS_DISPLAY)

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
        Text("Pitch Analysis", style = MaterialTheme.typography.titleMedium)

        Text(
            text = "Folded pitch histogram (one octave, 240 bins) and tonal segments",
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
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        if (histogramValues.isNotEmpty()) {
            HistogramChart(
                binCenters = histogramBinCenters,
                values = histogramValues
            )
        }

        if (tonalSegments.isNotEmpty()) {
            TonalSegmentsCard(
                segments = tonalSegments,
                totalCount = tonalSegments.size
            )
        }

        ApiInfoCard()
    }
}

private data class TonalSegmentUi(
    val label: String,
    val startTime: Float,
    val endTime: Float
)

/**
 * Landscape histogram drawn on Canvas.
 * X-axis = cents (0-1200), Y-axis = amplitude.
 * Note names shown at semitone boundaries.
 */
@Composable
private fun HistogramChart(
    binCenters: FloatArray,
    values: FloatArray
) {
    val maxValue = values.maxOrNull()?.takeIf { it > 0f } ?: 1f
    val noteNames = listOf("S", "r", "R", "g", "G", "M", "m", "P", "d", "D", "n", "N")
    val primaryColor = MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Pitch Histogram (folded, 1 octave)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                val chartLeft = 20f
                val chartRight = size.width - 8f
                val chartTop = 8f
                val chartBottom = size.height - 40f
                val chartWidth = chartRight - chartLeft
                val chartHeight = chartBottom - chartTop

                // Draw bars
                val barWidth = chartWidth / values.size
                for (i in values.indices) {
                    if (values[i] > 0f) {
                        val barHeight = (values[i] / maxValue) * chartHeight
                        drawRect(
                            color = primaryColor,
                            topLeft = Offset(chartLeft + i * barWidth, chartBottom - barHeight),
                            size = Size(barWidth.coerceAtLeast(1f), barHeight)
                        )
                    }
                }

                // Draw X-axis note labels at semitone boundaries
                val paint = android.graphics.Paint().apply {
                    textSize = 24f
                    color = android.graphics.Color.GRAY
                    textAlign = android.graphics.Paint.Align.CENTER
                }

                for (noteIdx in noteNames.indices) {
                    val cents = noteIdx * 100f + 50f // center of semitone region
                    val x = chartLeft + (cents / 1200f) * chartWidth
                    drawContext.canvas.nativeCanvas.drawText(
                        noteNames[noteIdx],
                        x,
                        size.height - 4f,
                        paint
                    )
                    // Grid line at semitone boundary (left edge of region)
                    val boundaryX = chartLeft + (noteIdx * 100f / 1200f) * chartWidth
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.3f),
                        start = Offset(boundaryX, chartTop),
                        end = Offset(boundaryX, chartBottom),
                        strokeWidth = 1f
                    )
                }
            }
        }
    }
}

@Composable
private fun TonalSegmentsCard(segments: List<TonalSegmentUi>, totalCount: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Sung Notes (${segments.size}${if (totalCount > segments.size) " of $totalCount" else ""})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Note", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                Text("Start", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                Text("Duration", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            }

            segments.forEach { segment ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(segment.label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                    Text("%.2f s".format(segment.startTime), style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                    Text("%.2f s".format(segment.endTime - segment.startTime), style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                }
            }

            Text(
                text = "Also see: PitchAnalysis.fitLinearSegments() for pitch slope analysis",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}

@Composable
private fun ApiInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("APIs Demonstrated:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
            Text("PitchAnalysis.computeHistogram(config: FOLDED, numBins: 240)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("PitchAnalysis.labelByMeanPitch() - note labeling (merged)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("PitchDetection.createContourExtractor() - pitch extraction", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
