package com.musicmuni.voxatrace.demo.sections.melodyeval.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraMelodyEval
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.model.LessonMaterial
import com.musicmuni.voxatrace.calibra.model.Segment
import com.musicmuni.voxatrace.calibra.model.SingingResult
import com.musicmuni.voxatrace.sonix.AudioSessionManager
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import com.musicmuni.voxatrace.sonix.SonixResampler
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

/**
 * ViewModel for offline melody evaluation using CalibraMelodyEval.
 *
 * ## VoxaTrace Integration (~20 lines)
 * ```kotlin
 * // 1. Load reference
 * val transData = Parser.parseTransString(transContent)
 * val audioData = SonixDecoder.decode(audioPath)
 * reference = LessonMaterial.fromAudio(samples, sampleRate, segments, keyHz)
 *
 * // 2. Record student
 * recorder = SonixRecorder.create(tempPath, SonixRecorderConfig.VOICE)
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

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

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

    private val _status = MutableStateFlow("Ready")
    val status: StateFlow<String> = _status.asStateFlow()

    val referenceName = "Alankaar 01"

    // MARK: - Private State

    private var reference: LessonMaterial? = null
    private var recorder: SonixRecorder? = null
    private var collectedAudio = mutableListOf<Float>()
    private var recordingJob: Job? = null

    // MARK: - Actions

    fun loadReference(context: Context) {
        _status.value = "Loading reference..."

        viewModelScope.launch {
            try {
                // Load and parse trans file
                val transContent = withContext(Dispatchers.IO) {
                    context.assets.open("$referenceName.trans").bufferedReader().readText()
                }
                val transData = Parser.parseTransString(transContent)
                if (transData == null) {
                    _status.value = "Failed to parse trans file"
                    return@launch
                }

                // Use only first segment for easier testing
                val firstSeg = transData.segments.first()
                val segment = Segment(
                    index = 0,
                    startSeconds = firstSeg.startTime.toFloat(),
                    endSeconds = firstSeg.endTime.toFloat(),
                    lyrics = firstSeg.lyrics
                )
                _segments.value = listOf(segment)

                // Copy audio file
                val audioPath = withContext(Dispatchers.IO) {
                    copyAssetToFile(context, "$referenceName.m4a").absolutePath
                }

                val audioData = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(audioPath)
                }

                if (audioData == null) {
                    _status.value = "Failed to decode audio"
                    return@launch
                }

                reference = LessonMaterial.fromAudio(
                    samples = audioData.samples,
                    sampleRate = audioData.sampleRate,
                    segments = listOf(segment),
                    keyHz = 196.0f
                )

                _referenceLoaded.value = true
                _status.value = "Reference loaded. Record your performance."
            } catch (e: Exception) {
                _status.value = "Error: ${e.message}"
            }
        }
    }

    fun startRecording(context: Context) {
        collectedAudio.clear()
        _hasRecording.value = false
        _result.value = null
        _recordingDuration.value = 0f
        _status.value = "Recording..."

        val tempPath = "${context.cacheDir}/melody_eval_temp.m4a"
        recorder = SonixRecorder.create(tempPath, SonixRecorderConfig.VOICE)

        _isRecording.value = true

        recordingJob = viewModelScope.launch {
            val hwRate = AudioSessionManager.hardwareSampleRate.toInt()
            var sampleCount = 0

            recorder?.start()

            recorder?.audioBuffers?.collect { buffer ->
                val samples16k = SonixResampler.resample(
                    samples = buffer.samples,
                    fromRate = hwRate,
                    toRate = 16000
                )

                collectedAudio.addAll(samples16k.toList())
                sampleCount += samples16k.size

                // Calculate RMS for level meter
                val sum = samples16k.fold(0f) { acc, s -> acc + s * s }
                val rms = kotlin.math.sqrt(sum / samples16k.size)

                _recordingLevel.value = (rms * 5).coerceAtMost(1f)
                _recordingDuration.value = sampleCount / 16000f
            }
        }
    }

    fun stopRecording() {
        recordingJob?.cancel()
        recorder?.stop()
        _isRecording.value = false

        if (collectedAudio.isNotEmpty()) {
            _hasRecording.value = true
            _status.value = "Recording complete. Ready to evaluate."
        } else {
            _status.value = "No audio recorded. Try again."
        }
    }

    fun evaluate() {
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
        recorder?.stop()
        recorder?.release()
    }
}
