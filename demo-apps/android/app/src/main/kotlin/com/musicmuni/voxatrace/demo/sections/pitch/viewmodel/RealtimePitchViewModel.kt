package com.musicmuni.voxatrace.demo.sections.pitch.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.model.PitchAlgorithm
import com.musicmuni.voxatrace.calibra.model.PitchDetectorConfig
import com.musicmuni.voxatrace.calibra.model.PitchPoint
import com.musicmuni.voxatrace.demo.sections.pitch.model.PitchAlgorithmInfo
import com.musicmuni.voxatrace.demo.sections.pitch.model.PitchPresetInfo
import com.musicmuni.voxatrace.demo.sections.pitch.model.VoiceTypeInfo
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
 * ViewModel for real-time pitch detection demo.
 */
class RealtimePitchViewModel : ViewModel() {

    // Configuration state
    private val _selectedAlgorithm = MutableStateFlow(0) // YIN default
    val selectedAlgorithm: StateFlow<Int> = _selectedAlgorithm.asStateFlow()

    private val _selectedPreset = MutableStateFlow(1) // BALANCED
    val selectedPreset: StateFlow<Int> = _selectedPreset.asStateFlow()

    private val _selectedVoiceType = MutableStateFlow(0) // Auto
    val selectedVoiceType: StateFlow<Int> = _selectedVoiceType.asStateFlow()

    // Runtime state
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _currentPitch = MutableStateFlow<PitchPoint?>(null)
    val currentPitch: StateFlow<PitchPoint?> = _currentPitch.asStateFlow()

    private val _amplitude = MutableStateFlow(0f)
    val amplitude: StateFlow<Float> = _amplitude.asStateFlow()

    // Config data
    val algorithms = PitchAlgorithmInfo.all
    val presets = PitchPresetInfo.all
    val voiceTypes = VoiceTypeInfo.all

    // Private state
    private var detector: CalibraPitch.Detector? = null
    private var recorder: SonixRecorder? = null
    private var recordingJob: Job? = null

    // Actions

    fun setSelectedAlgorithm(index: Int) {
        _selectedAlgorithm.value = index
        if (_isRecording.value) {
            stopRecording()
        }
    }

    fun setSelectedPreset(index: Int) {
        _selectedPreset.value = index
    }

    fun setSelectedVoiceType(index: Int) {
        _selectedVoiceType.value = index
    }

    fun startRecording(context: Context) {
        viewModelScope.launch {
            val recordPath = "${context.cacheDir}/realtime_pitch.m4a"
            recorder?.release()
            recorder = SonixRecorder.create(recordPath, SonixRecorderConfig.VOICE)

            detector?.release()
            detector = createDetector()

            recorder?.start()
            _isRecording.value = true

            // Process audio buffers
            recordingJob = launch {
                recorder?.audioBuffers?.collect { buffer ->
                    val det = detector ?: return@collect

                    // Resample to 16kHz for pitch detection
                    val hwRate = AudioSessionManager.hardwareSampleRate.toInt()
                    val samples16k = SonixResampler.resample(
                        samples = buffer.samples,
                        fromRate = hwRate,
                        toRate = 16000
                    )

                    // Detect pitch
                    val result = det.detect(samples16k, 16000)
                    val amp = det.getAmplitude(samples16k, 16000)

                    _currentPitch.value = result
                    _amplitude.value = amp
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
            _currentPitch.value = null
            _amplitude.value = 0f
        }
    }

    private fun createDetector(): CalibraPitch.Detector {
        val algorithm = if (_selectedAlgorithm.value == 0) PitchAlgorithm.YIN else PitchAlgorithm.SWIFT_F0
        val voiceType = voiceTypes[_selectedVoiceType.value].voiceType

        val detectorConfig = PitchDetectorConfig.Builder()
            .algorithm(algorithm)
            .voiceType(voiceType)
            .build()

        return CalibraPitch.createDetector(detectorConfig)
    }

    override fun onCleared() {
        super.onCleared()
        recordingJob?.cancel()
        recorder?.release()
        detector?.release()
    }
}
