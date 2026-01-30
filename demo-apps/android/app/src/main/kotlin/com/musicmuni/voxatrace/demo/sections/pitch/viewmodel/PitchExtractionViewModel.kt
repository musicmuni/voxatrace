package com.musicmuni.voxatrace.demo.sections.pitch.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.ContourExtractorConfig
import com.musicmuni.voxatrace.calibra.model.PitchAlgorithm
import com.musicmuni.voxatrace.calibra.model.PitchContour
import com.musicmuni.voxatrace.demo.sections.pitch.model.CleanupPresetInfo
import com.musicmuni.voxatrace.demo.sections.pitch.model.ExtractionPresetInfo
import com.musicmuni.voxatrace.demo.sections.pitch.model.PitchAlgorithmInfo
import com.musicmuni.voxatrace.demo.sections.pitch.model.VoiceTypeInfo
import com.musicmuni.voxatrace.sonix.SonixDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.log2

/**
 * ViewModel for batch pitch extraction demo.
 */
class PitchExtractionViewModel : ViewModel() {

    // Configuration state
    private val _selectedAlgorithm = MutableStateFlow(0)
    val selectedAlgorithm: StateFlow<Int> = _selectedAlgorithm.asStateFlow()

    private val _selectedPreset = MutableStateFlow(1) // BALANCED
    val selectedPreset: StateFlow<Int> = _selectedPreset.asStateFlow()

    private val _selectedVoiceType = MutableStateFlow(0) // Auto
    val selectedVoiceType: StateFlow<Int> = _selectedVoiceType.asStateFlow()

    private val _selectedCleanup = MutableStateFlow(1) // SCORING
    val selectedCleanup: StateFlow<Int> = _selectedCleanup.asStateFlow()

    private val _hopMs = MutableStateFlow(10)
    val hopMs: StateFlow<Int> = _hopMs.asStateFlow()

    // Runtime state
    private val _isExtracting = MutableStateFlow(false)
    val isExtracting: StateFlow<Boolean> = _isExtracting.asStateFlow()

    private val _contour = MutableStateFlow<PitchContour?>(null)
    val contour: StateFlow<PitchContour?> = _contour.asStateFlow()

    // Statistics
    private val _duration = MutableStateFlow(0f)
    val duration: StateFlow<Float> = _duration.asStateFlow()

    private val _voicedRatio = MutableStateFlow(0f)
    val voicedRatio: StateFlow<Float> = _voicedRatio.asStateFlow()

    private val _meanPitchHz = MutableStateFlow(0f)
    val meanPitchHz: StateFlow<Float> = _meanPitchHz.asStateFlow()

    private val _minPitchHz = MutableStateFlow(0f)
    val minPitchHz: StateFlow<Float> = _minPitchHz.asStateFlow()

    private val _maxPitchHz = MutableStateFlow(0f)
    val maxPitchHz: StateFlow<Float> = _maxPitchHz.asStateFlow()

    private val _rangeSemitones = MutableStateFlow(0f)
    val rangeSemitones: StateFlow<Float> = _rangeSemitones.asStateFlow()

    private val _sampleCount = MutableStateFlow(0)
    val sampleCount: StateFlow<Int> = _sampleCount.asStateFlow()

    // Config data
    val algorithms = PitchAlgorithmInfo.all
    val presets = ExtractionPresetInfo.all
    val voiceTypes = VoiceTypeInfo.all
    val cleanupPresets = CleanupPresetInfo.all

    // Actions
    fun setSelectedAlgorithm(index: Int) {
        _selectedAlgorithm.value = index
    }

    fun setSelectedPreset(index: Int) {
        _selectedPreset.value = index
    }

    fun setSelectedVoiceType(index: Int) {
        _selectedVoiceType.value = index
    }

    fun setSelectedCleanup(index: Int) {
        _selectedCleanup.value = index
    }

    fun setHopMs(value: Int) {
        _hopMs.value = value.coerceIn(5, 50)
    }

    fun extractPitch(context: Context) {
        viewModelScope.launch {
            _isExtracting.value = true
            _contour.value = null

            try {
                // Copy audio asset to file
                val audioFile = withContext(Dispatchers.IO) {
                    copyAssetToFile(context, "sample.m4a")
                }

                // Decode audio
                val audioData = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(audioFile.absolutePath)
                }

                if (audioData == null) {
                    _isExtracting.value = false
                    return@launch
                }

                // Build extractor config
                val algorithm = if (_selectedAlgorithm.value == 0) PitchAlgorithm.YIN else PitchAlgorithm.SWIFT_F0
                val preset = presets[_selectedPreset.value].preset
                val voiceType = voiceTypes[_selectedVoiceType.value].voiceType
                val cleanup = cleanupPresets[_selectedCleanup.value].cleanup

                val extractorConfig = ContourExtractorConfig.Builder()
                    .algorithm(algorithm)
                    .pitchPreset(preset)
                    .voiceType(voiceType)
                    .cleanup(cleanup)
                    .hopMs(_hopMs.value)
                    .sampleRate(audioData.sampleRate)
                    .build()

                val extractor = CalibraPitch.createContourExtractor(extractorConfig)

                // Extract contour
                val result = withContext(Dispatchers.Default) {
                    extractor.extract(audioData.samples, audioData.sampleRate)
                }

                extractor.release()

                // Calculate statistics
                val pitchesHz = result.pitchesHz
                val voiced = pitchesHz.filter { it > 0 }
                val voicedRatioCalc = if (pitchesHz.isNotEmpty()) voiced.size.toFloat() / pitchesHz.size else 0f
                val meanCalc = if (voiced.isNotEmpty()) voiced.average().toFloat() else 0f
                val minCalc = voiced.minOrNull() ?: 0f
                val maxCalc = voiced.maxOrNull() ?: 0f
                val rangeCalc = if (minCalc > 0 && maxCalc > 0) 12f * log2(maxCalc / minCalc) else 0f

                // Update state
                _contour.value = result
                _duration.value = result.duration
                _voicedRatio.value = voicedRatioCalc
                _meanPitchHz.value = meanCalc
                _minPitchHz.value = minCalc
                _maxPitchHz.value = maxCalc
                _rangeSemitones.value = rangeCalc
                _sampleCount.value = pitchesHz.size

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isExtracting.value = false
            }
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

    companion object {
        /**
         * Convert Hz to MIDI note name.
         */
        fun hzToNoteName(hz: Float): String {
            if (hz <= 0) return "-"
            val midi = (69 + 12 * log2(hz / 440f)).toInt()
            val noteNames = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
            val noteName = noteNames[midi % 12]
            val octave = (midi / 12) - 1
            return "$noteName$octave"
        }
    }
}
