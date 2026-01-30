package com.musicmuni.voxatrace.demo.sections.pitch.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.model.PitchAlgorithm
import com.musicmuni.voxatrace.calibra.model.PitchDetectorConfig
import com.musicmuni.voxatrace.calibra.model.PitchPoint
import com.musicmuni.voxatrace.demo.sections.pitch.model.PitchAlgorithmInfo
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for exploring PitchPoint properties in real-time.
 *
 * ## VoxaTrace Integration
 * ```kotlin
 * // 1. Create detector
 * val detector = CalibraPitch.createDetector(config)
 *
 * // 2. Detect pitch
 * val result = detector.detect(samples, sampleRate)
 * // Access: result.pitch, result.isSinging, result.note, result.midiNote,
 * //         result.centsOff, result.tuning, result.reliability, result.confidence
 * ```
 */
class PitchPointExplorerViewModel : ViewModel() {

    // Configuration
    private val _selectedAlgorithm = MutableStateFlow(0)
    val selectedAlgorithm: StateFlow<Int> = _selectedAlgorithm.asStateFlow()

    // State
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _currentPitch = MutableStateFlow<PitchPoint?>(null)
    val currentPitch: StateFlow<PitchPoint?> = _currentPitch.asStateFlow()

    private val _amplitude = MutableStateFlow(0f)
    val amplitude: StateFlow<Float> = _amplitude.asStateFlow()

    // Config data
    val algorithms = PitchAlgorithmInfo.all

    // Private state
    private var detector: CalibraPitch.Detector? = null
    private var recorder: SonixRecorder? = null
    private var recordingJob: Job? = null

    // Actions

    fun setSelectedAlgorithm(index: Int) {
        if (_isRecording.value) {
            stopRecording()
        }
        detector?.release()
        detector = null
        _selectedAlgorithm.value = index
    }

    fun toggleRecording(context: Context) {
        if (_isRecording.value) {
            stopRecording()
        } else {
            startRecording(context)
        }
    }

    fun onDisappear() {
        stopRecording()
        cleanup()
    }

    // Helpers

    fun tuningString(tuning: PitchPoint.Tuning): String {
        return when (tuning) {
            PitchPoint.Tuning.SILENT -> "SILENT"
            PitchPoint.Tuning.FLAT -> "FLAT"
            PitchPoint.Tuning.IN_TUNE -> "IN_TUNE"
            PitchPoint.Tuning.SHARP -> "SHARP"
        }
    }

    // Private methods

    private fun setupAudioIfNeeded(context: Context) {
        if (detector == null) {
            val algorithm = if (_selectedAlgorithm.value == 0) PitchAlgorithm.YIN else PitchAlgorithm.SWIFT_F0
            val detectorConfig = PitchDetectorConfig.Builder()
                .algorithm(algorithm)
                .build()

            detector = CalibraPitch.createDetector(detectorConfig)
        }

        if (recorder == null) {
            val recordPath = "${context.cacheDir}/pitch_explorer_temp.m4a"
            recorder = SonixRecorder.create(recordPath, SonixRecorderConfig.VOICE)
        }
    }

    private fun cleanup() {
        recorder?.stop()
        recorder?.release()
        recorder = null

        detector?.release()
        detector = null
    }

    private fun startRecording(context: Context) {
        setupAudioIfNeeded(context)
        val rec = recorder ?: return
        val det = detector ?: return

        det.reset()
        _isRecording.value = true

        rec.start()

        recordingJob = viewModelScope.launch {
            rec.audioBuffers.collect { buffer ->
                // VOICE preset records at 16kHz; CalibraPitch handles resampling internally (ADR-017)
                val samples = buffer.samples

                val result = det.detect(samples, 16000)
                val amp = det.getAmplitude(samples, 16000)

                _currentPitch.value = result
                _amplitude.value = amp
            }
        }
    }

    private fun stopRecording() {
        recordingJob?.cancel()
        recorder?.stop()
        _isRecording.value = false

        detector?.reset()
        _currentPitch.value = null
        _amplitude.value = 0f
    }

    override fun onCleared() {
        super.onCleared()
        cleanup()
    }
}
