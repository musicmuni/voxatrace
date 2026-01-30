package com.musicmuni.voxatrace.demo.sections.shared

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musicmuni.voxatrace.calibra.CalibraMusic
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Data point for a pitch contour segment.
 */
data class PitchDataPoint(
    val time: Float,
    val pitch: Float,  // In MIDI note numbers
    val series: String,
    val segmentId: String
)

/**
 * Segments a pitch contour into consecutive voiced regions.
 * Returns array of PitchDataPoint with MIDI note numbers.
 *
 * @param inputIsMidi If true, pitches are already in MIDI format (skip Hz->MIDI conversion)
 * @param gapThreshold Time gap in seconds to start a new segment (default 25ms)
 */
private fun segmentPitchContour(
    times: List<Float>,
    pitches: List<Float>,
    seriesName: String,
    inputIsMidi: Boolean = false,
    gapThreshold: Float = 0.025f
): List<PitchDataPoint> {
    val points = mutableListOf<PitchDataPoint>()
    var segmentIndex = 0
    var lastValidTime: Float? = null

    for ((time, pitch) in times.zip(pitches)) {
        val midiNote: Float = if (inputIsMidi) {
            pitch
        } else {
            CalibraMusic.hzToMidi(pitch)
        }

        // Skip invalid pitches (unvoiced sentinel is -1 for MIDI, <= 0 for Hz)
        if (midiNote.isNaN() || midiNote <= 0) {
            lastValidTime = null
            continue
        }

        // Check for time gap (unvoiced region passed)
        lastValidTime?.let { last ->
            if ((time - last) > gapThreshold) {
                segmentIndex++
            }
        }

        val segmentId = "${seriesName}_$segmentIndex"
        points.add(PitchDataPoint(
            time = time,
            pitch = midiNote,
            series = seriesName,
            segmentId = segmentId
        ))
        lastValidTime = time
    }

    return points
}

/**
 * Contour data for multi-contour graphs.
 */
data class ContourData(
    val pitches: List<Float>,
    val color: Color,
    val label: String,
    val times: List<Float>? = null
)

/**
 * A reusable pitch contour graph using Compose Canvas.
 *
 * Displays pitch values over time with musical note labels on Y-axis.
 * Supports both single contour and multi-contour overlay modes.
 */
@Composable
fun PitchGraphView(
    pitchesHz: List<Float>,
    times: List<Float>? = null,
    color: Color = Color.Blue,
    title: String? = null,
    series: String = "Pitch",
    height: Int = 200,
    inputIsMidi: Boolean = false
) {
    val timeValues = times ?: pitchesHz.indices.map { it * 0.01f }
    val dataPoints = remember(pitchesHz, times, series, inputIsMidi) {
        segmentPitchContour(timeValues, pitchesHz, series, inputIsMidi)
    }
    val seriesColors = mapOf(series to color)

    PitchGraphContent(
        dataPoints = dataPoints,
        seriesColors = seriesColors,
        title = title,
        height = height
    )
}

/**
 * Initialize with multiple pitch contours for overlay comparison.
 * Uses a shared times array for all contours.
 */
@Composable
fun PitchGraphView(
    contours: List<ContourData>,
    times: List<Float>? = null,
    title: String? = null,
    height: Int = 200,
    inputIsMidi: Boolean = false
) {
    val dataPoints = remember(contours, times, inputIsMidi) {
        contours.flatMap { contour ->
            val timeValues = contour.times ?: times ?: contour.pitches.indices.map { it * 0.01f }
            segmentPitchContour(timeValues, contour.pitches, contour.label, inputIsMidi)
        }
    }
    val seriesColors = contours.associate { it.label to it.color }

    PitchGraphContent(
        dataPoints = dataPoints,
        seriesColors = seriesColors,
        title = title,
        height = height
    )
}

/**
 * Initialize with multiple pitch contours, each with its own times array.
 */
@Composable
fun PitchGraphViewWithTimes(
    contoursWithTimes: List<Triple<List<Float>, List<Float>, Pair<Color, String>>>,
    title: String? = null,
    height: Int = 200,
    inputIsMidi: Boolean = false
) {
    val dataPoints = remember(contoursWithTimes, inputIsMidi) {
        contoursWithTimes.flatMap { (times, pitches, colorLabel) ->
            segmentPitchContour(times, pitches, colorLabel.second, inputIsMidi)
        }
    }
    val seriesColors = contoursWithTimes.associate { it.third.second to it.third.first }

    PitchGraphContent(
        dataPoints = dataPoints,
        seriesColors = seriesColors,
        title = title,
        height = height
    )
}

@Composable
private fun PitchGraphContent(
    dataPoints: List<PitchDataPoint>,
    seriesColors: Map<String, Color>,
    title: String?,
    height: Int
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
    val onSurfaceColor = MaterialTheme.colorScheme.onSurfaceVariant
    val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)

    // Calculate domain
    val (yMin, yMax, timeMin, timeMax) = remember(dataPoints) {
        if (dataPoints.isEmpty()) {
            listOf(48f, 72f, 0f, 1f)
        } else {
            val pitches = dataPoints.map { it.pitch }
            val times = dataPoints.map { it.time }
            val minMidi = floor(pitches.min()) - 2
            val maxMidi = ceil(pitches.max()) + 2
            listOf(minMidi, maxMidi, times.min(), times.max())
        }
    }

    // Generate tick values
    val yTicks = remember(yMin, yMax) {
        val range = yMax - yMin
        val stride = when {
            range <= 12 -> 1f
            range <= 24 -> 2f
            else -> 3f
        }
        val ticks = mutableListOf<Float>()
        var current = ceil(yMin)
        while (current <= yMax) {
            ticks.add(current)
            current += stride
        }
        ticks
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(surfaceColor, MaterialTheme.shapes.small)
            .padding(8.dp)
    ) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = onSurfaceColor
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            // Y-axis labels
            Column(
                modifier = Modifier
                    .width(32.dp)
                    .height(height.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                yTicks.reversed().forEach { midi ->
                    Text(
                        text = CalibraMusic.midiToNoteLabel(midi),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        color = onSurfaceColor
                    )
                }
            }

            // Graph canvas
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .height(height.dp)
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Draw grid lines
                yTicks.forEach { midi ->
                    val y = mapY(midi, yMin, yMax, canvasHeight)
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(canvasWidth, y),
                        strokeWidth = 1f
                    )
                }

                if (dataPoints.isEmpty()) return@Canvas

                // Group by segment and draw each
                val segments = dataPoints.groupBy { it.segmentId }
                segments.forEach { (_, segmentPoints) ->
                    if (segmentPoints.size < 2) return@forEach

                    val seriesName = segmentPoints.first().series
                    val color = seriesColors[seriesName] ?: Color.Gray

                    val path = Path()
                    var started = false

                    segmentPoints.forEach { point ->
                        val x = mapX(point.time, timeMin, timeMax, canvasWidth)
                        val y = mapY(point.pitch, yMin, yMax, canvasHeight)

                        if (!started) {
                            path.moveTo(x, y)
                            started = true
                        } else {
                            path.lineTo(x, y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = color,
                        style = Stroke(width = 2f)
                    )
                }
            }
        }

        // X-axis label
        Text(
            text = "Time (s)",
            style = MaterialTheme.typography.labelSmall,
            color = onSurfaceColor,
            modifier = Modifier.padding(start = 32.dp, top = 4.dp)
        )

        // Legend for multiple series
        if (seriesColors.size > 1) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                seriesColors.forEach { (label, color) ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(start = 32.dp)
                    ) {
                        Canvas(modifier = Modifier.size(12.dp)) {
                            drawLine(
                                color = color,
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2),
                                strokeWidth = 2f
                            )
                        }
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = onSurfaceColor
                        )
                    }
                }
            }
        }
    }
}

private fun mapX(value: Float, min: Float, max: Float, canvasWidth: Float): Float {
    if (max == min) return canvasWidth / 2
    return ((value - min) / (max - min)) * canvasWidth
}

private fun mapY(value: Float, min: Float, max: Float, canvasHeight: Float): Float {
    if (max == min) return canvasHeight / 2
    return canvasHeight - ((value - min) / (max - min)) * canvasHeight
}

/**
 * Comparison graph for raw vs processed pitch contours.
 */
@Composable
fun PitchComparisonGraphView(
    rawPitches: List<Float>,
    processedPitches: List<Float>,
    times: List<Float>? = null,
    rawLabel: String = "Raw",
    processedLabel: String = "Processed",
    height: Int = 200
) {
    PitchGraphView(
        contours = listOf(
            ContourData(rawPitches, Color.Gray, rawLabel),
            ContourData(processedPitches, Color.Blue, processedLabel)
        ),
        times = times,
        title = "Raw vs Processed",
        height = height
    )
}

/**
 * Overload that accepts FloatArray and Dp height.
 */
@Composable
fun PitchGraphView(
    pitchesHz: FloatArray,
    times: FloatArray? = null,
    color: Color = Color.Blue,
    title: String? = null,
    series: String = "Pitch",
    height: Dp = 200.dp,
    inputIsMidi: Boolean = false
) {
    PitchGraphView(
        pitchesHz = pitchesHz.toList(),
        times = times?.toList(),
        color = color,
        title = title,
        series = series,
        height = height.value.toInt(),
        inputIsMidi = inputIsMidi
    )
}
