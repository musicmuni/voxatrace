package com.musicmuni.voxatrace.demo.sections.pitch.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.ContourExtractorConfig
import com.musicmuni.voxatrace.calibra.model.ContourCleanup
import com.musicmuni.voxatrace.calibra.model.PitchAlgorithm
import com.musicmuni.voxatrace.calibra.model.PitchContour
import com.musicmuni.voxatrace.demo.sections.pitch.model.PitchAlgorithmInfo
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for contour cleanup comparison.
 *
 * ## VoxaTrace Integration
 * ```kotlin
 * // 1. Extract raw contour
 * val extractor = CalibraPitch.createContourExtractor(config)
 * val raw = extractor.extract(samples, 16000)
 *
 * // 2. Apply cleanup presets
 * val scoring = CalibraPitch.PostProcess.cleanup(raw, ContourCleanup.SCORING)
 * val display = CalibraPitch.PostProcess.cleanup(raw, ContourCleanup.DISPLAY)
 * ```
 */
class ContourCleanupViewModel : ViewModel() {

    // Configuration
    private val _selectedAlgorithm = MutableStateFlow(0)
    val selectedAlgorithm: StateFlow<Int> = _selectedAlgorithm.asStateFlow()

    // State
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _rawContour = MutableStateFlow<PitchContour?>(null)
    val rawContour: StateFlow<PitchContour?> = _rawContour.asStateFlow()

    private val _scoringContour = MutableStateFlow<PitchContour?>(null)
    val scoringContour: StateFlow<PitchContour?> = _scoringContour.asStateFlow()

    private val _displayContour = MutableStateFlow<PitchContour?>(null)
    val displayContour: StateFlow<PitchContour?> = _displayContour.asStateFlow()

    private val _showRaw = MutableStateFlow(true)
    val showRaw: StateFlow<Boolean> = _showRaw.asStateFlow()

    private val _showScoring = MutableStateFlow(true)
    val showScoring: StateFlow<Boolean> = _showScoring.asStateFlow()

    private val _showDisplay = MutableStateFlow(false)
    val showDisplay: StateFlow<Boolean> = _showDisplay.asStateFlow()

    // Config data
    val algorithms = PitchAlgorithmInfo.all

    // Private state
    private var recorder: SonixRecorder? = null
    private var collectedSamples = mutableListOf<Float>()
    private var recordingJob: Job? = null

    // Actions

    fun setSelectedAlgorithm(index: Int) {
        _selectedAlgorithm.value = index
    }

    fun setShowRaw(value: Boolean) {
        _showRaw.value = value
    }

    fun setShowScoring(value: Boolean) {
        _showScoring.value = value
    }

    fun setShowDisplay(value: Boolean) {
        _showDisplay.value = value
    }

    fun toggleRecording(context: Context) {
        if (_isRecording.value) {
            stopRecording()
            processRecording()
        } else {
            startRecording(context)
        }
    }

    fun clearResults() {
        _rawContour.value = null
        _scoringContour.value = null
        _displayContour.value = null
        collectedSamples.clear()
    }

    fun onDisappear() {
        stopRecording()
        cleanup()
    }

    // Statistics

    data class ContourStats(
        val voicedCount: Int,
        val octaveErrors: Int,
        val blips: Int
    )

    fun contourStats(contour: PitchContour?): ContourStats {
        if (contour == null) {
            return ContourStats(voicedCount = 0, octaveErrors = 0, blips = 0)
        }
        val pitches = contour.pitchesHz.toList()
        val voiced = pitches.count { it > 0 }
        val octaveErrors = countOctaveErrors(pitches)
        val blips = countBlips(pitches, minFrames = 8)
        return ContourStats(voicedCount = voiced, octaveErrors = octaveErrors, blips = blips)
    }

    private fun countOctaveErrors(pitches: List<Float>): Int {
        var count = 0
        for (i in 1 until pitches.size) {
            if (pitches[i] > 0 && pitches[i - 1] > 0) {
                val ratio = pitches[i] / pitches[i - 1]
                if (ratio > 1.8f || ratio < 0.55f) {
                    count++
                }
            }
        }
        return count
    }

    private fun countBlips(pitches: List<Float>, minFrames: Int): Int {
        var blipCount = 0
        var runLength = 0
        for (pitch in pitches) {
            if (pitch > 0) {
                runLength++
            } else {
                if (runLength in 1 until minFrames) {
                    blipCount++
                }
                runLength = 0
            }
        }
        if (runLength in 1 until minFrames) {
            blipCount++
        }
        return blipCount
    }

    // Private methods

    private fun startRecording(context: Context) {
        val recordPath = "${context.cacheDir}/pitch_cleanup_temp.m4a"
        recorder?.release()
        recorder = SonixRecorder.create(recordPath, SonixRecorderConfig.VOICE)

        collectedSamples.clear()
        _isRecording.value = true

        recorder?.start()

        recordingJob = viewModelScope.launch {
            recorder?.audioBuffers?.collect { buffer ->
                // VOICE preset records at 16kHz; ContourExtractor handles resampling internally (ADR-017)
                collectedSamples.addAll(buffer.samples.toList())
            }
        }
    }

    private fun stopRecording() {
        recordingJob?.cancel()
        recorder?.stop()
        _isRecording.value = false
    }

    private fun processRecording() {
        if (collectedSamples.isEmpty()) return
        _isProcessing.value = true

        viewModelScope.launch {
            try {
                val algorithm = if (_selectedAlgorithm.value == 0) PitchAlgorithm.YIN else PitchAlgorithm.SWIFT_F0

                val extractorConfig = ContourExtractorConfig.Builder()
                    .algorithm(algorithm)
                    .pitchPreset(com.musicmuni.voxatrace.calibra.model.PitchPreset.BALANCED)
                    .cleanup(ContourCleanup.RAW)
                    .hopMs(10)
                    .build()

                val extractor = CalibraPitch.createContourExtractor(extractorConfig)
                val raw = extractor.extract(collectedSamples.toFloatArray(), 16000)
                extractor.release()

                val scoring = CalibraPitch.PostProcess.cleanup(raw, ContourCleanup.SCORING)
                val display = CalibraPitch.PostProcess.cleanup(raw, ContourCleanup.DISPLAY)

                _rawContour.value = raw
                _scoringContour.value = scoring
                _displayContour.value = display

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isProcessing.value = false
            }
        }
    }

    private fun cleanup() {
        recorder?.release()
        recorder = null
    }

    override fun onCleared() {
        super.onCleared()
        cleanup()
    }
}
