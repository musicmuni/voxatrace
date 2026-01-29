package com.musicmuni.voxatrace.demo.sections.breathmonitor.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.model.PitchDetectorConfig
import com.musicmuni.voxatrace.sonix.AudioSessionManager
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import com.musicmuni.voxatrace.sonix.SonixResampler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val PREFS_NAME = "breath_monitor_prefs"
private const val KEY_BEST_SCORE = "best_score"
private const val RMS_THRESHOLD = 0.01f
private const val SILENCE_GRACE_MS = 500L

enum class BreathMonitorState {
    IDLE,
    WAITING_FOR_VOICE,
    SINGING,
    COMPLETE
}

/**
 * ViewModel for Breath Monitor - duration tracking with VAD and silence inertia.
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

    // Private state
    private var recorder: SonixRecorder? = null
    private var detector: CalibraPitch.Detector? = null
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

            detector?.release()
            detector = CalibraPitch.createDetector(PitchDetectorConfig.BALANCED)

            // Start recording
            recorder?.start()

            // Collect level for meter
            levelJob = launch {
                recorder?.level?.collect { level ->
                    _recordingLevel.value = level
                }
            }

            // Main detection loop
            recordingJob = launch {
                recorder?.audioBuffers?.collect { buffer ->
                    if (_monitoringState.value == BreathMonitorState.IDLE ||
                        _monitoringState.value == BreathMonitorState.COMPLETE
                    ) {
                        return@collect
                    }

                    val det = detector ?: return@collect

                    // Resample to 16kHz for pitch detection
                    val hwRate = AudioSessionManager.hardwareSampleRate.toInt()
                    val samples16k = SonixResampler.resample(
                        samples = buffer.samples,
                        fromRate = hwRate,
                        toRate = 16000
                    )

                    // Detect pitch using high-level API
                    val result = det.detect(samples16k, 16000)
                    val pitch = result.pitch

                    // Voice is detected if pitch is valid and level is above threshold
                    val hasVoice = pitch > 0 && _recordingLevel.value > RMS_THRESHOLD
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

                                    // Update best score if needed
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

                    // Update elapsed time during singing
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
            _monitoringState.value = BreathMonitorState.IDLE
            _status.value = "Hold a note as long as you can!"
        }
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
        detector?.release()
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
