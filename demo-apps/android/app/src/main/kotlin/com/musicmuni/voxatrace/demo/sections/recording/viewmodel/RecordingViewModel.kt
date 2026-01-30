package com.musicmuni.voxatrace.demo.sections.recording.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.SonixPlayerConfig
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

/**
 * ViewModel for audio recording using SonixRecorder.
 *
 * ## VoxaTrace Integration (~25 lines)
 * ```kotlin
 * // 1. Create recorder with Config + Factory pattern
 * val config = SonixRecorderConfig.Builder()
 *     .preset(SonixRecorderConfig.VOICE)
 *     .sampleRate(16000)
 *     .channels(1)
 *     .bitrate(128000)
 *     .onRecordingStarted { }
 *     .onRecordingStopped { }
 *     .onError { }
 *     .build()
 * val recorder = SonixRecorder.create(outputPath, config)
 *
 * // 2. Observe state via StateFlows
 * recorder.isRecording.collect { }
 * recorder.duration.collect { }
 * recorder.level.collect { }
 *
 * // 3. Control recording
 * recorder.start() / recorder.stop() / recorder.release()
 * ```
 */
class RecordingViewModel : ViewModel() {

    // MARK: - Published State

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _audioLevel = MutableStateFlow(0f)
    val audioLevel: StateFlow<Float> = _audioLevel.asStateFlow()

    private val _savedFilePath = MutableStateFlow<String?>(null)
    val savedFilePath: StateFlow<String?> = _savedFilePath.asStateFlow()

    private val _status = MutableStateFlow("Ready")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _selectedFormat = MutableStateFlow("m4a")
    val selectedFormat: StateFlow<String> = _selectedFormat.asStateFlow()

    // Buffer Pool metrics (for Calibra integration)
    private val _bufferPoolAvailable = MutableStateFlow(4)
    val bufferPoolAvailable: StateFlow<Int> = _bufferPoolAvailable.asStateFlow()

    private val _bufferPoolWasExhausted = MutableStateFlow(false)
    val bufferPoolWasExhausted: StateFlow<Boolean> = _bufferPoolWasExhausted.asStateFlow()

    // Playback state
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackTimeMs = MutableStateFlow(0L)
    val playbackTimeMs: StateFlow<Long> = _playbackTimeMs.asStateFlow()

    private val _playbackDurationMs = MutableStateFlow(0L)
    val playbackDurationMs: StateFlow<Long> = _playbackDurationMs.asStateFlow()

    // MARK: - Computed Properties

    val formats = listOf("m4a", "mp3")

    // MARK: - Private State

    private var recorder: SonixRecorder? = null
    private var player: SonixPlayer? = null
    private val observerJobs = mutableListOf<Job>()

    // MARK: - Actions

    fun setSelectedFormat(format: String) {
        if (!_isRecording.value) {
            _selectedFormat.value = format
        }
    }

    fun startRecording(context: Context) {
        viewModelScope.launch {
            _status.value = "Starting..."

            val outputDir = context.filesDir.absolutePath
            val timestamp = System.currentTimeMillis()
            val format = _selectedFormat.value
            val outputPath = "$outputDir/recording_$timestamp.$format"

            // Create recorder using Config + Factory pattern (ADR-001)
            // Note: Format is auto-detected from file extension (matches iOS behavior)
            val recorderConfig = SonixRecorderConfig.Builder()
                .preset(SonixRecorderConfig.VOICE)
                .sampleRate(16000)
                .channels(1)
                .bitrate(128000)
                .onRecordingStarted {
                    Napier.d("Recording started!")
                }
                .onRecordingStopped { path ->
                    Napier.d("Recording saved to: $path")
                }
                .onError { error ->
                    Napier.e("Recording error: $error")
                    _status.value = "Error: $error"
                }
                .build()

            val newRecorder = SonixRecorder.create(outputPath, recorderConfig)

            recorder = newRecorder
            _savedFilePath.value = outputPath

            setupRecorderObservers(newRecorder)

            newRecorder.start()
            _status.value = "Recording (${format.uppercase()})"
        }
    }

    fun stopRecording() {
        recorder?.stop()
        _isRecording.value = false
        _audioLevel.value = 0f
        _status.value = "Saved"
    }

    fun playRecording() {
        val path = _savedFilePath.value ?: return

        viewModelScope.launch {
            cancelPlayerObservers()
            player?.release()

            try {
                // Create player using Config + Factory pattern (ADR-001)
                val playerConfig = SonixPlayerConfig.Builder()
                    .volume(1.0f)
                    .onComplete {
                        _status.value = "Playback complete"
                        _isPlaying.value = false
                    }
                    .onError { error ->
                        _status.value = "Player error: $error"
                    }
                    .build()

                val newPlayer = SonixPlayer.create(path, playerConfig)
                player = newPlayer
                _playbackDurationMs.value = newPlayer.duration

                setupPlayerObservers(newPlayer)

                newPlayer.play()
                _status.value = "Playing recording"
            } catch (e: Exception) {
                _status.value = "Player error: ${e.message}"
            }
        }
    }

    fun pausePlayback() {
        player?.pause()
        _status.value = "Paused"
    }

    fun stopPlayback() {
        player?.stop()
        _playbackTimeMs.value = 0
        _status.value = "Stopped"
    }

    // MARK: - Private Methods

    private fun setupRecorderObservers(recorder: SonixRecorder) {
        cancelRecorderObservers()

        observerJobs.add(viewModelScope.launch {
            recorder.isRecording.collect { recording ->
                _isRecording.value = recording
            }
        })

        observerJobs.add(viewModelScope.launch {
            recorder.duration.collect { duration ->
                _durationMs.value = duration
            }
        })

        observerJobs.add(viewModelScope.launch {
            recorder.level.collect { level ->
                _audioLevel.value = level
                _bufferPoolAvailable.value = recorder.bufferPoolAvailable
                _bufferPoolWasExhausted.value = recorder.bufferPoolWasExhausted
            }
        })

        observerJobs.add(viewModelScope.launch {
            recorder.error.collect { error ->
                error?.let {
                    _status.value = "Error: ${it.message}"
                }
            }
        })
    }

    private fun setupPlayerObservers(player: SonixPlayer) {
        observerJobs.add(viewModelScope.launch {
            player.isPlaying.collect { playing ->
                _isPlaying.value = playing
            }
        })

        observerJobs.add(viewModelScope.launch {
            player.currentTime.collect { time ->
                _playbackTimeMs.value = time
            }
        })
    }

    private fun cancelRecorderObservers() {
        observerJobs.forEach { it.cancel() }
        observerJobs.clear()
    }

    private fun cancelPlayerObservers() {
        // Player observers are also in the same list, so we keep them for now
        // In a more complex app, we'd separate them
    }

    override fun onCleared() {
        super.onCleared()
        observerJobs.forEach { it.cancel() }
        recorder?.release()
        player?.release()
    }

    // MARK: - Helpers

    companion object {
        fun formatDuration(ms: Long): String {
            val seconds = (ms / 1000) % 60
            val minutes = (ms / 1000) / 60
            return String.format("%02d:%02d", minutes, seconds)
        }
    }
}
