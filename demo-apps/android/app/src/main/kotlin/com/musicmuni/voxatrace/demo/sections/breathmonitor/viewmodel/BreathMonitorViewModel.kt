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

private const val PREFS_NAME = "breath_monitor_prefs"
private const val KEY_BEST_SCORE = "best_score"
private const val SILENCE_GRACE_MS = 500L

enum class BreathMonitorState {
    IDLE,
    WAITING_FOR_VOICE,
    SINGING,
    COMPLETE
}

/**
 * ViewModel for Breath Monitor - duration tracking with VAD and silence inertia.
 *
 * APIs demonstrated:
 * - CalibraVAD (singingRealtime) for real-time voice detection
 * - TesseraBreath.computeScore() for offline breath capacity + control scoring
 * - PitchDetection.createContourExtractor() for pitch extraction
 */
class BreathMonitorViewModel : ViewModel() {

    // Published state
    private val _monitoringState = MutableStateFlow(BreathMonitorState.IDLE)
    val monitoringState: StateFlow<BreathMonitorState> = _monitoringState.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0f)
    val elapsedSeconds: StateFlow<Float> = _elapsedSeconds.asStateFlow()

    private val _bestScore = MutableStateFlow(0f)
    val bestScore: StateFlow<Float> = _bestScore.asStateFlow()

    private val _isVoiceDetected = MutableStateFlow(false)
    val isVoiceDetected: StateFlow<Boolean> = _isVoiceDetected.asStateFlow()

    private val _recordingLevel = MutableStateFlow(0f)
    val recordingLevel: StateFlow<Float> = _recordingLevel.asStateFlow()

    private val _status = MutableStateFlow("Hold a note as long as you can!")
    val status: StateFlow<String> = _status.asStateFlow()

    // Offline analysis state
    private val _offlineBreathCapacity = MutableStateFlow(0f)
    val offlineBreathCapacity: StateFlow<Float> = _offlineBreathCapacity.asStateFlow()

    private val _offlineControlScore = MutableStateFlow(0f)
    val offlineControlScore: StateFlow<Float> = _offlineControlScore.asStateFlow()

    private val _offlineVoicedTime = MutableStateFlow(0f)
    val offlineVoicedTime: StateFlow<Float> = _offlineVoicedTime.asStateFlow()

    private val _offlineHasEnoughData = MutableStateFlow(false)
    val offlineHasEnoughData: StateFlow<Boolean> = _offlineHasEnoughData.asStateFlow()

    private val _isAnalyzingOffline = MutableStateFlow(false)
    val isAnalyzingOffline: StateFlow<Boolean> = _isAnalyzingOffline.asStateFlow()

    // Private state
    private var recorder: SonixRecorder? = null
    private var vad: CalibraVAD? = null
    private var startTimeMs: Long = 0L
    private var lastVoiceTimeMs: Long = 0L
    private var recordingJob: Job? = null
    private var levelJob: Job? = null

    // Actions

    fun loadBestScore(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _bestScore.value = prefs.getFloat(KEY_BEST_SCORE, 0f)
    }

    fun startMonitoring(context: Context) {
        viewModelScope.launch {
            // Reset state
            _elapsedSeconds.value = 0f
            _isVoiceDetected.value = false
            _monitoringState.value = BreathMonitorState.WAITING_FOR_VOICE
            _status.value = "Start singing when ready..."

            // Create audio resources
            val recordPath = "${context.cacheDir}/breath_monitor.m4a"
            recorder?.release()
            recorder = SonixRecorder.create(recordPath, SonixRecorderConfig.VOICE)

            vad?.release()
            vad = CalibraVAD.create(VADModelProvider.singingRealtime())

            // Start recording
            recorder?.start()

            // Collect level for meter
            levelJob = launch {
                recorder?.level?.collect { level ->
                    _recordingLevel.value = level
                }
            }

            // Main detection loop using VAD (matching iOS pattern)
            recordingJob = launch {
                recorder?.audioBuffers?.collect { buffer ->
                    if (_monitoringState.value == BreathMonitorState.IDLE ||
                        _monitoringState.value == BreathMonitorState.COMPLETE
                    ) {
                        return@collect
                    }

                    val currentVad = vad ?: return@collect

                    // ADR-017: VOICE preset records at 16kHz; CalibraVAD handles resampling internally
                    val ratio = currentVad.getVADRatio(buffer.samples, 16000)
                    if (ratio < 0) return@collect

                    val hasVoice = ratio > 0.5f
                    val currentTimeMs = System.currentTimeMillis()

                    when (_monitoringState.value) {
                        BreathMonitorState.WAITING_FOR_VOICE -> {
                            if (hasVoice) {
                                _monitoringState.value = BreathMonitorState.SINGING
                                startTimeMs = currentTimeMs
                                lastVoiceTimeMs = currentTimeMs
                                _isVoiceDetected.value = true
                                _status.value = "Keep going!"
                            }
                        }
                        BreathMonitorState.SINGING -> {
                            if (hasVoice) {
                                lastVoiceTimeMs = currentTimeMs
                                _isVoiceDetected.value = true
                            } else {
                                _isVoiceDetected.value = false
                                val silenceDuration = currentTimeMs - lastVoiceTimeMs

                                if (silenceDuration > SILENCE_GRACE_MS) {
                                    _monitoringState.value = BreathMonitorState.COMPLETE
                                    _elapsedSeconds.value = (lastVoiceTimeMs - startTimeMs) / 1000f

                                    if (_elapsedSeconds.value > _bestScore.value) {
                                        _bestScore.value = _elapsedSeconds.value
                                        saveBestScore(context, _bestScore.value)
                                        _status.value = "New record! ${formatTime(_elapsedSeconds.value)}"
                                    } else {
                                        _status.value = "Good try! ${formatTime(_elapsedSeconds.value)}"
                                    }

                                    recorder?.stop()
                                }
                            }
                        }
                        else -> {}
                    }

                    if (_monitoringState.value == BreathMonitorState.SINGING) {
                        _elapsedSeconds.value = (currentTimeMs - startTimeMs) / 1000f
                    }
                }
            }
        }
    }

    fun stopMonitoring() {
        viewModelScope.launch {
            recordingJob?.cancel()
            levelJob?.cancel()
            recorder?.stop()
            recorder?.release()
            recorder = null
            vad?.reset()
            _monitoringState.value = BreathMonitorState.IDLE
            _status.value = "Hold a note as long as you can!"
        }
    }

    fun analyzeOffline(context: Context) {
        viewModelScope.launch {
            _isAnalyzingOffline.value = true
            _offlineBreathCapacity.value = 0f
            _offlineControlScore.value = 0f
            _offlineVoicedTime.value = 0f
            _offlineHasEnoughData.value = false

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

                // Extract pitch contour via tona (replaces CalibraPitch.createContourExtractor)
                val extractor = PitchDetection.createContourExtractor()
                val contour = withContext(Dispatchers.IO) {
                    extractor.extract(audioData.samples, audioData.sampleRate)
                }
                extractor.close()

                // Compute breath metrics via tessera (replaces CalibraBreath)
                val hasEnough = contour.size >= 2
                if (hasEnough) {
                    val score = TesseraBreath.computeScore(contour)
                    _offlineBreathCapacity.value = score.capacity
                    _offlineControlScore.value = score.controlScore

                    // Voiced time from contour
                    val pitches = contour.pitchesHz
                    val times = contour.times
                    if (times.size >= 2) {
                        val sr = 1f / (times[1] - times[0])
                        val voicedCount = pitches.count { it > 0f }
                        _offlineVoicedTime.value = voicedCount / sr
                    }
                }
                _offlineHasEnoughData.value = hasEnough

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

    fun resetBestScore(context: Context) {
        _bestScore.value = 0f
        saveBestScore(context, 0f)
    }

    private fun saveBestScore(context: Context, score: Float) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putFloat(KEY_BEST_SCORE, score).apply()
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
