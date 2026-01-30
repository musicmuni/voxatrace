package com.musicmuni.voxatrace.demo.sections.speakingpitch.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musicmuni.voxatrace.calibra.CalibraMusic
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.CalibraSpeakingPitch
import com.musicmuni.voxatrace.calibra.model.PitchAlgorithm
import com.musicmuni.voxatrace.calibra.model.PitchDetectorConfig
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import com.musicmuni.voxatrace.sonix.SonixResampler
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

private const val FEMALE_THRESHOLD_HZ = 174.61f // F3
private const val RMS_THRESHOLD = 0.02f
private const val COUNTDOWN_SECONDS = 4

enum class Gender { MALE, FEMALE }

/**
 * Detection state for speaking pitch.
 */
enum class SpeakingPitchDetectionState {
    IDLE,
    LISTENING,
    COUNTDOWN,
    PROCESSING,
    COMPLETE
}

/**
 * Speaking pitch result data.
 */
data class SpeakingPitchResult(
    val frequencyHz: Float,
    val noteLabel: String,
    val gender: Gender? = null
)

/**
 * Speaking Pitch & Gender Detector View.
 *
 * Uses CalibraSpeakingPitch.detectFromAudio() - a stateless one-shot detection.
 * Automatically detects when user starts speaking, counts down, then analyzes.
 */
@Composable
fun SpeakingPitchView() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var recorder by remember { mutableStateOf<SonixRecorder?>(null) }
    var pitchDetector by remember { mutableStateOf<CalibraPitch.Detector?>(null) }
    var recordingJob by remember { mutableStateOf<Job?>(null) }
    var countdownJob by remember { mutableStateOf<Job?>(null) }
    var collectedAudio by remember { mutableStateOf(mutableListOf<Float>()) }

    var detectionState by remember { mutableStateOf(SpeakingPitchDetectionState.IDLE) }
    var countdownSeconds by remember { mutableIntStateOf(COUNTDOWN_SECONDS) }
    var currentLevel by remember { mutableFloatStateOf(0f) }
    var result by remember { mutableStateOf<SpeakingPitchResult?>(null) }

    // Offline analysis state
    var offlineResult by remember { mutableStateOf<SpeakingPitchResult?>(null) }
    var isAnalyzingOffline by remember { mutableStateOf(false) }

    val status = when (detectionState) {
        SpeakingPitchDetectionState.IDLE -> "Speak naturally to detect your speaking pitch"
        SpeakingPitchDetectionState.LISTENING -> "Say something..."
        SpeakingPitchDetectionState.COUNTDOWN -> "Keep speaking..."
        SpeakingPitchDetectionState.PROCESSING -> "Processing..."
        SpeakingPitchDetectionState.COMPLETE -> if (result != null) "Detection complete!" else "Could not detect. Try again."
    }

    fun processAudio() {
        if (collectedAudio.isEmpty()) {
            detectionState = SpeakingPitchDetectionState.COMPLETE
            return
        }

        scope.launch {
            val audioArray = collectedAudio.toFloatArray()
            val detectedHz = withContext(Dispatchers.Default) {
                CalibraSpeakingPitch.detectFromAudio(audioArray)
            }

            if (detectedHz > 0) {
                val noteLabel = CalibraMusic.hzToNoteLabel(detectedHz)
                val gender = if (detectedHz >= FEMALE_THRESHOLD_HZ) Gender.FEMALE else Gender.MALE
                result = SpeakingPitchResult(
                    frequencyHz = detectedHz,
                    noteLabel = noteLabel,
                    gender = gender
                )
            }

            detectionState = SpeakingPitchDetectionState.COMPLETE
        }
    }

    fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = scope.launch {
            for (i in COUNTDOWN_SECONDS downTo 1) {
                countdownSeconds = i
                delay(1000)
                if (detectionState != SpeakingPitchDetectionState.COUNTDOWN) {
                    return@launch
                }
            }

            // Countdown finished
            detectionState = SpeakingPitchDetectionState.PROCESSING
            recorder?.stop()
            processAudio()
        }
    }

    fun startDetection() {
        // Setup pitch detector if needed
        if (pitchDetector == null) {
            val config = PitchDetectorConfig.Builder()
                .algorithm(PitchAlgorithm.YIN)
                .build()
            pitchDetector = CalibraPitch.createDetector(config)
        }

        // Reset state
        collectedAudio = mutableListOf()
        result = null
        currentLevel = 0f
        countdownSeconds = COUNTDOWN_SECONDS
        detectionState = SpeakingPitchDetectionState.LISTENING

        recorder = SonixRecorder.create("${context.cacheDir}/shruti_temp.m4a", SonixRecorderConfig.VOICE)
        recorder?.start()

        recordingJob = scope.launch {
            recorder?.audioBuffers?.collect { buffer ->
                // VOICE preset records at 16kHz; CalibraPitch handles resampling internally (ADR-017)
                val samples = buffer.samples
                val calculatedRms = pitchDetector?.getAmplitude(samples, 16000) ?: 0f
                currentLevel = calculatedRms

                when (detectionState) {
                    SpeakingPitchDetectionState.LISTENING -> {
                        if (calculatedRms > RMS_THRESHOLD) {
                            detectionState = SpeakingPitchDetectionState.COUNTDOWN
                            startCountdown()
                        }
                    }
                    SpeakingPitchDetectionState.COUNTDOWN -> {
                        collectedAudio.addAll(samples.toList())
                    }
                    else -> { }
                }
            }
        }
    }

    fun stopDetection() {
        recordingJob?.cancel()
        countdownJob?.cancel()
        recorder?.stop()
        detectionState = SpeakingPitchDetectionState.IDLE
    }

    fun copyAssetToFile(assetName: String): File {
        val file = File(context.cacheDir, assetName)
        if (!file.exists()) {
            context.assets.open(assetName).use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
        }
        return file
    }

    fun analyzeOffline() {
        isAnalyzingOffline = true
        offlineResult = null

        scope.launch {
            try {
                val audioFile = withContext(Dispatchers.IO) {
                    copyAssetToFile("Alankaar 01_voice.m4a")
                }

                val audioData = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(audioFile.absolutePath)
                }

                if (audioData == null) {
                    Napier.e("Failed to decode audio file")
                    isAnalyzingOffline = false
                    return@launch
                }

                val samples16k = withContext(Dispatchers.IO) {
                    SonixResampler.resample(
                        samples = audioData.samples,
                        fromRate = audioData.sampleRate,
                        toRate = 16000
                    )
                }

                val detectedHz = withContext(Dispatchers.Default) {
                    CalibraSpeakingPitch.detectFromAudio(samples16k)
                }

                if (detectedHz > 0) {
                    val noteLabel = CalibraMusic.hzToNoteLabel(detectedHz)
                    val gender = if (detectedHz >= FEMALE_THRESHOLD_HZ) Gender.FEMALE else Gender.MALE
                    offlineResult = SpeakingPitchResult(
                        frequencyHz = detectedHz,
                        noteLabel = noteLabel,
                        gender = gender
                    )
                }
            } catch (e: Exception) {
                Napier.e("Offline analysis failed", e)
            } finally {
                isAnalyzingOffline = false
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            recordingJob?.cancel()
            countdownJob?.cancel()
            recorder?.release()
            pitchDetector?.release()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Speaking Pitch & Gender Detector", style = MaterialTheme.typography.titleMedium)

        Text(
            text = status,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Main display card
        MainDisplayCard(
            detectionState = detectionState,
            countdownSeconds = countdownSeconds,
            result = result
        )

        // Level meter
        if (detectionState == SpeakingPitchDetectionState.LISTENING ||
            detectionState == SpeakingPitchDetectionState.COUNTDOWN) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Level:", style = MaterialTheme.typography.bodySmall)
                LinearProgressIndicator(
                    progress = { currentLevel.coerceIn(0f, 1f) },
                    modifier = Modifier.weight(1f).height(8.dp),
                    color = if (currentLevel > RMS_THRESHOLD) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                )
            }
        }

        // Control buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (detectionState) {
                SpeakingPitchDetectionState.IDLE -> {
                    Button(
                        onClick = { startDetection() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Start Detection")
                    }
                }
                SpeakingPitchDetectionState.COMPLETE -> {
                    Button(
                        onClick = { startDetection() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Try Again")
                    }
                }
                else -> {
                    Button(
                        onClick = { stopDetection() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }

        // Info text
        Text(
            text = "Speak naturally for a few seconds. Your natural speaking pitch will be detected.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Offline Analysis Section
        OfflineAnalysisSection(
            onAnalyze = { analyzeOffline() },
            isAnalyzing = isAnalyzingOffline,
            result = offlineResult
        )
    }
}

@Composable
private fun MainDisplayCard(
    detectionState: SpeakingPitchDetectionState,
    countdownSeconds: Int,
    result: SpeakingPitchResult?
) {
    val backgroundColor = when (detectionState) {
        SpeakingPitchDetectionState.COUNTDOWN -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (detectionState) {
                SpeakingPitchDetectionState.LISTENING -> {
                    Text(
                        text = "Listening...",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                SpeakingPitchDetectionState.COUNTDOWN -> {
                    Text(
                        text = "$countdownSeconds",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Keep speaking",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                SpeakingPitchDetectionState.PROCESSING -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Analyzing...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                SpeakingPitchDetectionState.COMPLETE -> {
                    if (result != null) {
                        Text(
                            text = result.noteLabel,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${"%.1f".format(result.frequencyHz)} Hz",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        result.gender?.let { gender ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Inferred Voice Type:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (gender == Gender.FEMALE) "FEMALE" else "MALE",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(
                                            if (gender == Gender.FEMALE) Color(0xFFE91E63) else Color(0xFF2196F3),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "No pitch detected",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                SpeakingPitchDetectionState.IDLE -> {
                    Text(
                        text = "Ready",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun OfflineAnalysisSection(
    onAnalyze: () -> Unit,
    isAnalyzing: Boolean,
    result: SpeakingPitchResult?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Offline Analysis",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Analyze speaking pitch from audio file",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            onClick = onAnalyze,
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

        result?.let { r ->
            OfflineResultCard(result = r)
        }

        ApiInfoCard()
    }
}

@Composable
private fun OfflineResultCard(result: SpeakingPitchResult) {
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
                text = "Offline Result",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Detected Note",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = result.noteLabel,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Frequency",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${"%.1f".format(result.frequencyHz)} Hz",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                result.gender?.let { gender ->
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Voice Type",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (gender == Gender.FEMALE) "FEMALE" else "MALE",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    if (gender == Gender.FEMALE) Color(0xFFE91E63) else Color(0xFF2196F3),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
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
                text = "- SonixDecoder.decode() - Load audio from file",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "- SonixResampler.resample() - Resample to 16kHz",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "- CalibraSpeakingPitch.detectFromAudio() - Detect speaking pitch",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
