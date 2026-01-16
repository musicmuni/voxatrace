package com.musicmuni.calibra.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.musicmuni.vozos.calibra.CalibraOfflineEvaluator
import com.musicmuni.vozos.calibra.metrics.BreathAnalyzer
import com.musicmuni.vozos.calibra.metrics.ShrutiDetector
import com.musicmuni.vozos.sonix.util.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Offline Analysis Section demonstrating Calibra public APIs.
 *
 * Demonstrates:
 * - `BreathAnalyzer` for breath capacity analysis
 * - `ShrutiDetector` for speaking pitch detection from audio
 * - `CalibraOfflineEvaluator` for song evaluation using pre-recorded pitch data
 * - `Parser` from Sonix for reading .pitchPP and .trans files
 */
@Composable
fun OfflineAnalysisSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var breathCapacity by remember { mutableFloatStateOf(0f) }
    var shruti by remember { mutableFloatStateOf(0f) }
    var hasEnoughData by remember { mutableStateOf(false) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("Tap 'Analyze' to run offline analysis") }

    // Song evaluation state
    var songScore by remember { mutableFloatStateOf(0f) }
    var segmentFeedback by remember { mutableStateOf<List<SegmentFeedbackItem>>(emptyList()) }
    var isSongEvaluating by remember { mutableStateOf(false) }
    var songStatus by remember { mutableStateOf("Tap 'Evaluate Song' to demo CalibraOfflineEvaluator") }

    val analyze: () -> Unit = {
        isAnalyzing = true
        status = "Analyzing..."
        scope.launch {
            withContext(Dispatchers.Default) {
                // In a real app, you would load audio files and extract pitch here
                // For this example, we'll use placeholder data
                val times = floatArrayOf(0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f)
                val pitches = floatArrayOf(440f, 442f, 438f, 441f, 440f, 439f)
                val audioMono = FloatArray(16000) // 1 second of placeholder audio

                // Analyze breath capacity
                val enough = BreathAnalyzer.hasEnoughData(times, pitches)
                val capacity = if (enough) {
                    BreathAnalyzer.computeCapacity(times, pitches)
                } else 0f

                // Detect shruti from audio
                val detectedShruti = ShrutiDetector.detectFromAudio(audioMono, 16000)

                withContext(Dispatchers.Main) {
                    hasEnoughData = enough
                    breathCapacity = capacity
                    shruti = detectedShruti
                    isAnalyzing = false
                    status = "Analysis complete"
                }
            }
        }
    }

    val evaluateSong: () -> Unit = {
        isSongEvaluating = true
        songStatus = "Loading song data..."
        scope.launch {
            try {
                // Load pitch contour from song.pitchPP using Sonix Parser
                val pitchContent = withContext(Dispatchers.IO) {
                    context.assets.open("song.pitchPP").bufferedReader().readText()
                }
                val pitchData = Parser.parsePitchString(pitchContent)
                if (pitchData == null) {
                    songStatus = "Failed to parse song.pitchPP"
                    isSongEvaluating = false
                    return@launch
                }

                // Load transcription from song.trans using Sonix Parser
                val transContent = withContext(Dispatchers.IO) {
                    context.assets.open("song.trans").bufferedReader().readText()
                }
                val transData = Parser.parseTransString(transContent)
                if (transData == null) {
                    songStatus = "Failed to parse song.trans"
                    isSongEvaluating = false
                    return@launch
                }

                songStatus = "Evaluating ${transData.segments.size} segments..."

                // Extract segment boundaries from trans data
                val segments = transData.segments
                val refSegStarts = segments.map { it.startTime.toFloat() }.toFloatArray()
                val refSegEnds = segments.map { it.endTime.toFloat() }.toFloatArray()

                // For demo: use same pitch data as both reference and student
                // In a real app, student pitch would come from recorded audio
                val refTimes = pitchData.times
                val refPitches = pitchData.pitchesHz

                // Simulate student performance: same as reference with slight variations
                val studentTimes = refTimes
                val studentPitches = refPitches.map { pitch ->
                    if (pitch > 0) {
                        // Add small random variation to simulate imperfect singing
                        pitch * (0.97f + Math.random().toFloat() * 0.06f)
                    } else pitch
                }.toFloatArray()

                // Use same segment boundaries for student
                val stdSegStarts = refSegStarts
                val stdSegEnds = refSegEnds

                // Tonic frequency (G3 = 196 Hz)
                val tonic = 196.0f

                // Create offline evaluator and evaluate
                val result = withContext(Dispatchers.Default) {
                    val evaluator = CalibraOfflineEvaluator.create()
                    val evalResult = evaluator.evaluateSong(
                        refTimes, refPitches,
                        studentTimes, studentPitches,
                        refSegStarts, refSegEnds,
                        stdSegStarts, stdSegEnds,
                        tonic
                    )
                    evaluator.release()
                    evalResult
                }

                val overallScore = result.first
                val feedback = result.second

                // Map feedback to segment lyrics
                val feedbackItems = feedback.map { seg ->
                    val lyrics = if (seg.segmentIndex < segments.size) {
                        segments[seg.segmentIndex].lyrics
                    } else ""
                    SegmentFeedbackItem(seg.segmentIndex, seg.score, lyrics)
                }

                songScore = overallScore
                segmentFeedback = feedbackItems
                songStatus = "Evaluation complete - ${segments.size} segments"
                isSongEvaluating = false

            } catch (e: Exception) {
                songStatus = "Error: ${e.message}"
                isSongEvaluating = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Offline Analysis", style = MaterialTheme.typography.titleMedium)

        Text(
            text = status,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { /* TODO: Load reference audio */ }) { Text("Load Reference") }
            Button(onClick = { /* TODO: Load student audio */ }) { Text("Load Student") }
            Button(onClick = analyze, enabled = !isAnalyzing) { Text("Analyze") }
        }

        if (isAnalyzing) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        if (breathCapacity > 0 || shruti > 0) {
            ResultCard(title = "Breath Analysis") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Capacity", style = MaterialTheme.typography.labelSmall)
                        Text("${"%.2f".format(breathCapacity)}s", style = MaterialTheme.typography.titleLarge)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Has Enough Data", style = MaterialTheme.typography.labelSmall)
                        Text(
                            if (hasEnoughData) "Yes" else "No",
                            style = MaterialTheme.typography.titleLarge,
                            color = if (hasEnoughData) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    }
                }
            }

            ResultCard(title = "Speaking Pitch Detection") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Shruti", style = MaterialTheme.typography.labelSmall)
                        Text("${"%.2f".format(shruti)} Hz", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Song Evaluation Section
        Text(
            text = "Song Evaluation (CalibraOfflineEvaluator)",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = songStatus,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Button(onClick = evaluateSong, enabled = !isSongEvaluating) {
            Text("Evaluate Song")
        }

        if (isSongEvaluating) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        if (songScore > 0) {
            ResultCard(title = "Overall Score") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${"%.1f".format(songScore * 100)}%",
                        style = MaterialTheme.typography.displaySmall,
                        color = scoreColor(songScore)
                    )
                    Text(
                        "${segmentFeedback.size} segments evaluated",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (segmentFeedback.isNotEmpty()) {
                ResultCard(title = "Per-Segment Scores") {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        segmentFeedback.take(5).forEach { segment ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Seg ${segment.index + 1}",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.weight(0.15f)
                                )
                                Text(
                                    segment.lyrics.take(25) + if (segment.lyrics.length > 25) "..." else "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(0.65f),
                                    maxLines = 1
                                )
                                Text(
                                    "${"%.0f".format(segment.score * 100)}%",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = scoreColor(segment.score),
                                    modifier = Modifier.weight(0.2f)
                                )
                            }
                        }
                        if (segmentFeedback.size > 5) {
                            Text(
                                "... and ${segmentFeedback.size - 5} more segments",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // API Info
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(6.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("APIs Demonstrated:", style = MaterialTheme.typography.labelMedium)
                Text(
                    "• BreathAnalyzer.hasEnoughData(), .computeCapacity()",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "• ShrutiDetector.detectFromAudio()",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "• CalibraOfflineEvaluator.evaluateSong() with Parser",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ResultCard(title: String, content: @Composable () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}

@Composable
private fun scoreColor(score: Float): Color {
    return when {
        score >= 0.8f -> Color(0xFF4CAF50) // Green
        score >= 0.6f -> Color(0xFFFF9800) // Orange
        else -> Color(0xFFF44336) // Red
    }
}

private data class SegmentFeedbackItem(
    val index: Int,
    val score: Float,
    val lyrics: String
)
