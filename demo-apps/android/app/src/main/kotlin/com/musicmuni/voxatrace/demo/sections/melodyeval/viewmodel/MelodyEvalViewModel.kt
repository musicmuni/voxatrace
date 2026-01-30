package com.musicmuni.voxatrace.demo.sections.melodyeval.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraMelodyEval
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.model.LessonMaterial
import com.musicmuni.voxatrace.calibra.model.Segment
import com.musicmuni.voxatrace.calibra.model.SingingResult
import com.musicmuni.voxatrace.sonix.AudioMode
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.SonixPlayerConfig
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import com.musicmuni.voxatrace.sonix.util.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min
import kotlin.math.sqrt

/**
 * ViewModel for melody evaluation using CalibraMelodyEval with singalong mode.
 *
 * ## VoxaTrace Integration
 * ```kotlin
 * // 1. Load reference (first 4 segments of Alankaar 01)
 * val transData = Parser.parseTransString(transContent)
 * val audioData = SonixDecoder.decode(audioPath)
 * reference = LessonMaterial.fromAudio(samples, sampleRate, segments, keyHz)
 *
 * // 2. Singalong: Play reference + record student simultaneously
 * player = SonixPlayer.create(source = referencePath, audioSession = AudioMode.PLAY_AND_RECORD)
 * recorder = SonixRecorder.create(outputPath, config, audioSession = AudioMode.PLAY_AND_RECORD)
 *
 * // 3. Evaluate
 * val extractor = CalibraPitch.createContourExtractor()
 * val result = CalibraMelodyEval.evaluate(reference, studentMaterial, extractor)
 * ```
 */
class MelodyEvalViewModel : ViewModel() {

    // MARK: - Published State

    private val _referenceLoaded = MutableStateFlow(false)
    val referenceLoaded: StateFlow<Boolean> = _referenceLoaded.asStateFlow()

    private val _segments = MutableStateFlow<List<Segment>>(emptyList())
    val segments: StateFlow<List<Segment>> = _segments.asStateFlow()

    private val _isSingalongActive = MutableStateFlow(false)
    val isSingalongActive: StateFlow<Boolean> = _isSingalongActive.asStateFlow()

    private val _hasRecording = MutableStateFlow(false)
    val hasRecording: StateFlow<Boolean> = _hasRecording.asStateFlow()

    private val _recordingDuration = MutableStateFlow(0f)
    val recordingDuration: StateFlow<Float> = _recordingDuration.asStateFlow()

    private val _recordingLevel = MutableStateFlow(0f)
    val recordingLevel: StateFlow<Float> = _recordingLevel.asStateFlow()

    private val _isEvaluating = MutableStateFlow(false)
    val isEvaluating: StateFlow<Boolean> = _isEvaluating.asStateFlow()

    private val _result = MutableStateFlow<SingingResult?>(null)
    val result: StateFlow<SingingResult?> = _result.asStateFlow()

    private val _status = MutableStateFlow("Tap 'Load Reference' to begin")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _isPreparing = MutableStateFlow(false)
    val isPreparing: StateFlow<Boolean> = _isPreparing.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _currentSegmentIndex = MutableStateFlow(-1)
    val currentSegmentIndex: StateFlow<Int> = _currentSegmentIndex.asStateFlow()

    val referenceName = "Alankaar 01"

    /** Display names for the 4 segments */
    val segmentNames = listOf("SRGM", "PDNS", "SNDP", "MGRS")

    // MARK: - Private State

    private var reference: LessonMaterial? = null
    private var recorder: SonixRecorder? = null
    private var collectedAudio = mutableListOf<Float>()
    private var recordingJob: Job? = null

    // Singalong
    private var referencePlayer: SonixPlayer? = null
    private var playerObserverJob: Job? = null
    private var timeObserverJob: Job? = null
    private var audioFilePath: String? = null
    private var segmentStartTimeMs: Long = 0
    private var segmentEndTimeMs: Long = 0
    private var originalSampleRate: Int = 44100

    // Original segment times (for player position tracking)
    private var originalSegmentTimes: List<Pair<Double, Double>> = emptyList()

    // MARK: - Actions

    fun loadReference(context: Context) {
        _status.value = "Loading reference..."
        _isPreparing.value = true

        viewModelScope.launch {
            try {
                // Load and parse trans file
                val transContent = withContext(Dispatchers.IO) {
                    context.assets.open("$referenceName.trans").bufferedReader().readText()
                }
                val transData = Parser.parseTransString(transContent)
                if (transData == null) {
                    _status.value = "Failed to parse trans file"
                    _isPreparing.value = false
                    return@launch
                }

                // Use first 4 segments: SRGM, PDNS, SNDP, MGRS
                val segmentCount = min(4, transData.segments.size)

                // Store original segment times for player position tracking
                originalSegmentTimes = (0 until segmentCount).map { i ->
                    val seg = transData.segments[i]
                    seg.startTime to seg.endTime
                }

                _segments.value = (0 until segmentCount).map { i ->
                    val seg = transData.segments[i]
                    Segment(
                        index = i,
                        startSeconds = seg.startTime.toFloat(),
                        endSeconds = seg.endTime.toFloat(),
                        lyrics = seg.lyrics
                    )
                }

                // Calculate the time range for the 4 segments
                val startTime = originalSegmentTimes.firstOrNull()?.first ?: 0.0
                val endTime = originalSegmentTimes.lastOrNull()?.second ?: 0.0
                segmentStartTimeMs = (startTime * 1000).toLong()
                segmentEndTimeMs = (endTime * 1000).toLong()

                // Copy audio file
                val audioPath = withContext(Dispatchers.IO) {
                    copyAssetToFile(context, "$referenceName.m4a").absolutePath
                }
                audioFilePath = audioPath

                val audioData = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(audioPath)
                }

                if (audioData == null) {
                    _status.value = "Failed to decode audio"
                    _isPreparing.value = false
                    return@launch
                }

                originalSampleRate = audioData.sampleRate

                // Trim audio for evaluation (we need trimmed audio for CalibraMelodyEval)
                val startSample = maxOf(0, (startTime * audioData.sampleRate).toInt())
                val endSample = minOf(audioData.samples.size, (endTime * audioData.sampleRate).toInt())
                val trimmedSamples = audioData.samples.copyOfRange(startSample, endSample)

                // Create adjusted segments for the trimmed reference (times relative to start of trimmed audio)
                val adjustedSegments = _segments.value.map { seg ->
                    Segment(
                        index = seg.index,
                        startSeconds = maxOf(0f, seg.startSeconds - startTime.toFloat()),
                        endSeconds = maxOf(0f, seg.endSeconds - startTime.toFloat()),
                        lyrics = seg.lyrics
                    )
                }

                reference = LessonMaterial.fromAudio(
                    samples = trimmedSamples,
                    sampleRate = audioData.sampleRate,
                    segments = adjustedSegments,
                    keyHz = 196.0f
                )

                _referenceLoaded.value = true

                // Load player for singalong
                loadPlayerForSingalong()

            } catch (e: Exception) {
                _status.value = "Error: ${e.message}"
                _isPreparing.value = false
            }
        }
    }

    private suspend fun loadPlayerForSingalong() {
        val path = audioFilePath
        if (path == null) {
            _status.value = "Audio file not found"
            _isPreparing.value = false
            return
        }

        playerObserverJob?.cancel()
        timeObserverJob?.cancel()
        referencePlayer?.release()

        try {
            val player = SonixPlayer.create(
                source = path,
                config = SonixPlayerConfig.DEFAULT,
                audioSession = AudioMode.PLAY_AND_RECORD
            )
            referencePlayer = player

            // Observe playback state - auto-stop when playback finishes
            playerObserverJob = viewModelScope.launch {
                player.isPlaying.collect { playing ->
                    if (!playing && _isSingalongActive.value) {
                        stopSingalong()
                    }
                }
            }

            // Monitor time to stop at segment end and track current segment
            timeObserverJob = viewModelScope.launch {
                player.currentTime.collect { timeMs ->
                    if (_isSingalongActive.value) {
                        // Stop at segment end
                        if (timeMs >= segmentEndTimeMs) {
                            stopSingalong()
                        } else {
                            // Update current segment indicator
                            val currentTimeSec = timeMs / 1000.0
                            updateCurrentSegment(currentTimeSec)
                        }
                    }
                }
            }

            _isReady.value = true
            _isPreparing.value = false
            _status.value = "Ready! Tap 'Start Singalong' to sing along."
        } catch (e: Exception) {
            _status.value = "Player error: ${e.message}"
            _isPreparing.value = false
        }
    }

    fun startSingalong(context: Context) {
        if (!_isReady.value) {
            _status.value = "Reference not ready"
            return
        }

        collectedAudio.clear()
        _hasRecording.value = false
        _result.value = null
        _recordingDuration.value = 0f
        _currentSegmentIndex.value = 0
        _status.value = "Sing along: ${segmentNames[0]}"

        // Create recorder for simultaneous playback+recording with echo cancellation
        val tempPath = "${context.cacheDir}/melody_eval_temp.m4a"
        val recorderConfig = SonixRecorderConfig.Builder()
            .preset(SonixRecorderConfig.VOICE)
            .echoCancellation(true)
            .build()
        recorder = SonixRecorder.create(tempPath, recorderConfig, AudioMode.PLAY_AND_RECORD)

        _isSingalongActive.value = true

        // Start collecting audio
        recordingJob = viewModelScope.launch {
            // VOICE preset records at 16kHz (ADR-017)
            var sampleCount = 0

            recorder?.start()

            recorder?.audioBuffers?.collect { buffer ->
                val samples = buffer.samples

                collectedAudio.addAll(samples.toList())
                sampleCount += samples.size

                // Calculate RMS for level meter
                val sum = samples.fold(0f) { acc, s -> acc + s * s }
                val rms = sqrt(sum / samples.size)

                _recordingLevel.value = (rms * 5).coerceAtMost(1f)
                _recordingDuration.value = sampleCount / 16000f
            }
        }

        // Seek to the start of the first segment and play
        referencePlayer?.seek(segmentStartTimeMs)

        // Start player
        referencePlayer?.play()
    }

    private fun updateCurrentSegment(currentTimeSec: Double) {
        // Use original segment times for tracking since we play from original audio file
        for ((index, times) in originalSegmentTimes.withIndex()) {
            if (currentTimeSec >= times.first && currentTimeSec < times.second) {
                if (_currentSegmentIndex.value != index) {
                    _currentSegmentIndex.value = index
                    _status.value = "Sing along: ${segmentNames[index]}"
                }
                return
            }
        }
    }

    fun stopSingalong() {
        recordingJob?.cancel()
        recorder?.stop()
        referencePlayer?.stop()
        _isSingalongActive.value = false
        _currentSegmentIndex.value = -1

        if (collectedAudio.isNotEmpty()) {
            _hasRecording.value = true
            _status.value = "Recording complete. Evaluating..."
            evaluate()
        } else {
            _status.value = "No audio recorded. Try again."
        }
    }

    private fun evaluate() {
        val ref = reference
        if (ref == null || collectedAudio.isEmpty()) {
            _status.value = "Missing reference or recording"
            return
        }

        _isEvaluating.value = true
        _status.value = "Evaluating..."

        viewModelScope.launch {
            val studentAudio = collectedAudio.toFloatArray()

            val studentMaterial = LessonMaterial.fromAudio(
                samples = studentAudio,
                sampleRate = 16000,
                segments = emptyList(),
                keyHz = ref.keyHz
            )

            val extractor = CalibraPitch.createContourExtractor()

            val evalResult = withContext(Dispatchers.Default) {
                CalibraMelodyEval.evaluate(
                    reference = ref,
                    student = studentMaterial,
                    contourExtractor = extractor
                )
            }

            extractor.release()

            _result.value = evalResult
            _isEvaluating.value = false
            _status.value = "Evaluation complete: ${(evalResult.overallScore * 100).toInt()}%"
        }
    }

    /** Get the score for a specific segment index */
    fun segmentScore(index: Int): Float? {
        return _result.value?.latestScorePerSegment()?.get(index)
    }

    private fun copyAssetToFile(context: Context, assetName: String): File {
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

    override fun onCleared() {
        super.onCleared()
        recordingJob?.cancel()
        playerObserverJob?.cancel()
        timeObserverJob?.cancel()
        recorder?.stop()
        recorder?.release()
        referencePlayer?.release()
    }
}
