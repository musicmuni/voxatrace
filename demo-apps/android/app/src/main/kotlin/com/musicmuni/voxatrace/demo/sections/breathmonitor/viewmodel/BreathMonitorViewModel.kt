package com.musicmuni.voxatrace.demo.sections.breathmonitor.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraVAD
import com.musicmuni.voxatrace.calibra.model.VADModelProvider
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import com.musicmuni.voxatrace.tessera.TesseraBreath
import com.musicmuni.voxatrace.tessera.model.BreathConfig
import com.musicmuni.voxatrace.tona.PitchDetection
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

enum class BreathMonitorState {
    IDLE,
    RECORDING,
    ANALYZING,
    COMPLETE
}

/**
 * ViewModel for Breath Monitor.
 *
 * Real-time: records audio with VAD voice/silence indicator.
 * On stop: analyzes recording for breath capacity and control.
 *
 * APIs demonstrated:
 * - CalibraVAD (singingRealtime) for real-time voice detection
 * - TesseraBreath.analyze() for breath control score + phrase summary
 * - PitchDetection.createContourExtractor() for pitch extraction
 * - SonixDecoder.decode() for audio file loading
 */
class BreathMonitorViewModel : ViewModel() {

    // State
    private val _state = MutableStateFlow(BreathMonitorState.IDLE)
    val state: StateFlow<BreathMonitorState> = _state.asStateFlow()

    private val _isVoiceDetected = MutableStateFlow(false)
    val isVoiceDetected: StateFlow<Boolean> = _isVoiceDetected.asStateFlow()

    private val _recordingLevel = MutableStateFlow(0f)
    val recordingLevel: StateFlow<Float> = _recordingLevel.asStateFlow()

    // Results (populated after analysis)
    private val _breathCapacity = MutableStateFlow<Float?>(null)
    val breathCapacity: StateFlow<Float?> = _breathCapacity.asStateFlow()

    private val _controlScore = MutableStateFlow<Float?>(null)
    val controlScore: StateFlow<Float?> = _controlScore.asStateFlow()

    // Offline analysis state (bundled audio)
    private val _offlineBreathCapacity = MutableStateFlow(0f)
    val offlineBreathCapacity: StateFlow<Float> = _offlineBreathCapacity.asStateFlow()

    private val _offlineControlScore = MutableStateFlow(0f)
    val offlineControlScore: StateFlow<Float> = _offlineControlScore.asStateFlow()

    private val _offlineVoicedTime = MutableStateFlow(0f)
    val offlineVoicedTime: StateFlow<Float> = _offlineVoicedTime.asStateFlow()

    private val _isAnalyzingOffline = MutableStateFlow(false)
    val isAnalyzingOffline: StateFlow<Boolean> = _isAnalyzingOffline.asStateFlow()

    // Private
    private var recorder: SonixRecorder? = null
    private var vad: CalibraVAD? = null
    private var recordPath: String? = null
    private var recordingJob: Job? = null
    private var levelJob: Job? = null

    fun startRecording(context: Context) {
        viewModelScope.launch {
            // Reset
            _isVoiceDetected.value = false
            _breathCapacity.value = null
            _controlScore.value = null
            _state.value = BreathMonitorState.RECORDING

            // Create audio resources
            recordPath = "${context.cacheDir}/breath_monitor.m4a"
            recorder?.release()
            recorder = SonixRecorder.create(recordPath!!, SonixRecorderConfig.VOICE)

            vad?.release()
            vad = CalibraVAD.create(VADModelProvider.singingRealtime())

            recorder?.start()

            // Level meter
            levelJob = launch {
                recorder?.level?.collect { level ->
                    _recordingLevel.value = level
                }
            }

            // VAD indicator (voice/silence feedback only, no auto-stop)
            recordingJob = launch {
                recorder?.audioBuffers?.collect { buffer ->
                    if (_state.value != BreathMonitorState.RECORDING) return@collect

                    val currentVad = vad ?: return@collect
                    val ratio = currentVad.getVADRatio(buffer.samples, 16000)
                    if (ratio >= 0) {
                        _isVoiceDetected.value = ratio > 0.5f
                    }
                }
            }
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            recordingJob?.cancel()
            levelJob?.cancel()
            recorder?.stop()

            val path = recordPath
            if (path != null) {
                _state.value = BreathMonitorState.ANALYZING
                analyzeRecording(path)
            } else {
                _state.value = BreathMonitorState.IDLE
            }

            recorder?.release()
            recorder = null
            vad?.release()
            vad = null
        }
    }

    private suspend fun analyzeRecording(path: String) {
        try {
            val audioData = withContext(Dispatchers.IO) { SonixDecoder.decode(path) }
            if (audioData != null) {
                val extractor = PitchDetection.createContourExtractor()
                val contour = withContext(Dispatchers.IO) {
                    extractor.extract(audioData.samples, audioData.sampleRate)
                }
                extractor.release()

                if (contour.size >= 2) {
                    val metrics = TesseraBreath.analyze(contour, config = BreathConfig.PRACTICE)
                    _breathCapacity.value = metrics.phrases?.longestDuration
                    _controlScore.value = metrics.controlScore
                }
            }
        } catch (e: Exception) {
            Napier.e("Recording analysis failed", e)
        }
        _state.value = BreathMonitorState.COMPLETE
    }

    fun reset() {
        _state.value = BreathMonitorState.IDLE
        _breathCapacity.value = null
        _controlScore.value = null
        _isVoiceDetected.value = false
    }

    // Offline analysis of bundled audio
    fun analyzeOffline(context: Context) {
        viewModelScope.launch {
            _isAnalyzingOffline.value = true
            _offlineBreathCapacity.value = 0f
            _offlineControlScore.value = 0f
            _offlineVoicedTime.value = 0f

            try {
                val audioFile = withContext(Dispatchers.IO) {
                    copyAssetToFile(context, "Alankaar 01_voice.m4a")
                }

                val audioData = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(audioFile.absolutePath)
                }

                if (audioData == null) {
                    Napier.e("Failed to decode audio file")
                    _isAnalyzingOffline.value = false
                    return@launch
                }

                val extractor = PitchDetection.createContourExtractor()
                val contour = withContext(Dispatchers.IO) {
                    extractor.extract(audioData.samples, audioData.sampleRate)
                }
                extractor.release()

                if (contour.size >= 2) {
                    val metrics = TesseraBreath.analyze(contour, config = BreathConfig.PRACTICE)
                    _offlineBreathCapacity.value = metrics.phrases?.longestDuration ?: 0f
                    _offlineControlScore.value = metrics.controlScore ?: 0f

                    val pitches = contour.pitchesHz
                    val times = contour.times
                    if (times.size >= 2) {
                        val sr = 1f / (times[1] - times[0])
                        val voicedCount = pitches.count { it > 0f }
                        _offlineVoicedTime.value = voicedCount / sr
                    }
                }
            } catch (e: Exception) {
                Napier.e("Offline analysis failed", e)
            } finally {
                _isAnalyzingOffline.value = false
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

    override fun onCleared() {
        super.onCleared()
        recordingJob?.cancel()
        levelJob?.cancel()
        recorder?.release()
        vad?.release()
    }

    companion object {
        fun formatTime(seconds: Float): String {
            val mins = (seconds / 60).toInt()
            val secs = seconds % 60
            return if (mins > 0) {
                "%d:%05.2f".format(mins, secs)
            } else {
                "%.2f".format(secs)
            }
        }
    }
}
