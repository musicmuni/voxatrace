package com.musicmuni.voxatrace.demo.sections.intonation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.accura.Accura
import com.musicmuni.voxatrace.accura.model.IntonationAnalysisResult
import com.musicmuni.voxatrace.accura.model.IntonationSystem
import com.musicmuni.voxatrace.accura.model.NoteLabelTradition
import com.musicmuni.voxatrace.accura.model.PitchingScore
import com.musicmuni.voxatrace.sonix.SonixDecoder
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
 * ViewModel for Intonation Analysis - pitch accuracy against EQ and JI tuning systems.
 *
 * APIs demonstrated:
 * - Accura.analyzePitching() for intonation analysis
 * - Accura.calculateScore() for scoring
 * - PitchDetection.createContourExtractor() for pitch extraction
 * - SonixDecoder.decode() for audio decoding
 */
class IntonationViewModel : ViewModel() {

    // Published state
    private val _eqResult = MutableStateFlow<IntonationAnalysisResult?>(null)
    val eqResult: StateFlow<IntonationAnalysisResult?> = _eqResult.asStateFlow()

    private val _jiResult = MutableStateFlow<IntonationAnalysisResult?>(null)
    val jiResult: StateFlow<IntonationAnalysisResult?> = _jiResult.asStateFlow()

    private val _eqScore = MutableStateFlow<PitchingScore?>(null)
    val eqScore: StateFlow<PitchingScore?> = _eqScore.asStateFlow()

    private val _jiScore = MutableStateFlow<PitchingScore?>(null)
    val jiScore: StateFlow<PitchingScore?> = _jiScore.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    // Actions

    fun analyzeOffline(context: Context) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _eqResult.value = null
            _jiResult.value = null
            _eqScore.value = null
            _jiScore.value = null

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

                // Analyze against Equal Temperament
                val eqAnalysis = withContext(Dispatchers.IO) {
                    Accura.analyzePitching(
                        contour,
                        tonicHz = 146.83f,
                        intonationSystem = IntonationSystem.EQ,
                        noteLabelTradition = NoteLabelTradition.CARNATIC
                    )
                }
                _eqResult.value = eqAnalysis

                if (eqAnalysis != null) {
                    _eqScore.value = Accura.calculateScore(eqAnalysis)
                }

                // Analyze against Just Intonation
                val jiAnalysis = withContext(Dispatchers.IO) {
                    Accura.analyzePitching(
                        contour,
                        tonicHz = 146.83f,
                        intonationSystem = IntonationSystem.JI,
                        noteLabelTradition = NoteLabelTradition.CARNATIC
                    )
                }
                _jiResult.value = jiAnalysis

                if (jiAnalysis != null) {
                    _jiScore.value = Accura.calculateScore(jiAnalysis)
                }

            } catch (e: Exception) {
                Napier.e("Intonation analysis failed", e)
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
