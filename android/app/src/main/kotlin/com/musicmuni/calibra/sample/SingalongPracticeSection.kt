package com.musicmuni.calibra.sample

import androidx.compose.foundation.layout.*
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
import com.musicmuni.vozos.sonix.util.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Singalong Practice Section - for lessons like Alankaar where user sings along with teacher.
 *
 * Demonstrates:
 * - `CalibraSegmentEvaluator` for sing-along evaluation
 * - `CalibraPitch` for real-time pitch detection
 * - `SonixRecorder` for audio capture
 *
 * Flow:
 * 1. Load lesson assets (.trans, .mp3)
 * 2. User selects a segment
 * 3. Play reference audio while user sings along
 * 4. Capture user's pitch in real-time
 * 5. Evaluate and show score after segment ends
 */
@Composable
fun SingalongPracticeSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Lesson state
    var lessonLoaded by remember { mutableStateOf(false) }
    var segments by remember { mutableStateOf<List<SegmentInfo>>(emptyList()) }
    var currentSegmentIndex by remember { mutableStateOf(0) }
    val lessonName = "Alankaar 01"

    // Playback state
    var player by remember { mutableStateOf<SonixPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentTimeMs by remember { mutableLongStateOf(0L) }

    // Recording state
    var recorder by remember { mutableStateOf<SonixRecorder?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var recordingLevel by remember { mutableFloatStateOf(0f) }

    // Calibra public API
    var evaluator by remember { mutableStateOf<CalibraSegmentEvaluator?>(null) }
    var pitchDetector by remember { mutableStateOf<CalibraPitch?>(null) }

    var currentPitch by remember { mutableFloatStateOf(0f) }
    var currentNote by remember { mutableStateOf("-") }

    // Evaluation state
    var segmentScore by remember { mutableFloatStateOf(0f) }
    var practiceState by remember { mutableStateOf(PracticeState.IDLE) }
    var status by remember { mutableStateOf("Ready") }
    var segmentEndTimeMs by remember { mutableLongStateOf(Long.MAX_VALUE) }

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

    // Load lesson
    fun loadLesson() {
        scope.launch {
            status = "Loading lesson..."
            try {
                // Create pitch detector using Calibra public API (with processing for smoothing + octave correction)
                pitchDetector?.release()
                pitchDetector = CalibraPitch.create(enableProcessing = true)

                // Parse trans file
                val transContent = withContext(Dispatchers.IO) {
                    context.assets.open("$lessonName.trans").bufferedReader().readText()
                }
                val transData = Parser.parseTransString(transContent)
                if (transData == null) {
                    status = "Failed to parse trans file"
                    return@launch
                }

                // Convert to SegmentInfo
                segments = transData.segments.mapIndexed { index, seg ->
                    SegmentInfo(
                        index = index,
                        lyrics = seg.lyrics,
                        startTime = seg.startTime,
                        endTime = seg.endTime
                    )
                }

                // Copy audio to cache and create player
                val audioPath = copyAssetToCache("$lessonName.mp3")
                player?.release()
                player = SonixPlayer.create(audioPath)

                // Create recorder for capturing user audio
                val recordPath = "${context.cacheDir}/user_recording.m4a"
                recorder?.release()
                recorder = SonixRecorder.create(recordPath, "m4a", "voice")

                // Decode reference audio using Sonix and create evaluator
                val audioData = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(audioPath)
                }
                if (audioData != null) {
                    // Resample reference audio to 16kHz
                    val refSamples16k = SonixResampler.resample(
                        samples = audioData.floatSamples,
                        fromRate = audioData.sampleRate,
                        toRate = 16000
                    )
                    // Create evaluator from audio reference at 16kHz
                    evaluator?.release()
                    evaluator = CalibraSegmentEvaluator.createFromAudio(
                        refSamplesMono = refSamples16k,
                        sampleRate = 16000
                    )
                } else {
                    status = "Failed to decode reference audio"
                    return@launch
                }

                lessonLoaded = true
                currentSegmentIndex = 0
                status = "Lesson loaded: ${segments.size} segments"
            } catch (e: Exception) {
                status = "Error: ${e.message}"
            }
        }
    }

    // Start practicing a segment
    fun startPractice() {
        if (!lessonLoaded || segments.isEmpty()) return

        scope.launch {
            val segment = segments[currentSegmentIndex]
            practiceState = PracticeState.PRACTICING
            segmentScore = 0f
            status = "Practicing segment ${currentSegmentIndex + 1}..."

            // Precompute reference features for this segment using Calibra public API
            evaluator?.precomputeRef(
                segmentIndex = currentSegmentIndex,
                refStart = segment.startTime.toFloat(),
                refEnd = segment.endTime.toFloat()
            )

            // Seek player to segment start
            player?.seek((segment.startTime * 1000).toLong())
            segmentEndTimeMs = (segment.endTime * 1000).toLong()

            // Start capturing using Calibra public API
            evaluator?.startCapture(segmentIndex = currentSegmentIndex)

            // Start recording and playback
            recorder?.start()
            player?.play()
            isPlaying = true
            isRecording = true

            // Collect audio buffers for pitch detection
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

            // Collect playback time
            launch {
                player?.currentTime?.collect { timeMs ->
                    currentTimeMs = timeMs
                }
            }
        }
    }

    // Auto-stop when segment ends
    LaunchedEffect(currentTimeMs, practiceState) {
        if (practiceState == PracticeState.PRACTICING && currentTimeMs >= segmentEndTimeMs && isPlaying) {
            val segment = segments[currentSegmentIndex]

            // Stop playback and recording
            player?.pause()
            recorder?.stop()
            isPlaying = false
            isRecording = false

            // Evaluate using Calibra public API
            evaluator?.stopCapture(segmentIndex = currentSegmentIndex)
            segmentScore = evaluator?.evaluate(
                segmentIndex = currentSegmentIndex,
                stdStart = segment.startTime.toFloat(),
                stdEnd = segment.endTime.toFloat()
            ) ?: 0f

            practiceState = PracticeState.EVALUATED
            status = "Segment ${currentSegmentIndex + 1} score: ${(segmentScore * 100).toInt()}%"
            segmentEndTimeMs = Long.MAX_VALUE
        }
    }

    // Stop practice and evaluate (manual stop)
    fun stopPractice() {
        if (practiceState != PracticeState.PRACTICING) return

        scope.launch {
            val segment = segments[currentSegmentIndex]

            // Stop playback and recording
            player?.pause()
            recorder?.stop()
            isPlaying = false
            isRecording = false

            // Evaluate using Calibra public API
            evaluator?.stopCapture(segmentIndex = currentSegmentIndex)
            segmentScore = evaluator?.evaluate(
                segmentIndex = currentSegmentIndex,
                stdStart = segment.startTime.toFloat(),
                stdEnd = segment.endTime.toFloat()
            ) ?: 0f

            practiceState = PracticeState.EVALUATED
            status = "Segment ${currentSegmentIndex + 1} score: ${(segmentScore * 100).toInt()}%"
        }
    }

    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            player?.release()
            recorder?.release()
            pitchDetector?.release()
            evaluator?.release()
        }
    }

    // UI
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Singalong Practice (Alankaar)",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = status,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Load button
        if (!lessonLoaded) {
            Button(onClick = { loadLesson() }) {
                Text("Load Lesson")
            }
        } else {
            // Segment selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Segment ${currentSegmentIndex + 1} of ${segments.size}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { if (currentSegmentIndex > 0) currentSegmentIndex-- },
                        enabled = currentSegmentIndex > 0 && practiceState != PracticeState.PRACTICING
                    ) {
                        Text("<")
                    }
                    Button(
                        onClick = { if (currentSegmentIndex < segments.size - 1) currentSegmentIndex++ },
                        enabled = currentSegmentIndex < segments.size - 1 && practiceState != PracticeState.PRACTICING
                    ) {
                        Text(">")
                    }
                }
            }

            // Current segment lyrics
            if (segments.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = segments[currentSegmentIndex].lyrics,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Practice controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (practiceState) {
                    PracticeState.IDLE, PracticeState.EVALUATED -> {
                        Button(
                            onClick = { startPractice() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Start Practice")
                        }
                    }
                    PracticeState.PRACTICING -> {
                        Button(
                            onClick = { stopPractice() },
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

            // Real-time feedback during practice
            if (practiceState == PracticeState.PRACTICING) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Pitch display
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

                    // Level meter
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
            if (practiceState == PracticeState.EVALUATED) {
                ScoreCard(score = segmentScore)
            }
        }
    }
}

data class SegmentInfo(
    val index: Int,
    val lyrics: String,
    val startTime: Double,
    val endTime: Double
)

enum class PracticeState {
    IDLE,
    PRACTICING,
    EVALUATED
}

@Composable
fun ScoreCard(score: Float) {
    val backgroundColor = when {
        score >= 0.8f -> Color(0xFF4CAF50)
        score >= 0.6f -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }
    val feedback = when {
        score >= 0.8f -> "Excellent!"
        score >= 0.6f -> "Good job!"
        else -> "Keep practicing!"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(score * 100).toInt()}%",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Text(
                text = feedback,
                color = Color.White
            )
        }
    }
}

object MusicUtils {
    fun getMidiNoteName(pitchHz: Float): String {
        if (pitchHz <= 0) return "-"
        val midiNote = (12 * kotlin.math.log2(pitchHz / 440.0) + 69).toInt()
        val noteNames = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
        val noteName = noteNames[midiNote % 12]
        val octave = (midiNote / 12) - 1
        return "$noteName$octave"
    }
}
