package com.musicmuni.voxatrace.demo.sections.effects.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraEffects
import com.musicmuni.voxatrace.calibra.CalibraEffectsConfig
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

/**
 * ViewModel for audio effects using CalibraEffectsConfig.Builder.
 *
 * ## VoxaTrace Integration
 * ```kotlin
 * // 1. Build effects config and create chain (ADR-001: Builder builds Config)
 * val config = CalibraEffectsConfig.Builder()
 *     .addNoiseGate(thresholdDb = threshold, holdTimeMs = 100f, timeConstMs = 10f)
 *     .addCompressor(thresholdDb = threshold, ratio = ratio)
 *     .addReverb(mix = mix, roomSize = roomSize)
 *     .build()
 * effects = CalibraEffects.create(config)
 *
 * // 2. Apply to audio (via SonixPlayer processing tap)
 * player.setProcessingTap { samples ->
 *     effects.process(samples)
 * }
 *
 * // 3. Adjust parameters in realtime
 * effects.setReverbMix(mix)
 * effects.setCompressorThreshold(thresholdDb)
 * ```
 */
class EffectsViewModel : ViewModel() {

    // Published State
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _status = MutableStateFlow("Ready")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _selectedPresetIndex = MutableStateFlow(0)
    val selectedPresetIndex: StateFlow<Int> = _selectedPresetIndex.asStateFlow()

    // Effect parameters
    private val _reverbMix = MutableStateFlow(0.3f)
    val reverbMix: StateFlow<Float> = _reverbMix.asStateFlow()

    private val _reverbRoomSize = MutableStateFlow(0.5f)
    val reverbRoomSize: StateFlow<Float> = _reverbRoomSize.asStateFlow()

    private val _compressorThreshold = MutableStateFlow(-20f)
    val compressorThreshold: StateFlow<Float> = _compressorThreshold.asStateFlow()

    private val _compressorRatio = MutableStateFlow(4f)
    val compressorRatio: StateFlow<Float> = _compressorRatio.asStateFlow()

    private val _noiseGateThreshold = MutableStateFlow(-40f)
    val noiseGateThreshold: StateFlow<Float> = _noiseGateThreshold.asStateFlow()

    val presetNames = listOf("Vocal Chain", "Practice", "Recording", "Dry", "Wet", "Clean")

    // Private
    private var player: SonixPlayer? = null
    private var recorder: SonixRecorder? = null
    private var effects: CalibraEffects? = null
    private var audioTask: Job? = null
    private var audioFilePath: String? = null

    // Actions

    fun setSelectedPresetIndex(index: Int) {
        _selectedPresetIndex.value = index
        applyPreset()
    }

    fun toggleRecording(context: Context) {
        if (_isRecording.value) {
            stopRecording()
        } else {
            startRecording(context)
        }
    }

    fun togglePlayback(context: Context) {
        if (_isPlaying.value) {
            pausePlayback()
        } else {
            startPlayback(context)
        }
    }

    fun updateReverbMix(value: Float) {
        _reverbMix.value = value
        effects?.setReverbMix(value)
    }

    fun updateReverbRoomSize(value: Float) {
        _reverbRoomSize.value = value
        effects?.setReverbRoomSize(value)
    }

    fun updateCompressorThreshold(value: Float) {
        _compressorThreshold.value = value
        effects?.setCompressorThreshold(value)
    }

    fun updateCompressorRatio(value: Float) {
        _compressorRatio.value = value
        effects?.setCompressorRatio(value)
    }

    fun updateNoiseGateThreshold(value: Float) {
        _noiseGateThreshold.value = value
        effects?.setNoiseGateThreshold(value)
    }

    fun onDisappear() {
        cleanupAudio()
    }

    // Private Methods

    private fun applyPreset() {
        when (_selectedPresetIndex.value) {
            0 -> { // Vocal Chain
                _noiseGateThreshold.value = -45f
                _compressorThreshold.value = -20f
                _compressorRatio.value = 3f
                _reverbMix.value = 0.25f
                _reverbRoomSize.value = 0.5f
            }
            1 -> { // Practice
                _noiseGateThreshold.value = -50f
                _compressorThreshold.value = -18f
                _compressorRatio.value = 2f
                _reverbMix.value = 0f
                _reverbRoomSize.value = 0.3f
            }
            2 -> { // Recording
                _noiseGateThreshold.value = -40f
                _compressorThreshold.value = -24f
                _compressorRatio.value = 6f
                _reverbMix.value = 0.15f
                _reverbRoomSize.value = 0.4f
            }
            3 -> { // Dry
                _noiseGateThreshold.value = -80f
                _compressorThreshold.value = -20f
                _compressorRatio.value = 4f
                _reverbMix.value = 0f
                _reverbRoomSize.value = 0f
            }
            4 -> { // Wet
                _noiseGateThreshold.value = -80f
                _compressorThreshold.value = -20f
                _compressorRatio.value = 1f
                _reverbMix.value = 0.5f
                _reverbRoomSize.value = 0.7f
            }
            5 -> { // Clean
                _noiseGateThreshold.value = -40f
                _compressorThreshold.value = -60f
                _compressorRatio.value = 1f
                _reverbMix.value = 0f
                _reverbRoomSize.value = 0f
            }
        }
        rebuildEffectsIfNeeded()
    }

    private fun setupAudioIfNeeded(context: Context) {
        if (effects == null) {
            val config = CalibraEffectsConfig.Builder()
                .addNoiseGate(
                    thresholdDb = _noiseGateThreshold.value,
                    holdTimeMs = 100f,
                    timeConstMs = 10f
                )
                .addCompressor(
                    thresholdDb = _compressorThreshold.value,
                    ratio = _compressorRatio.value,
                    attackMs = 10f,
                    releaseMs = 100f,
                    autoMakeup = false,
                    makeupDb = 0f
                )
                .addReverb(mix = _reverbMix.value, roomSize = _reverbRoomSize.value)
                .build()
            effects = CalibraEffects.create(config)
        }

        if (audioFilePath == null) {
            audioFilePath = File(context.cacheDir, "effects_recording.wav").absolutePath
        }
    }

    private fun rebuildEffectsIfNeeded() {
        if (effects == null) return

        val oldEffects = effects

        val config = CalibraEffectsConfig.Builder()
            .addNoiseGate(
                thresholdDb = _noiseGateThreshold.value,
                holdTimeMs = 100f,
                timeConstMs = 10f
            )
            .addCompressor(
                thresholdDb = _compressorThreshold.value,
                ratio = _compressorRatio.value,
                attackMs = 10f,
                releaseMs = 100f,
                autoMakeup = false,
                makeupDb = 0f
            )
            .addReverb(mix = _reverbMix.value, roomSize = _reverbRoomSize.value)
            .build()
        effects = CalibraEffects.create(config)

        // Reinstall processing tap if playing
        if (_isPlaying.value) {
            val newEffects = effects
            player?.setProcessingTap { samples ->
                newEffects?.process(samples)
            }
        }

        oldEffects?.release()
    }

    private fun cleanupAudio() {
        audioTask?.cancel()
        audioTask = null
        player?.setProcessingTap(null)
        player?.stop()
        player?.release()
        player = null

        recorder?.stop()
        recorder?.release()
        recorder = null

        effects?.release()
        effects = null
    }

    private fun startRecording(context: Context) {
        _isRecording.value = true
        _status.value = "Setting up..."

        viewModelScope.launch {
            setupAudioIfNeeded(context)

            val recordPath = audioFilePath ?: return@launch
            recorder?.release()
            recorder = SonixRecorder.create(recordPath, SonixRecorderConfig.VOICE)

            val rec = recorder
            if (rec == null) {
                _isRecording.value = false
                _status.value = "Setup failed"
                return@launch
            }

            _status.value = "Recording..."

            rec.start()

            audioTask = launch {
                rec.audioBuffers.collect { _ ->
                    // Just collect to keep recording going
                }
            }
        }
    }

    private fun stopRecording() {
        audioTask?.cancel()
        audioTask = null
        recorder?.stop()
        _isRecording.value = false

        // Release player so it reloads the new recording
        player?.stop()
        player?.release()
        player = null

        _status.value = "Recording saved"
    }

    private fun startPlayback(context: Context) {
        setupAudioIfNeeded(context)

        val audioPath = audioFilePath
        if (audioPath == null || !File(audioPath).exists()) {
            _status.value = "No recording found. Record first!"
            return
        }

        _status.value = "Loading..."

        viewModelScope.launch {
            try {
                if (player == null) {
                    player = SonixPlayer.create(audioPath)
                }

                // Install processing tap for real-time effects
                val effectsChain = effects
                player?.setProcessingTap { samples ->
                    effectsChain?.process(samples)
                }

                player?.play()
                _isPlaying.value = true
                _status.value = "Playing with effects..."

            } catch (e: Exception) {
                _status.value = "Error: ${e.message}"
            }
        }
    }

    private fun pausePlayback() {
        player?.pause()
        _isPlaying.value = false
        _status.value = "Paused"
    }

    override fun onCleared() {
        super.onCleared()
        cleanupAudio()
    }
}
