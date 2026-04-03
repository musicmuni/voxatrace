package com.musicmuni.voxatrace.demo.sections.voiceprofile.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.tessera.Tessera
import com.musicmuni.voxatrace.tessera.model.TesseraMetric
import com.musicmuni.voxatrace.tona.PitchDetection
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * ViewModel for Voice Profile - multi-metric batch analysis via Tessera.analyze().
 *
 * APIs demonstrated:
 * - Tessera.analyze() for batch analysis (breath, agility, vocal range)
 * - PitchDetection.createContourExtractor() for pitch extraction
 * - SonixDecoder.decode() for audio decoding
 */
class VoiceProfileViewModel : ViewModel() {

    // Published state
    private val _breathCapacity = MutableStateFlow(0f)
    val breathCapacity: StateFlow<Float> = _breathCapacity.asStateFlow()

    private val _breathControl = MutableStateFlow(0f)
    val breathControl: StateFlow<Float> = _breathControl.asStateFlow()

    private val _agilityScore = MutableStateFlow(0f)
    val agilityScore: StateFlow<Float> = _agilityScore.asStateFlow()

    private val _vocalRangeLow = MutableStateFlow("")
    val vocalRangeLow: StateFlow<String> = _vocalRangeLow.asStateFlow()

    private val _vocalRangeHigh = MutableStateFlow("")
    val vocalRangeHigh: StateFlow<String> = _vocalRangeHigh.asStateFlow()

    private val _vocalRangeOctaves = MutableStateFlow(0f)
    val vocalRangeOctaves: StateFlow<Float> = _vocalRangeOctaves.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    // Actions

    fun analyzeOffline(context: Context) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _breathCapacity.value = 0f
            _breathControl.value = 0f
            _agilityScore.value = 0f
            _vocalRangeLow.value = ""
            _vocalRangeHigh.value = ""
            _vocalRangeOctaves.value = 0f

            try {
                val audioFile = withContext(Dispatchers.IO) {
                    copyAssetToFile(context, "Alankaar 01_voice.m4a")
                }

                val audioData = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(audioFile.absolutePath)
                }

                if (audioData == null) {
                    Napier.e("Failed to decode audio file")
                    _isAnalyzing.value = false
                    return@launch
                }

                // Extract pitch contour via tona
                val extractor = PitchDetection.createContourExtractor()
                val contour = withContext(Dispatchers.IO) {
                    extractor.extract(audioData.samples, audioData.sampleRate)
                }
                extractor.release()

                // Batch analysis via Tessera.analyze()
                val result = withContext(Dispatchers.IO) {
                    Tessera.analyze(contour)
                }

                result.breath?.let { breath ->
                    _breathCapacity.value = breath.capacity
                    _breathControl.value = breath.controlScore
                }

                result.agility?.let { agility ->
                    _agilityScore.value = agility.scores.firstOrNull() ?: 0f
                }

                result.vocalRange?.let { vocalRange ->
                    _vocalRangeLow.value = vocalRange.range.lower.noteLabel
                    _vocalRangeHigh.value = vocalRange.range.upper.noteLabel
                    _vocalRangeOctaves.value = vocalRange.range.octaves
                }

            } catch (e: Exception) {
                Napier.e("Voice profile analysis failed", e)
            } finally {
                _isAnalyzing.value = false
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
}
