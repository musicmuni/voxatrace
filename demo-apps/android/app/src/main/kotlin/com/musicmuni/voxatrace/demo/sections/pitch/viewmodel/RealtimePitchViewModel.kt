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
import com.musicmuni.voxatrace.demo.sections.pitch.model.QuietHandlingInfo
import com.musicmuni.voxatrace.demo.sections.pitch.model.StrictnessInfo
import com.musicmuni.voxatrace.demo.sections.pitch.model.VoiceTypeInfo
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
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

    private val _selectedQuietHandling = MutableStateFlow(1) // NORMAL
    val selectedQuietHandling: StateFlow<Int> = _selectedQuietHandling.asStateFlow()

    private val _selectedStrictness = MutableStateFlow(1) // BALANCED
    val selectedStrictness: StateFlow<Int> = _selectedStrictness.asStateFlow()

    // Runtime state
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _currentPitch = MutableStateFlow<PitchPoint?>(null)
    val currentPitch: StateFlow<PitchPoint?> = _currentPitch.asStateFlow()

    private val _amplitude = MutableStateFlow(0f)
    val amplitude: StateFlow<Float> = _amplitude.asStateFlow()

    // Pitch history for scrolling graph
    private val _pitchHistory = MutableStateFlow<List<Float>>(emptyList())
    val pitchHistory: StateFlow<List<Float>> = _pitchHistory.asStateFlow()

    // Recorded pitches for session graph
    private val _recordedPitches = MutableStateFlow<List<Float>>(emptyList())
    val recordedPitches: StateFlow<List<Float>> = _recordedPitches.asStateFlow()

    // Show session graph after stopping
    private val _showGraph = MutableStateFlow(false)
    val showGraph: StateFlow<Boolean> = _showGraph.asStateFlow()

    // Config data
    val algorithms = PitchAlgorithmInfo.all
    val presets = PitchPresetInfo.all
    val voiceTypes = VoiceTypeInfo.all
    val quietHandlings = QuietHandlingInfo.all
    val strictnesses = StrictnessInfo.all

    val maxHistoryPoints = 200

    // Private state
    private var detector: CalibraPitch.Detector? = null
    private var recorder: SonixRecorder? = null
    private var recordingJob: Job? = null

    // Actions

    fun setSelectedAlgorithm(index: Int) {
        _selectedAlgorithm.value = index
        if (_isRecording.value) {
            recreateDetectorIfRecording()
        }
    }

    fun setSelectedPreset(index: Int) {
        _selectedPreset.value = index
        if (_isRecording.value) {
            recreateDetectorIfRecording()
        }
    }

    fun setSelectedVoiceType(index: Int) {
        _selectedVoiceType.value = index
        if (_isRecording.value) {
            recreateDetectorIfRecording()
        }
    }

    fun setSelectedQuietHandling(index: Int) {
        _selectedQuietHandling.value = index
        if (_isRecording.value) {
            recreateDetectorIfRecording()
        }
    }

    fun setSelectedStrictness(index: Int) {
        _selectedStrictness.value = index
        if (_isRecording.value) {
            recreateDetectorIfRecording()
        }
    }

    private fun recreateDetectorIfRecording() {
        if (_isRecording.value) {
            detector?.release()
            detector = createDetector()
        }
    }

    fun startRecording(context: Context) {
        viewModelScope.launch {
            val recordPath = "${context.cacheDir}/realtime_pitch.m4a"
            recorder?.release()
            recorder = SonixRecorder.create(recordPath, SonixRecorderConfig.VOICE)

            detector?.release()
            detector = createDetector()
            detector?.reset()

            _recordedPitches.value = emptyList()
            _pitchHistory.value = emptyList()
            _showGraph.value = false

            recorder?.start()
            _isRecording.value = true

            // Process audio buffers
            recordingJob = launch {
                recorder?.audioBuffers?.collect { buffer ->
                    val det = detector ?: return@collect

                    // VOICE preset records at 16kHz - use directly, no resampling needed
                    val samples = buffer.samples

                    // Detect pitch
                    val result = det.detect(samples, 16000)
                    val amp = det.getAmplitude(samples, 16000)

                    _currentPitch.value = result
                    _amplitude.value = amp

                    // Update pitch history (for scrolling view)
                    val newHistory = _pitchHistory.value.toMutableList()
                    newHistory.add(result.pitch)
                    if (newHistory.size > maxHistoryPoints) {
                        newHistory.removeAt(0)
                    }
                    _pitchHistory.value = newHistory

                    // Record all pitches
                    _recordedPitches.value = _recordedPitches.value + result.pitch
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
            detector?.reset()

            // Show graph if we have recorded pitches
            if (_recordedPitches.value.isNotEmpty()) {
                _showGraph.value = true
            }
        }
    }

    fun toggleRecording(context: Context) {
        if (_isRecording.value) {
            stopRecording()
        } else {
            startRecording(context)
        }
    }

    fun clearRecording() {
        _recordedPitches.value = emptyList()
        _showGraph.value = false
    }

    private fun createDetector(): CalibraPitch.Detector {
        val algorithm = if (_selectedAlgorithm.value == 0) PitchAlgorithm.YIN else PitchAlgorithm.SWIFT_F0
        val voiceType = voiceTypes[_selectedVoiceType.value].voiceType
        val quietHandling = quietHandlings[_selectedQuietHandling.value].handling
        val strictness = strictnesses[_selectedStrictness.value].strictness

        val detectorConfig = PitchDetectorConfig.Builder()
            .algorithm(algorithm)
            .voiceType(voiceType)
            .quietHandling(quietHandling)
            .strictness(strictness)
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
