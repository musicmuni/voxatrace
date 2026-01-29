package com.musicmuni.voxatrace.demo.sections.vad.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraVAD
import com.musicmuni.voxatrace.calibra.model.VADBackend
import com.musicmuni.voxatrace.calibra.model.VADModelProvider
import com.musicmuni.voxatrace.calibra.model.VoiceActivityLevel
import com.musicmuni.voxatrace.sonix.AudioSessionManager
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import com.musicmuni.voxatrace.sonix.SonixResampler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Voice Activity Detection.
 */
class LiveVADViewModel : ViewModel() {

    // Published state
    private val _vadRatio = MutableStateFlow(0f)
    val vadRatio: StateFlow<Float> = _vadRatio.asStateFlow()

    private val _activityLevel = MutableStateFlow(VoiceActivityLevel.NONE)
    val activityLevel: StateFlow<VoiceActivityLevel> = _activityLevel.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _selectedBackendIndex = MutableStateFlow(0)
    val selectedBackendIndex: StateFlow<Int> = _selectedBackendIndex.asStateFlow()

    // Backend info
    data class BackendInfo(val backend: VADBackend, val name: String, val description: String)

    val backends = listOf(
        BackendInfo(VADBackend.SPEECH, "Speech", "Silero neural network - high accuracy for speech"),
        BackendInfo(VADBackend.GENERAL, "General", "RMS-based - fast, no neural network"),
        BackendInfo(VADBackend.SINGING_REALTIME, "Singing Realtime", "Pitch-based - low latency singing detection"),
        BackendInfo(VADBackend.SINGING, "Singing", "Essentia YAMNet - high accuracy singing detection")
    )

    // Private state
    private var vad: CalibraVAD? = null
    private var recorder: SonixRecorder? = null
    private var recordingJob: Job? = null

    // Actions

    fun setSelectedBackend(index: Int) {
        _selectedBackendIndex.value = index
        vad?.release()
        vad = createVAD()
    }

    fun startRecording(context: Context) {
        viewModelScope.launch {
            val recordPath = "${context.cacheDir}/vad_temp.m4a"
            recorder?.release()
            recorder = SonixRecorder.create(recordPath, SonixRecorderConfig.VOICE)

            vad?.release()
            vad = createVAD()

            recorder?.start()
            _isRecording.value = true

            recordingJob = launch {
                recorder?.audioBuffers?.collect { buffer ->
                    val v = vad ?: return@collect

                    val hwRate = AudioSessionManager.hardwareSampleRate.toInt()
                    val samples16k = SonixResampler.resample(
                        samples = buffer.samples,
                        fromRate = hwRate,
                        toRate = 16000
                    )

                    val ratio = v.getVADRatio(samples16k)
                    if (ratio >= 0) {
                        _vadRatio.value = ratio
                        _activityLevel.value = when {
                            ratio < 0.2f -> VoiceActivityLevel.NONE
                            ratio < 0.6f -> VoiceActivityLevel.PARTIAL
                            else -> VoiceActivityLevel.FULL
                        }
                    }
                }
            }
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            recordingJob?.cancel()
            recorder?.stop()
            recorder?.release()
            recorder = null
            _isRecording.value = false
            vad?.reset()
            _vadRatio.value = 0f
            _activityLevel.value = VoiceActivityLevel.NONE
        }
    }

    private fun createVAD(): CalibraVAD {
        val backend = backends[_selectedBackendIndex.value].backend
        return when (backend) {
            VADBackend.GENERAL -> CalibraVAD.create(VADModelProvider.General)
            else -> {
                // For neural backends, model provider would be needed
                // For demo without models, fall back to GENERAL
                CalibraVAD.create(VADModelProvider.General)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        recordingJob?.cancel()
        recorder?.release()
        vad?.release()
    }
}
