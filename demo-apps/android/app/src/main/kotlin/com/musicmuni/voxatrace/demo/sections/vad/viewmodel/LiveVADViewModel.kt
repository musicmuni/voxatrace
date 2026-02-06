package com.musicmuni.voxatrace.demo.sections.vad.viewmodel

import android.content.Context
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.sqrt

/**
 * ViewModel for Voice Activity Detection.
 */
class LiveVADViewModel : ViewModel() {

    // Configuration
    private val _selectedBackendIndex = MutableStateFlow(0)
    val selectedBackendIndex: StateFlow<Int> = _selectedBackendIndex.asStateFlow()

    private val _threshold = MutableStateFlow(0.5f)
    val threshold: StateFlow<Float> = _threshold.asStateFlow()

    // Published state
    private val _vadRatio = MutableStateFlow(0f)
    val vadRatio: StateFlow<Float> = _vadRatio.asStateFlow()

    private val _activityLevel = MutableStateFlow(VoiceActivityLevel.NONE)
    val activityLevel: StateFlow<VoiceActivityLevel> = _activityLevel.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _waveformSamples = MutableStateFlow<List<WaveformSample>>(emptyList())
    val waveformSamples: StateFlow<List<WaveformSample>> = _waveformSamples.asStateFlow()

    private val _voiceTimeSeconds = MutableStateFlow(0f)
    val voiceTimeSeconds: StateFlow<Float> = _voiceTimeSeconds.asStateFlow()

    private val _silenceTimeSeconds = MutableStateFlow(0f)
    val silenceTimeSeconds: StateFlow<Float> = _silenceTimeSeconds.asStateFlow()

    private val _lastProcessingLatencyMs = MutableStateFlow(0)
    val lastProcessingLatencyMs: StateFlow<Int> = _lastProcessingLatencyMs.asStateFlow()

    // Backend info
    val backends = VADBackendInfo.all

    val maxWaveformSamples = 100

    // API code example
    val apiCodeExample: String
        get() {
            val backend = backends[_selectedBackendIndex.value]
            return when (backend.backend) {
                VADBackend.GENERAL -> """
                    val vad = CalibraVAD.create(VADModelProvider.general)
                    val ratio = vad.getVADRatio(samples16k)
                """.trimIndent()
                VADBackend.SPEECH -> """
                    // Auto-loads bundled Silero model
                    val vad = CalibraVAD.create(VADModelProvider.speech())
                    val ratio = vad.getVADRatio(samples16k)
                """.trimIndent()
                VADBackend.SINGING_REALTIME -> """
                    // Auto-loads bundled SwiftF0 model
                    val vad = CalibraVAD.create(VADModelProvider.singingRealtime())
                    val ratio = vad.getVADRatio(samples16k)
                """.trimIndent()
            }
        }

    // Private state
    private var vad: CalibraVAD? = null
    private var recorder: SonixRecorder? = null
    private var recordingJob: Job? = null

    // Actions

    fun setSelectedBackend(index: Int) {
        val wasRecording = _isRecording.value
        if (wasRecording) {
            stopRecordingInternal()
        }
        _selectedBackendIndex.value = index
        vad?.release()
        vad = null
        resetStatistics()
        if (wasRecording) {
            // We can't restart without context, so user needs to press start again
        }
    }

    fun setThreshold(value: Float) {
        _threshold.value = value.coerceIn(0.2f, 0.9f)
    }

    fun toggleRecording(context: Context) {
        if (_isRecording.value) {
            stopRecording()
        } else {
            startRecording(context)
        }
    }

    fun startRecording(context: Context) {
        viewModelScope.launch {
            val recordPath = "${context.cacheDir}/vad_temp.m4a"
            recorder?.release()
            recorder = SonixRecorder.create(recordPath, SonixRecorderConfig.VOICE)

            vad?.release()
            // Load VAD model off the main thread (YAMNet for singing backend is heavy)
            vad = withContext(Dispatchers.Default) { createVAD() }

            resetStatistics()

            recorder?.start()
            _isRecording.value = true

            recordingJob = launch {
                recorder?.audioBuffers?.collect { buffer ->
                    val v = vad ?: return@collect

                    // VOICE preset records at 16kHz; CalibraVAD handles resampling internally (ADR-017)
                    val samples = buffer.samples

                    val startTime = System.nanoTime()
                    val ratio = v.getVADRatio(samples, 16000)
                    val endTime = System.nanoTime()
                    val latencyMs = ((endTime - startTime) / 1_000_000).toInt()

                    if (ratio >= 0) {
                        _vadRatio.value = ratio
                        _activityLevel.value = classifyLevel(ratio)
                        _lastProcessingLatencyMs.value = latencyMs

                        // Calculate amplitude (RMS)
                        val amplitude = calculateRMS(samples)

                        // Update waveform samples
                        val isVoice = ratio > _threshold.value
                        val sample = WaveformSample(amplitude, isVoice)
                        val newSamples = _waveformSamples.value.toMutableList()
                        newSamples.add(sample)
                        if (newSamples.size > maxWaveformSamples) {
                            newSamples.removeAt(0)
                        }
                        _waveformSamples.value = newSamples

                        // Update time tracking (assuming ~100ms per buffer)
                        val bufferDurationSec = 0.1f
                        if (isVoice) {
                            _voiceTimeSeconds.value += bufferDurationSec
                        } else {
                            _silenceTimeSeconds.value += bufferDurationSec
                        }
                    }
                }
            }
        }
    }

    fun stopRecording() {
        stopRecordingInternal()
    }

    fun onDisappear() {
        stopRecordingInternal()
        cleanup()
    }

    private fun stopRecordingInternal() {
        recordingJob?.cancel()
        recorder?.stop()
        _isRecording.value = false
        vad?.reset()
    }

    private fun resetStatistics() {
        _vadRatio.value = 0f
        _activityLevel.value = VoiceActivityLevel.NONE
        _waveformSamples.value = emptyList()
        _voiceTimeSeconds.value = 0f
        _silenceTimeSeconds.value = 0f
        _lastProcessingLatencyMs.value = 0
    }

    private fun createVAD(): CalibraVAD {
        val backend = backends[_selectedBackendIndex.value].backend
        return when (backend) {
            VADBackend.GENERAL -> CalibraVAD.create(VADModelProvider.general)
            VADBackend.SPEECH -> CalibraVAD.create(VADModelProvider.speech())
            VADBackend.SINGING_REALTIME -> CalibraVAD.create(VADModelProvider.singingRealtime())
        }
    }

    private fun classifyLevel(ratio: Float): VoiceActivityLevel {
        return when {
            ratio < VADConstants.noSingingThreshold -> VoiceActivityLevel.NONE
            ratio < VADConstants.partialSingingThreshold -> VoiceActivityLevel.PARTIAL
            else -> VoiceActivityLevel.FULL
        }
    }

    private fun calculateRMS(samples: FloatArray): Float {
        if (samples.isEmpty()) return 0f
        val sumSquares = samples.fold(0f) { acc, sample -> acc + sample * sample }
        return sqrt(sumSquares / samples.size)
    }

    private fun cleanup() {
        recorder?.release()
        recorder = null
        vad?.release()
        vad = null
    }

    override fun onCleared() {
        super.onCleared()
        cleanup()
    }
}
