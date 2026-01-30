package com.musicmuni.voxatrace.demo.sections.melodyeval.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.musicmuni.voxatrace.calibra.model.SingingResult
import com.musicmuni.voxatrace.demo.sections.melodyeval.viewmodel.MelodyEvalViewModel

/**
 * Melody evaluation view with singalong mode.
 * Students hear the reference melody (first 4 segments of Alankaar 01) while recording.
 */
@Composable
fun MelodyEvalView(
    viewModel: MelodyEvalViewModel = viewModel()
) {
    val context = LocalContext.current

    val referenceLoaded by viewModel.referenceLoaded.collectAsState()
    val isSingalongActive by viewModel.isSingalongActive.collectAsState()
    val hasRecording by viewModel.hasRecording.collectAsState()
    val recordingDuration by viewModel.recordingDuration.collectAsState()
    val recordingLevel by viewModel.recordingLevel.collectAsState()
    val isEvaluating by viewModel.isEvaluating.collectAsState()
    val result by viewModel.result.collectAsState()
    val status by viewModel.status.collectAsState()
    val isPreparing by viewModel.isPreparing.collectAsState()
    val isReady by viewModel.isReady.collectAsState()
    val currentSegmentIndex by viewModel.currentSegmentIndex.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            // Cleanup handled by ViewModel onCleared
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Melody Singalong",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = status,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (!referenceLoaded) {
            // Step 1: Load reference
            LoadReferenceSection(
                referenceName = viewModel.referenceName,
                segmentNames = viewModel.segmentNames,
                onLoadReference = { viewModel.loadReference(context) }
            )
        } else {
            // Pattern display showing what to sing
            PatternDisplaySection(
                segmentNames = viewModel.segmentNames,
                currentSegmentIndex = currentSegmentIndex,
                getSegmentScore = { viewModel.segmentScore(it) }
            )

            // Step 2: Singalong (play + record)
            SingalongSection(
                isSingalongActive = isSingalongActive,
                isPreparing = isPreparing,
                isReady = isReady,
                hasRecording = hasRecording,
                isEvaluating = isEvaluating,
                recordingDuration = recordingDuration,
                recordingLevel = recordingLevel,
                onStartSingalong = { viewModel.startSingalong(context) },
                onStopSingalong = { viewModel.stopSingalong() }
            )

            // Results (shown after evaluation)
            result?.let { res ->
                ResultsSection(
                    result = res,
                    segmentNames = viewModel.segmentNames,
                    getSegmentScore = { viewModel.segmentScore(it) }
                )
            }
        }
    }
}

@Composable
private fun LoadReferenceSection(
    referenceName: String,
    segmentNames: List<String>,
    onLoadReference: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Step 1: Load Reference",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "First 4 phrases of $referenceName:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Preview what will be loaded
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            segmentNames.forEach { name ->
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Button(onClick = onLoadReference) {
            Text("Load $referenceName")
        }
    }
}

@Composable
private fun PatternDisplaySection(
    segmentNames: List<String>,
    currentSegmentIndex: Int,
    getSegmentScore: (Int) -> Float?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Phrases to Sing:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ) {
            LazyRow(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(segmentNames) { index, name ->
                    SegmentChip(
                        name = name,
                        isActive = currentSegmentIndex == index,
                        score = getSegmentScore(index)
                    )
                }
            }
        }
    }
}

@Composable
private fun SegmentChip(
    name: String,
    isActive: Boolean,
    score: Float?
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            score != null && score >= 0.8f -> Color(0xFF4CAF50) // Green
            score != null && score >= 0.6f -> Color(0xFFFF9800) // Orange
            score != null -> Color(0xFFF44336) // Red
            isActive -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        label = "segmentChipBackground"
    )

    val textColor = if (score != null || isActive) Color.White else MaterialTheme.colorScheme.onSurface

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
            if (score != null) {
                Text(
                    text = "${(score * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor.copy(alpha = 0.9f),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun SingalongSection(
    isSingalongActive: Boolean,
    isPreparing: Boolean,
    isReady: Boolean,
    hasRecording: Boolean,
    isEvaluating: Boolean,
    recordingDuration: Float,
    recordingLevel: Float,
    onStartSingalong: () -> Unit,
    onStopSingalong: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Step 2: Singalong",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "Listen to the reference and sing along",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                isSingalongActive -> {
                    Button(
                        onClick = onStopSingalong,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            Icons.Filled.Stop,
                            contentDescription = "Stop",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Stop")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                        Text(
                            text = formatTime(recordingDuration),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                isPreparing -> {
                    Button(
                        onClick = {},
                        enabled = false
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Preparing...")
                    }
                }

                else -> {
                    Button(
                        onClick = onStartSingalong,
                        enabled = isReady && !isEvaluating
                    ) {
                        Icon(
                            Icons.Filled.Mic,
                            contentDescription = "Singalong",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(if (hasRecording) "Try Again" else "Start Singalong")
                    }

                    if (hasRecording && !isEvaluating) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Recording complete",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        if (isSingalongActive) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Level:",
                    style = MaterialTheme.typography.bodySmall
                )
                LinearProgressIndicator(
                    progress = { recordingLevel.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            }
        }

        if (isEvaluating) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Text(
                    text = "Evaluating...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ResultsSection(
    result: SingingResult,
    segmentNames: List<String>,
    getSegmentScore: (Int) -> Float?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Results",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )

        // Overall score card
        OverallScoreCard(result)

        // Per-segment breakdown
        if (result.segmentResults.isNotEmpty()) {
            Text(
                text = "Per-Phrase Scores",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            result.segmentResults.entries.sortedBy { it.key }.forEach { (index, attempts) ->
                attempts.lastOrNull()?.let { segmentResult ->
                    val phraseName = if (index < segmentNames.size) {
                        segmentNames[index]
                    } else {
                        "Phrase ${index + 1}"
                    }
                    SegmentRow(
                        phraseName = phraseName,
                        lyrics = segmentResult.segment.lyrics.trim(),
                        score = segmentResult.score
                    )
                }
            }
        }
    }
}

@Composable
private fun OverallScoreCard(result: SingingResult) {
    val backgroundColor = when {
        result.overallScore >= 0.8f -> Color(0xFF4CAF50)
        result.overallScore >= 0.6f -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }

    val feedbackMessage = when {
        result.overallScore >= 0.9f -> "Excellent! Perfect performance."
        result.overallScore >= 0.8f -> "Great job! Almost perfect."
        result.overallScore >= 0.7f -> "Good job! Keep practicing."
        result.overallScore >= 0.5f -> "Not bad. Focus on pitch accuracy."
        else -> "Keep practicing! Match each phrase carefully."
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Overall Score",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
            Text(
                text = "${(result.overallScore * 100).toInt()}%",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = feedbackMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun SegmentRow(
    phraseName: String,
    lyrics: String,
    score: Float
) {
    val backgroundColor = when {
        score >= 0.8f -> Color(0xFF4CAF50).copy(alpha = 0.2f)
        score >= 0.6f -> Color(0xFFFF9800).copy(alpha = 0.2f)
        else -> Color(0xFFF44336).copy(alpha = 0.2f)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = phraseName,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "($lyrics)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
            Text(
                text = "${(score * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun formatTime(seconds: Float): String {
    val totalSeconds = seconds.toInt()
    val mins = totalSeconds / 60
    val secs = totalSeconds % 60
    val hundredths = ((seconds - totalSeconds) * 100).toInt()
    return "%d:%02d.%02d".format(mins, secs, hundredths)
}
