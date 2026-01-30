package com.musicmuni.voxatrace.demo.sections.noteeval.view

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
import com.musicmuni.voxatrace.demo.sections.noteeval.viewmodel.DifficultyPreset
import com.musicmuni.voxatrace.demo.sections.noteeval.viewmodel.ExerciseEvalResult
import com.musicmuni.voxatrace.demo.sections.noteeval.viewmodel.NoteEvalResult
import com.musicmuni.voxatrace.demo.sections.noteeval.viewmodel.NoteEvalViewModel
import kotlin.math.pow

/**
 * Note/Exercise evaluation view with singalong mode.
 * Students hear the reference notes while recording their performance.
 */
@Composable
fun NoteEvalView(
    viewModel: NoteEvalViewModel = viewModel()
) {
    val context = LocalContext.current

    val selectedExercise by viewModel.selectedExercise.collectAsState()
    val isSingalongActive by viewModel.isSingalongActive.collectAsState()
    val hasRecording by viewModel.hasRecording.collectAsState()
    val recordingDuration by viewModel.recordingDuration.collectAsState()
    val recordingLevel by viewModel.recordingLevel.collectAsState()
    val isEvaluating by viewModel.isEvaluating.collectAsState()
    val result by viewModel.result.collectAsState()
    val status by viewModel.status.collectAsState()
    val isPreparing by viewModel.isPreparing.collectAsState()
    val isReady by viewModel.isReady.collectAsState()
    val selectedPreset by viewModel.selectedPreset.collectAsState()
    val noteDurationMs by viewModel.noteDurationMs.collectAsState()

    // Prepare on first load
    LaunchedEffect(Unit) {
        viewModel.prepare(context)
    }

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
            text = "Note Singalong",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = status,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Step 1: Select exercise
        ExercisePickerSection(
            exercises = viewModel.exercises,
            selectedExercise = selectedExercise,
            selectedPreset = selectedPreset,
            noteDurationMs = noteDurationMs,
            availableDurations = viewModel.availableDurations,
            onSelectExercise = { index ->
                viewModel.selectExercise(index)
                viewModel.prepare(context)
            },
            onSelectPreset = { viewModel.setPreset(it) },
            onSelectDuration = { ms ->
                viewModel.setNoteDuration(ms)
                viewModel.prepare(context)
            }
        )

        // Visual pattern display
        PatternDisplaySection(
            midiNotes = viewModel.currentMidiNotes,
            getNoteResult = { viewModel.noteResult(it) }
        )

        // Step 2: Singalong (play + record)
        SingalongSection(
            isSingalongActive = isSingalongActive,
            isPreparing = isPreparing,
            hasRecording = hasRecording,
            isEvaluating = isEvaluating,
            recordingDuration = recordingDuration,
            recordingLevel = recordingLevel,
            onStartSingalong = { viewModel.startSingalong(context) },
            onStopSingalong = { viewModel.stopSingalong() }
        )

        // Results (shown after evaluation)
        result?.let { res ->
            ResultsSection(result = res)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExercisePickerSection(
    exercises: List<com.musicmuni.voxatrace.demo.sections.noteeval.viewmodel.ExerciseInfo>,
    selectedExercise: Int,
    selectedPreset: DifficultyPreset,
    noteDurationMs: Int,
    availableDurations: List<Int>,
    onSelectExercise: (Int) -> Unit,
    onSelectPreset: (DifficultyPreset) -> Unit,
    onSelectDuration: (Int) -> Unit
) {
    var exerciseMenuExpanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Step 1: Select Exercise",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium
        )

        // Exercise dropdown
        ExposedDropdownMenuBox(
            expanded = exerciseMenuExpanded,
            onExpandedChange = { exerciseMenuExpanded = !exerciseMenuExpanded }
        ) {
            OutlinedTextField(
                value = exercises[selectedExercise].name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Exercise") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = exerciseMenuExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = exerciseMenuExpanded,
                onDismissRequest = { exerciseMenuExpanded = false }
            ) {
                exercises.forEachIndexed { index, exercise ->
                    DropdownMenuItem(
                        text = { Text(exercise.name) },
                        onClick = {
                            onSelectExercise(index)
                            exerciseMenuExpanded = false
                        }
                    )
                }
            }
        }

        // Difficulty picker
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Difficulty:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.weight(1f)
            ) {
                DifficultyPreset.entries.forEachIndexed { index, preset ->
                    SegmentedButton(
                        selected = selectedPreset == preset,
                        onClick = { onSelectPreset(preset) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = DifficultyPreset.entries.size
                        ),
                        label = {
                            Text(
                                text = when (preset) {
                                    DifficultyPreset.LENIENT -> "Lenient"
                                    DifficultyPreset.BALANCED -> "Balanced"
                                    DifficultyPreset.STRICT -> "Strict"
                                },
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
        }

        // Note duration picker
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Note Duration:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.weight(1f)
            ) {
                availableDurations.forEachIndexed { index, duration ->
                    SegmentedButton(
                        selected = noteDurationMs == duration,
                        onClick = { onSelectDuration(duration) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = availableDurations.size
                        ),
                        label = {
                            Text(
                                text = "${duration / 1000.0}s",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PatternDisplaySection(
    midiNotes: List<Int>,
    getNoteResult: (Int) -> NoteEvalResult?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Notes to Sing:",
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
                itemsIndexed(midiNotes) { index, midiNote ->
                    NoteChip(
                        midiNote = midiNote,
                        result = getNoteResult(index)
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteChip(
    midiNote: Int,
    result: NoteEvalResult?
) {
    val noteName = getMidiNoteName(midiNote)

    val backgroundColor = when {
        result != null && result.score >= 0.8f -> Color(0xFF4CAF50) // Green
        result != null && result.score >= 0.5f -> Color(0xFFFF9800) // Orange
        result != null -> Color(0xFFF44336) // Red
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (result != null) Color.White else MaterialTheme.colorScheme.onSurface

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = noteName,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
            if (result != null) {
                Text(
                    text = "${result.scorePercent}%",
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
                        enabled = !isEvaluating
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
private fun ResultsSection(result: ExerciseEvalResult) {
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

        // Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatBox(
                title = "Notes",
                value = "${result.noteCount}",
                modifier = Modifier.weight(1f)
            )
            StatBox(
                title = "Passing",
                value = "${result.passingNotes}/${result.noteCount}",
                modifier = Modifier.weight(1f)
            )
            StatBox(
                title = "Pass Rate",
                value = "${(result.passingRatio * 100).toInt()}%",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun OverallScoreCard(result: ExerciseEvalResult) {
    val backgroundColor = when {
        result.score >= 0.8f -> Color(0xFF4CAF50)
        result.score >= 0.6f -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }

    val feedbackMessage = when {
        result.score >= 0.9f -> "Excellent! Perfect performance."
        result.score >= 0.8f -> "Great job! Almost perfect."
        result.score >= 0.7f -> "Good job! Keep practicing."
        result.score >= 0.5f -> "Not bad. Focus on pitch accuracy."
        else -> "Keep practicing! Match each note carefully."
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
                text = "${result.scorePercent}%",
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
private fun StatBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Utility functions

private fun formatTime(seconds: Float): String {
    val totalSeconds = seconds.toInt()
    val mins = totalSeconds / 60
    val secs = totalSeconds % 60
    val hundredths = ((seconds - totalSeconds) * 100).toInt()
    return "%d:%02d.%02d".format(mins, secs, hundredths)
}

private fun getMidiNoteName(midi: Int): String {
    val noteNames = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    val octave = (midi / 12) - 1
    val note = midi % 12
    return "${noteNames[note]}$octave"
}
