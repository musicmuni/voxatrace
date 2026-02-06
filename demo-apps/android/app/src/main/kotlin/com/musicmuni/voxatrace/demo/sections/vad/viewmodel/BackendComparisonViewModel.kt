package com.musicmuni.voxatrace.demo.sections.vad.viewmodel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraVAD
import com.musicmuni.voxatrace.calibra.model.VADBackend
import com.musicmuni.voxatrace.calibra.model.VADModelProvider
import com.musicmuni.voxatrace.calibra.model.VoiceActivityLevel
import com.musicmuni.voxatrace.demo.sections.vad.model.VADBackendInfo
import com.musicmuni.voxatrace.demo.sections.vad.model.VADConstants
import com.musicmuni.voxatrace.demo.sections.vad.model.WaveformSample
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * ViewModel for comparing two VAD backends side-by-side.
 *
 * ## VoxaTrace Integration
 * ```kotlin
 * // 1. Create VADs for two backends (auto-loads bundled models)
 * val vadLeft = CalibraVAD.create(VADModelProvider.singingRealtime())
 * val vadRight = CalibraVAD.create(VADModelProvider.speech())
 *
 * // 2. Process same audio through both
 * val ratioLeft = vadLeft.getVADRatio(samples16k)
 * val ratioRight = vadRight.getVADRatio(samples16k)
 * ```
 */
class BackendComparisonViewModel : ViewModel() {

    // Configuration
    private val _leftBackendIndex = MutableStateFlow(2) // Singing RT
    val leftBackendIndex: StateFlow<Int> = _leftBackendIndex.asStateFlow()

    private val _rightBackendIndex = MutableStateFlow(0) // Speech
    val rightBackendIndex: StateFlow<Int> = _rightBackendIndex.asStateFlow()

    // Published State - Left
    private val _vadRatioLeft = MutableStateFlow(0f)
    val vadRatioLeft: StateFlow<Float> = _vadRatioLeft.asStateFlow()

    private val _activityLevelLeft = MutableStateFlow(VoiceActivityLevel.NONE)
    val activityLevelLeft: StateFlow<VoiceActivityLevel> = _activityLevelLeft.asStateFlow()

    private val _waveformSamplesLeft = MutableStateFlow<List<WaveformSample>>(emptyList())
    val waveformSamplesLeft: StateFlow<List<WaveformSample>> = _waveformSamplesLeft.asStateFlow()

    private val _latencyMsLeft = MutableStateFlow(0)
    val latencyMsLeft: StateFlow<Int> = _latencyMsLeft.asStateFlow()

    // Published State - Right
    private val _vadRatioRight = MutableStateFlow(0f)
    val vadRatioRight: StateFlow<Float> = _vadRatioRight.asStateFlow()

    private val _activityLevelRight = MutableStateFlow(VoiceActivityLevel.NONE)
    val activityLevelRight: StateFlow<VoiceActivityLevel> = _activityLevelRight.asStateFlow()

    private val _waveformSamplesRight = MutableStateFlow<List<WaveformSample>>(emptyList())
    val waveformSamplesRight: StateFlow<List<WaveformSample>> = _waveformSamplesRight.asStateFlow()

    private val _latencyMsRight = MutableStateFlow(0)
    val latencyMsRight: StateFlow<Int> = _latencyMsRight.asStateFlow()

    // Published State - Recording
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    // Configuration Data
    val backends = VADBackendInfo.all
    val maxWaveformSamples = 100

    // Private
    private var vadLeft: CalibraVAD? = null
    private var vadRight: CalibraVAD? = null
    private var recorder: SonixRecorder? = null
    private var recordingJob: Job? = null

    // Actions

    fun setLeftBackendIndex(index: Int) {
        val wasRecording = _isRecording.value
        if (wasRecording) {
            stopRecordingInternal()
        }
        _leftBackendIndex.value = index
        recreateVADs()
        if (wasRecording) {
            // Can't restart without context
        }
    }

    fun setRightBackendIndex(index: Int) {
        val wasRecording = _isRecording.value
        if (wasRecording) {
            stopRecordingInternal()
        }
        _rightBackendIndex.value = index
        recreateVADs()
        if (wasRecording) {
            // Can't restart without context
        }
    }

    fun toggleRecording(context: Context) {
        if (_isRecording.value) {
            stopRecording()
        } else {
            startRecording(context)
        }
    }

    fun onDisappear() {
        stopRecordingInternal()
        cleanup()
    }

    // Helpers

    fun colorForLevel(level: VoiceActivityLevel): Color {
        return when (level) {
            VoiceActivityLevel.NONE -> Color.Gray
            VoiceActivityLevel.PARTIAL -> Color(0xFFFF9800)
            VoiceActivityLevel.FULL -> Color(0xFF4CAF50)
        }
    }

    fun textForLevel(level: VoiceActivityLevel): String {
        return when (level) {
            VoiceActivityLevel.NONE -> "None"
            VoiceActivityLevel.PARTIAL -> "Partial"
            VoiceActivityLevel.FULL -> "Full"
        }
    }

    // Private Methods

    private fun createVAD(backend: VADBackend): CalibraVAD {
        return when (backend) {
            VADBackend.GENERAL -> CalibraVAD.create(VADModelProvider.general)
            VADBackend.SPEECH -> CalibraVAD.create(VADModelProvider.speech())
            VADBackend.SINGING_REALTIME -> CalibraVAD.create(VADModelProvider.singingRealtime())
        }
    }

    private fun recreateVADs() {
        vadLeft?.release()
        vadRight?.release()

        vadLeft = createVAD(backends[_leftBackendIndex.value].backend)
        vadRight = createVAD(backends[_rightBackendIndex.value].backend)

        resetStatistics()
    }

    private fun resetStatistics() {
        _vadRatioLeft.value = 0f
        _vadRatioRight.value = 0f
        _activityLevelLeft.value = VoiceActivityLevel.NONE
        _activityLevelRight.value = VoiceActivityLevel.NONE
        _waveformSamplesLeft.value = emptyList()
        _waveformSamplesRight.value = emptyList()
        _latencyMsLeft.value = 0
        _latencyMsRight.value = 0
    }

    private fun startRecording(context: Context) {
        if (vadLeft == null) {
            vadLeft = createVAD(backends[_leftBackendIndex.value].backend)
        }
        if (vadRight == null) {
            vadRight = createVAD(backends[_rightBackendIndex.value].backend)
        }

        val recordPath = "${context.cacheDir}/vad_compare_temp.m4a"
        recorder = SonixRecorder.create(recordPath, SonixRecorderConfig.VOICE)

        val rec = recorder ?: return
        val vL = vadLeft ?: return
        val vR = vadRight ?: return

        _isRecording.value = true
        resetStatistics()

        rec.start()

        recordingJob = viewModelScope.launch {
            rec.audioBuffers.collect { buffer ->
                // VOICE preset records at 16kHz; CalibraVAD handles resampling internally (ADR-017)
                val samples = buffer.samples

                // Process left
                val startLeft = System.nanoTime()
                val ratioLeft = vL.getVADRatio(samples, 16000)
                val endLeft = System.nanoTime()
                val latencyLeft = ((endLeft - startLeft) / 1_000_000).toInt()

                // Process right
                val startRight = System.nanoTime()
                val ratioRight = vR.getVADRatio(samples, 16000)
                val endRight = System.nanoTime()
                val latencyRight = ((endRight - startRight) / 1_000_000).toInt()

                if (ratioLeft < 0 && ratioRight < 0) {
                    return@collect
                }

                val amplitude = calculateRMS(samples)

                // Update left
                if (ratioLeft >= 0) {
                    _vadRatioLeft.value = ratioLeft
                    _activityLevelLeft.value = classifyLevel(ratioLeft)
                    _latencyMsLeft.value = latencyLeft

                    val sampleLeft = WaveformSample(amplitude, ratioLeft > 0.5f)
                    val newSamplesLeft = _waveformSamplesLeft.value.toMutableList()
                    newSamplesLeft.add(sampleLeft)
                    if (newSamplesLeft.size > maxWaveformSamples) {
                        newSamplesLeft.removeAt(0)
                    }
                    _waveformSamplesLeft.value = newSamplesLeft
                }

                // Update right
                if (ratioRight >= 0) {
                    _vadRatioRight.value = ratioRight
                    _activityLevelRight.value = classifyLevel(ratioRight)
                    _latencyMsRight.value = latencyRight

                    val sampleRight = WaveformSample(amplitude, ratioRight > 0.5f)
                    val newSamplesRight = _waveformSamplesRight.value.toMutableList()
                    newSamplesRight.add(sampleRight)
                    if (newSamplesRight.size > maxWaveformSamples) {
                        newSamplesRight.removeAt(0)
                    }
                    _waveformSamplesRight.value = newSamplesRight
                }
            }
        }
    }

    private fun stopRecordingInternal() {
        recordingJob?.cancel()
        recorder?.stop()
        _isRecording.value = false
        vadLeft?.reset()
        vadRight?.reset()
    }

    fun stopRecording() {
        stopRecordingInternal()
    }

    private fun cleanup() {
        recorder?.release()
        recorder = null
        vadLeft?.release()
        vadLeft = null
        vadRight?.release()
        vadRight = null
    }

    private fun calculateRMS(samples: FloatArray): Float {
        if (samples.isEmpty()) return 0f
        val sumSquares = samples.fold(0f) { acc, sample -> acc + sample * sample }
        return sqrt(sumSquares / samples.size)
    }

    private fun classifyLevel(ratio: Float): VoiceActivityLevel {
        return when {
            ratio < VADConstants.noSingingThreshold -> VoiceActivityLevel.NONE
            ratio < VADConstants.partialSingingThreshold -> VoiceActivityLevel.PARTIAL
            else -> VoiceActivityLevel.FULL
        }
    }

    override fun onCleared() {
        super.onCleared()
        cleanup()
    }
}
