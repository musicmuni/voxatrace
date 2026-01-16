package com.musicmuni.calibra.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.musicmuni.vozos.calibra.CalibraPitch
import com.musicmuni.vozos.calibra.CalibraSegmentEvaluator
import com.musicmuni.vozos.sonix.SonixDecoder
import com.musicmuni.vozos.sonix.SonixPlayer
import com.musicmuni.vozos.sonix.SonixRecorder
import com.musicmuni.vozos.sonix.SonixResampler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream

enum class SingafterPhase {
    IDLE,
    LISTENING,
    SINGING,
    EVALUATED
}

/// Model for singafter segment with type and related_seg from trans file
@Serializable
data class SingafterSegment(
    val id: Int,
    val type: String = "",
    val lyrics: String,
    @SerialName("related_seg") val relatedSeg: Int = -1,
    @SerialName("time_stamp") val timeStamp: List<Double>
) {
    val startTime: Double get() = timeStamp.getOrNull(0) ?: 0.0
    val endTime: Double get() = timeStamp.getOrNull(1) ?: 0.0
}

/// Phrase pair for singafter practice (teacher + student segments)
data class PhrasePair(
    val index: Int,
    val lyrics: String,
    val teacherStartTime: Double,
    val teacherEndTime: Double,
    val studentStartTime: Double,
    val studentEndTime: Double,
    val teacherId: Int  // For evaluator reference
)

/**
 * Singafter Practice Section - for lessons like Chalan where teacher sings first, then student repeats.
 *
 * Demonstrates:
 * - `CalibraSegmentEvaluator` for call-and-response singing evaluation
 * - `SonixRecorder` for audio capture
 *
 * Flow:
 * 1. Load lesson assets (.trans with teacher/student pairs, .mp3)
 * 2. User selects a phrase pair
 * 3. Play teacher audio first
 * 4. When teacher segment ends, start recording student
 * 5. Capture user's pitch during student segment
 * 6. Evaluate and show score when student segment ends
 */
@Composable
fun SingafterPracticeSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Lesson state
    var lessonLoaded by remember { mutableStateOf(false) }
    var phrasePairs by remember { mutableStateOf<List<PhrasePair>>(emptyList()) }
    var currentPairIndex by remember { mutableStateOf(0) }
    val lessonName = "Chalan"

    // Playback state (using SonixPlayer)
    var player by remember { mutableStateOf<SonixPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentTimeMs by remember { mutableLongStateOf(0L) }

    // Recording state
    var isRecording by remember { mutableStateOf(false) }
    var recordingLevel by remember { mutableFloatStateOf(0f) }

    // Sonix recorder
    var recorder by remember { mutableStateOf<SonixRecorder?>(null) }

    // Calibra public API
    var evaluator by remember { mutableStateOf<CalibraSegmentEvaluator?>(null) }
    var pitchDetector by remember { mutableStateOf<CalibraPitch?>(null) }

    var currentPitch by remember { mutableFloatStateOf(0f) }
    var currentNote by remember { mutableStateOf("-") }

    // Evaluation state
    var segmentScore by remember { mutableFloatStateOf(0f) }
    var practicePhase by remember { mutableStateOf(SingafterPhase.IDLE) }
    var status by remember { mutableStateOf("Ready") }

    // Timer for checking phase transitions
    var teacherEndTimeMs by remember { mutableLongStateOf(Long.MAX_VALUE) }
    var studentEndTimeMs by remember { mutableLongStateOf(Long.MAX_VALUE) }

    val json = remember { Json { ignoreUnknownKeys = true } }

    // Copy asset to cache and return path
    suspend fun copyAssetToCache(assetName: String): String {
        return withContext(Dispatchers.IO) {
            val cacheFile = File(context.cacheDir, assetName)
            if (!cacheFile.exists()) {
                context.assets.open(assetName).use { input ->
                    FileOutputStream(cacheFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }
            cacheFile.absolutePath
        }
    }

    fun cleanup() {
        player?.release()
        player = null
        recorder?.release()
        recorder = null

        // Clean up Calibra objects
        pitchDetector?.release()
        pitchDetector = null
        evaluator?.release()
        evaluator = null
    }

    fun loadLesson() {
        scope.launch {
            status = "Loading lesson..."
            try {
                // Create pitch detector using Calibra public API (with processing for smoothing + octave correction)
                pitchDetector = CalibraPitch.create(enableProcessing = true)

                // Load and parse trans file from assets
                val transContent = withContext(Dispatchers.IO) {
                    context.assets.open("$lessonName.trans").bufferedReader().readText()
                }
                val segments = json.decodeFromString<List<SingafterSegment>>(transContent)

                // Group into phrase pairs (teacher followed by related student)
                val teacherSegments = segments.filter { it.type == "teacher_vocal" }
                phrasePairs = teacherSegments.mapNotNull { teacher ->
                    val student = segments.find { it.id == teacher.relatedSeg && it.type == "student_vocal" }
                    if (student != null) {
                        PhrasePair(
                            index = teacher.id,
                            lyrics = teacher.lyrics,
                            teacherStartTime = teacher.startTime,
                            teacherEndTime = teacher.endTime,
                            studentStartTime = student.startTime,
                            studentEndTime = student.endTime,
                            teacherId = teacher.id
                        )
                    } else null
                }

                // Copy audio to cache and create player (m4a format)
                val audioPath = copyAssetToCache("$lessonName.m4a")
                player?.release()
                player = SonixPlayer.create(audioPath)

                // Create recorder
                val tempPath = "${context.cacheDir}/singafter_temp.m4a"
                recorder = SonixRecorder.create(tempPath, "m4a", "voice")

                // Decode reference audio using Sonix and create evaluator
                val audioData = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(audioPath)
                }
                if (audioData != null) {
                    // Resample reference audio to 16kHz (same rate used for student audio)
                    val refSamples16k = SonixResampler.resample(
                        samples = audioData.floatSamples,
                        fromRate = audioData.sampleRate,
                        toRate = 16000
                    )
                    // Create evaluator from audio reference at 16kHz
                    evaluator = CalibraSegmentEvaluator.createFromAudio(
                        refSamplesMono = refSamples16k,
                        sampleRate = 16000
                    )
                } else {
                    status = "Failed to decode reference audio"
                    return@launch
                }

                lessonLoaded = true
                currentPairIndex = 0
                status = "Lesson loaded: ${phrasePairs.size} phrase pairs"
            } catch (e: Exception) {
                status = "Error: ${e.message}"
                e.printStackTrace()
            }
        }
    }

    fun startPractice() {
        if (!lessonLoaded || phrasePairs.isEmpty() || player == null) return

        scope.launch {
            val pair = phrasePairs[currentPairIndex]
            practicePhase = SingafterPhase.LISTENING
            segmentScore = 0f
            status = "Listen to the teacher..."

            // Seek player to teacher segment start and play (convert seconds to milliseconds)
            val startMs = (pair.teacherStartTime * 1000).toLong()
            teacherEndTimeMs = (pair.teacherEndTime * 1000).toLong()
            studentEndTimeMs = (pair.studentEndTime * 1000).toLong()
            player?.seek(startMs)
            player?.play()
            isPlaying = true

            // Collect playback time
            launch {
                player?.currentTime?.collect { timeMs ->
                    currentTimeMs = timeMs
                }
            }
        }
    }

    // Auto-transition from listening to singing phase
    LaunchedEffect(currentTimeMs, practicePhase) {
        if (practicePhase == SingafterPhase.LISTENING && currentTimeMs >= teacherEndTimeMs) {
            val pair = phrasePairs[currentPairIndex]
            practicePhase = SingafterPhase.SINGING
            status = "Your turn! Sing now..."

            // Precompute reference features using Calibra public API (use teacherId for segment reference)
            evaluator?.precomputeRef(
                segmentIndex = pair.teacherId,
                refStart = pair.teacherStartTime.toFloat(),
                refEnd = pair.teacherEndTime.toFloat()
            )

            // Start capturing using Calibra public API
            evaluator?.startCapture(segmentIndex = pair.teacherId)

            // Start recording
            recorder?.start()
            isRecording = true

            teacherEndTimeMs = Long.MAX_VALUE // Prevent re-triggering
        }
    }

    // Collect audio during singing phase
    LaunchedEffect(isRecording) {
        if (isRecording) {
            launch {
                val hwRate = 48000 // Android hardware sample rate

                recorder?.audioBuffers?.collect { buffer ->
                    if (!isRecording) return@collect

                    // Prepare audio for Calibra (resample to 16kHz)
                    val floatBuffer = FloatArray(buffer.sampleCount)
                    buffer.fillFloatSamples(floatBuffer)
                    val samples = SonixResampler.resample(
                        samples = floatBuffer,
                        fromRate = hwRate,
                        toRate = 16000
                    )

                    // Use pitch detector directly for real-time pitch display
                    val result = pitchDetector?.detectWithConfidence(samples = samples)
                    val detectedPitch = result?.pitchHz ?: -1.0f
                    val calculatedRms = pitchDetector?.getAmplitude(samples = samples) ?: 0.0f

                    // Also feed to evaluator for scoring
                    evaluator?.processFrame(samples = samples)

                    currentPitch = detectedPitch
                    recordingLevel = calculatedRms
                    currentNote = MusicUtils.getMidiNoteName(detectedPitch)
                }
            }
        }
    }

    // Auto-stop when student segment ends
    LaunchedEffect(currentTimeMs, practicePhase, isRecording) {
        if (practicePhase == SingafterPhase.SINGING && currentTimeMs >= studentEndTimeMs && isPlaying) {
            val pair = phrasePairs[currentPairIndex]

            // Stop playback and recording
            player?.pause()
            recorder?.stop()
            isPlaying = false
            isRecording = false

            // Evaluate using Calibra public API (use teacherId for segment reference)
            evaluator?.stopCapture(segmentIndex = pair.teacherId)

            segmentScore = evaluator?.evaluate(
                segmentIndex = pair.teacherId,
                stdStart = pair.teacherStartTime.toFloat(),
                stdEnd = pair.teacherEndTime.toFloat()
            ) ?: 0f

            practicePhase = SingafterPhase.EVALUATED
            status = "Score: ${(segmentScore * 100).toInt()}%"
            studentEndTimeMs = Long.MAX_VALUE
        }
    }

    fun forceStop() {
        scope.launch {
            player?.pause()
            recorder?.stop()
            isPlaying = false
            isRecording = false

            if (practicePhase == SingafterPhase.SINGING) {
                val pair = phrasePairs[currentPairIndex]
                evaluator?.stopCapture(segmentIndex = pair.teacherId)
            }

            practicePhase = SingafterPhase.IDLE
            status = "Stopped"
        }
    }

    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            cleanup()
        }
    }

    // UI
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Singafter Practice (Chalan)",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = status,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (!lessonLoaded) {
            Button(onClick = { loadLesson() }) {
                Text("Load Lesson")
            }
        } else {
            // Phrase pair selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Phrase ${currentPairIndex + 1} of ${phrasePairs.size}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { if (currentPairIndex > 0) currentPairIndex-- },
                        enabled = currentPairIndex > 0 && practicePhase == SingafterPhase.IDLE
                    ) {
                        Text("<")
                    }
                    Button(
                        onClick = { if (currentPairIndex < phrasePairs.size - 1) currentPairIndex++ },
                        enabled = currentPairIndex < phrasePairs.size - 1 && practicePhase == SingafterPhase.IDLE
                    ) {
                        Text(">")
                    }
                }
            }

            // Current phrase lyrics
            if (phrasePairs.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = phrasePairs[currentPairIndex].lyrics,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Phase indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PhaseIndicator(
                    label = "Listen",
                    isActive = practicePhase == SingafterPhase.LISTENING,
                    isComplete = practicePhase.ordinal > SingafterPhase.LISTENING.ordinal
                )
                Text("\u2192")
                PhaseIndicator(
                    label = "Sing",
                    isActive = practicePhase == SingafterPhase.SINGING,
                    isComplete = practicePhase == SingafterPhase.EVALUATED
                )
                Text("\u2192")
                PhaseIndicator(
                    label = "Score",
                    isActive = practicePhase == SingafterPhase.EVALUATED,
                    isComplete = false
                )
            }

            // Practice controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (practicePhase) {
                    SingafterPhase.IDLE, SingafterPhase.EVALUATED -> {
                        Button(
                            onClick = { startPractice() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Start Practice")
                        }
                    }
                    SingafterPhase.LISTENING, SingafterPhase.SINGING -> {
                        Button(
                            onClick = { forceStop() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Stop")
                        }
                    }
                }
            }

            // Real-time feedback during singing phase
            if (practicePhase == SingafterPhase.SINGING) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Pitch: ${"%.1f".format(currentPitch)} Hz")
                        Text(
                            text = currentNote,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Level:", style = MaterialTheme.typography.bodySmall)
                        LinearProgressIndicator(
                            progress = { recordingLevel.coerceIn(0f, 1f) },
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                        )
                    }
                }
            }

            // Score display after evaluation
            if (practicePhase == SingafterPhase.EVALUATED) {
                ScoreCard(score = segmentScore)
            }
        }
    }
}

@Composable
private fun PhaseIndicator(label: String, isActive: Boolean, isComplete: Boolean) {
    val backgroundColor = when {
        isActive -> MaterialTheme.colorScheme.primary
        isComplete -> Color(0xFF4CAF50)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(backgroundColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isComplete) {
                Text("\u2713", color = Color.White)
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
