package com.musicmuni.voxatrace.demo.sections.singalong.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
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
import com.musicmuni.voxatrace.calibra.CalibraMusic
import com.musicmuni.voxatrace.calibra.model.SegmentResult
import com.musicmuni.voxatrace.demo.sections.shared.ContourData
import com.musicmuni.voxatrace.demo.sections.shared.PitchGraphView
import com.musicmuni.voxatrace.demo.sections.singalong.model.SessionPreset
import com.musicmuni.voxatrace.demo.sections.singalong.model.SingalongUIState
import com.musicmuni.voxatrace.demo.sections.singalong.viewmodel.SingalongViewModel

/**
 * Main view for Singalong Practice demo.
 *
 * This view is purely declarative UI - all business logic lives in SingalongViewModel.
 * Demonstrates clean MVVM separation where:
 * - View: Renders UI based on ViewModel state
 * - ViewModel: Manages CalibraLiveEval session and transforms state
 */
@Composable
fun SingalongView(viewModel: SingalongViewModel = viewModel()) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val segments by viewModel.segments.collectAsState()
    val currentSegmentIndex by viewModel.currentSegmentIndex.collectAsState()
    val completedSegmentIndices by viewModel.completedSegmentIndices.collectAsState()
    val completedResults by viewModel.completedResults.collectAsState()
    val practicePhase by viewModel.practicePhase.collectAsState()
    val currentPitch by viewModel.currentPitch.collectAsState()
    val segmentProgress by viewModel.segmentProgress.collectAsState()
    val lastResult by viewModel.lastResult.collectAsState()
    val selectedPreset by viewModel.selectedPreset.collectAsState()

    DisposableEffect(Unit) {
        viewModel.onAppear(context)
        onDispose {
            viewModel.onDisappear()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Singalong Practice",
                style = MaterialTheme.typography.titleMedium
            )
            PresetPicker(
                selectedPreset = selectedPreset,
                onSelect = { viewModel.changePreset(it) }
            )
        }

        // Content
        when (val state = uiState) {
            is SingalongUIState.Idle,
            is SingalongUIState.Loading -> LoadingView()

            is SingalongUIState.Error -> ErrorView(state.message)

            is SingalongUIState.Ready,
            is SingalongUIState.Practicing -> PracticeView(
                segmentCount = segments.size,
                currentIndex = currentSegmentIndex,
                completedIndices = completedSegmentIndices,
                currentLyrics = viewModel.currentLyrics,
                isPracticing = viewModel.isPracticing,
                currentPitch = currentPitch,
                segmentProgress = segmentProgress,
                lastResult = lastResult,
                canGoPrevious = viewModel.canGoPrevious,
                canGoNext = viewModel.canGoNext,
                canRetry = viewModel.canRetry,
                onSegmentTap = viewModel::goToSegment,
                onPlay = viewModel::play,
                onPause = viewModel::pause,
                onPrevious = viewModel::previousSegment,
                onRetry = viewModel::retry,
                onNext = viewModel::nextSegment,
                onReset = viewModel::reset,
                onFinish = viewModel::finish
            )

            is SingalongUIState.Summary -> SummaryView(
                segmentCount = segments.size,
                completedResults = completedResults,
                onPracticeAgain = viewModel::reset
            )
        }
    }
}

@Composable
private fun LoadingView() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
        Text(
            text = "Loading lesson...",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorView(message: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun PracticeView(
    segmentCount: Int,
    currentIndex: Int,
    completedIndices: Set<Int>,
    currentLyrics: String,
    isPracticing: Boolean,
    currentPitch: Float,
    segmentProgress: Float,
    lastResult: SegmentResult?,
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    canRetry: Boolean,
    onSegmentTap: (Int) -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onPrevious: () -> Unit,
    onRetry: () -> Unit,
    onNext: () -> Unit,
    onReset: () -> Unit,
    onFinish: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Progress bar
        SegmentProgressBar(
            segmentCount = segmentCount,
            currentIndex = currentIndex,
            completedIndices = completedIndices,
            onSegmentTap = onSegmentTap
        )

        HorizontalDivider()

        // Lyrics
        if (currentLyrics.isNotEmpty()) {
            LyricsCard(lyrics = currentLyrics)
        }

        // Live pitch feedback (only during practice)
        if (isPracticing) {
            PitchFeedbackCard(
                pitch = currentPitch,
                progress = segmentProgress,
                noteName = if (currentPitch > 0) CalibraMusic.midiToNoteLabel(CalibraMusic.hzToMidi(currentPitch)) else "-"
            )
        }

        // Last result
        lastResult?.let { result ->
            SegmentResultCard(result = result)
        }

        // Practice button
        PracticeButton(
            isPracticing = isPracticing,
            onStart = onPlay,
            onStop = onPause
        )

        HorizontalDivider()

        // Navigation
        NavigationControls(
            canGoPrevious = canGoPrevious,
            canGoNext = canGoNext,
            canRetry = canRetry,
            onPrevious = onPrevious,
            onRetry = onRetry,
            onNext = onNext
        )

        // Session controls
        SessionControls(
            onReset = onReset,
            onFinish = onFinish
        )
    }
}

@Composable
private fun SegmentProgressBar(
    segmentCount: Int,
    currentIndex: Int,
    completedIndices: Set<Int>,
    onSegmentTap: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            (0 until segmentCount).forEach { index ->
                SegmentChip(
                    index = index,
                    isActive = index == currentIndex,
                    isCompleted = completedIndices.contains(index),
                    onTap = { onSegmentTap(index) }
                )
            }
        }
        Text(
            text = "Segment ${currentIndex + 1} of $segmentCount",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SegmentChip(
    index: Int,
    isActive: Boolean,
    isCompleted: Boolean,
    onTap: () -> Unit
) {
    val backgroundColor = when {
        isCompleted -> Color(0xFF4CAF50)
        isActive -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onTap),
        contentAlignment = Alignment.Center
    ) {
        if (isCompleted) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.White
            )
        } else {
            Text(
                text = "${index + 1}",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LyricsCard(lyrics: String) {
    Text(
        text = lyrics,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                MaterialTheme.shapes.small
            )
            .padding(12.dp)
    )
}

@Composable
private fun PitchFeedbackCard(
    pitch: Float,
    progress: Float,
    noteName: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pitch: ${if (pitch > 0) "%.1f".format(pitch) else "-"} Hz",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = noteName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SegmentResultCard(result: SegmentResult) {
    val scoreColor = when {
        result.score >= 0.8f -> Color(0xFF4CAF50)
        result.score >= 0.6f -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.error
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Segment ${result.segment.index + 1}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${(result.score * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = scoreColor
            )
            Text(
                text = result.feedbackMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Pitch comparison graph
        val refData = result.referencePitchData
        val stdData = result.studentPitchData

        if (refData.pitchesMidi.isNotEmpty() || stdData.pitchesMidi.isNotEmpty()) {
            PitchGraphView(
                contours = listOf(
                    ContourData(
                        pitches = refData.pitchesMidi,
                        color = Color.Blue,
                        label = "Reference",
                        times = refData.times
                    ),
                    ContourData(
                        pitches = stdData.pitchesMidi,
                        color = Color(0xFFFF9800),
                        label = "You",
                        times = stdData.times
                    )
                ),
                height = 120,
                inputIsMidi = true
            )
        }
    }
}

@Composable
private fun PracticeButton(
    isPracticing: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    if (isPracticing) {
        Button(
            onClick = onStop,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Stop")
        }
    } else {
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Practice")
        }
    }
}

@Composable
private fun NavigationControls(
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    canRetry: Boolean,
    onPrevious: () -> Unit,
    onRetry: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onPrevious,
            enabled = canGoPrevious,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
            Text("Prev")
        }
        OutlinedButton(
            onClick = onRetry,
            enabled = canRetry,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Text("Retry")
        }
        OutlinedButton(
            onClick = onNext,
            enabled = canGoNext,
            modifier = Modifier.weight(1f)
        ) {
            Text("Next")
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Composable
private fun SessionControls(
    onReset: () -> Unit,
    onFinish: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onReset,
            modifier = Modifier.weight(1f)
        ) {
            Text("Start Over")
        }
        Button(
            onClick = onFinish,
            modifier = Modifier.weight(1f)
        ) {
            Text("Finish")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PresetPicker(
    selectedPreset: SessionPreset,
    onSelect: (SessionPreset) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        Surface(
            modifier = Modifier
                .menuAnchor()
                .clickable { expanded = true },
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.small
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = selectedPreset.displayName,
                    style = MaterialTheme.typography.labelMedium
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SessionPreset.entries.forEach { preset ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(preset.displayName)
                            Text(
                                text = preset.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    onClick = {
                        onSelect(preset)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SummaryView(
    segmentCount: Int,
    completedResults: Map<Int, List<SegmentResult>>,
    onPracticeAgain: () -> Unit
) {
    val scores = completedResults.values.mapNotNull { it.lastOrNull()?.score }
    val overallScore = if (scores.isNotEmpty()) scores.average().toFloat() else 0f
    val scoreColor = when {
        overallScore >= 0.8f -> Color(0xFF4CAF50)
        overallScore >= 0.6f -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.error
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Session Complete",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Overall score
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${(overallScore * 100).toInt()}%",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = scoreColor
            )
            Text(
                text = getPerformanceLevel(overallScore),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        HorizontalDivider()

        // Segment results grid
        Text(
            text = "Segment Results",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(200.dp)
        ) {
            items(segmentCount) { index ->
                val result = completedResults[index]?.lastOrNull()
                if (result != null) {
                    MiniResultCard(segmentIndex = index, result = result)
                } else {
                    NotPracticedCard(index = index)
                }
            }
        }

        HorizontalDivider()

        // Statistics
        StatisticsView(
            practicedCount = completedResults.size,
            totalCount = segmentCount,
            scores = scores
        )

        // Practice again
        Button(
            onClick = onPracticeAgain,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Practice Again")
        }
    }
}

@Composable
private fun MiniResultCard(segmentIndex: Int, result: SegmentResult) {
    val scoreColor = when {
        result.score >= 0.8f -> Color(0xFF4CAF50)
        result.score >= 0.6f -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.error
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Segment ${segmentIndex + 1}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "${(result.score * 100).toInt()}%",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = scoreColor
        )
        Text(
            text = getPerformanceLevel(result.score),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NotPracticedCard(index: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Segment ${index + 1}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Not practiced",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.height(60.dp)
        )
    }
}

@Composable
private fun StatisticsView(
    practicedCount: Int,
    totalCount: Int,
    scores: List<Float>
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Statistics",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(label = "Practiced", value = "$practicedCount/$totalCount")
            StatItem(
                label = "Best",
                value = if (scores.isNotEmpty()) "${(scores.max() * 100).toInt()}%" else "-"
            )
            StatItem(
                label = "Average",
                value = if (scores.isNotEmpty()) "${(scores.average() * 100).toInt()}%" else "-"
            )
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun getPerformanceLevel(score: Float): String = when {
    score >= 0.9f -> "Excellent"
    score >= 0.8f -> "Great"
    score >= 0.7f -> "Good"
    score >= 0.6f -> "Fair"
    else -> "Needs Practice"
}
