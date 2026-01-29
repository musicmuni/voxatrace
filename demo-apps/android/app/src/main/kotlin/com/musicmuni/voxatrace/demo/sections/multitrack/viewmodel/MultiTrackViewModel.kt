package com.musicmuni.voxatrace.demo.sections.multitrack.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.sonix.SonixMixer
import com.musicmuni.voxatrace.sonix.SonixMixerConfig
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

/**
 * ViewModel for multi-track mixing using SonixMixer.
 */
class MultiTrackViewModel : ViewModel() {

    // MARK: - Published State

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentTimeMs = MutableStateFlow(0L)
    val currentTimeMs: StateFlow<Long> = _currentTimeMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _backingVolume = MutableStateFlow(0.8f)
    val backingVolume: StateFlow<Float> = _backingVolume.asStateFlow()

    private val _vocalVolume = MutableStateFlow(1.0f)
    val vocalVolume: StateFlow<Float> = _vocalVolume.asStateFlow()

    private val _status = MutableStateFlow("Ready")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded.asStateFlow()

    // MARK: - Private State

    private var mixer: SonixMixer? = null
    private val observerJobs = mutableListOf<Job>()

    // MARK: - Actions

    fun initialize(context: Context) {
        if (_isLoaded.value) return

        viewModelScope.launch {
            try {
                _status.value = "Loading tracks..."

                // Copy assets to files
                val (backingPath, vocalPath) = withContext(Dispatchers.IO) {
                    val backingFile = copyAssetToFile(context, "sample.m4a")
                    val vocalFile = copyAssetToFile(context, "vocal.m4a")
                    Pair(backingFile.absolutePath, vocalFile.absolutePath)
                }

                // Create mixer with Config + Factory pattern (ADR-001)
                val mixerConfig = SonixMixerConfig.Builder()
                    .loopCount(1)
                    .onPlaybackComplete {
                        Napier.d("Playback complete!")
                        _status.value = "Playback complete"
                    }
                    .onLoopComplete { loopIndex ->
                        Napier.d("Completed loop $loopIndex")
                    }
                    .onError { error ->
                        Napier.e("Playback error: $error")
                        _status.value = "Error: $error"
                    }
                    .build()
                val newMixer = SonixMixer.create(mixerConfig)

                mixer = newMixer

                // Add tracks
                val backingSuccess = newMixer.addTrack("backing", backingPath)
                val vocalSuccess = newMixer.addTrack("vocal", vocalPath)

                if (backingSuccess && vocalSuccess) {
                    _durationMs.value = newMixer.duration
                    _isLoaded.value = true
                    _status.value = "Loaded 2 tracks"
                    Napier.d("Multi-track loaded: duration=${newMixer.duration}ms")

                    setupObservers(newMixer)
                } else {
                    _status.value = "Failed to load tracks"
                }
            } catch (e: Exception) {
                Napier.e("MultiTrack init failed", e)
                _status.value = "Error: ${e.message}"
            }
        }
    }

    private fun setupObservers(mixer: SonixMixer) {
        observerJobs.add(viewModelScope.launch {
            mixer.isPlaying.collect { playing ->
                _isPlaying.value = playing
            }
        })

        observerJobs.add(viewModelScope.launch {
            mixer.currentTime.collect { time ->
                _currentTimeMs.value = time
            }
        })

        observerJobs.add(viewModelScope.launch {
            mixer.error.collect { error ->
                error?.let {
                    _status.value = "Error: ${it.message}"
                }
            }
        })
    }

    fun play() {
        mixer?.play()
    }

    fun pause() {
        mixer?.pause()
    }

    fun stop() {
        mixer?.stop()
        _currentTimeMs.value = 0
    }

    fun seek(fraction: Float) {
        val seekPos = (fraction * _durationMs.value).toLong()
        mixer?.seek(seekPos)
    }

    fun setBackingVolume(volume: Float) {
        _backingVolume.value = volume
        mixer?.setTrackVolume("backing", volume)
    }

    fun setVocalVolume(volume: Float) {
        _vocalVolume.value = volume
        mixer?.setTrackVolume("vocal", volume)
    }

    fun fadeVocalDown() {
        mixer?.fadeTrackVolume("vocal", 0.2f, 500)
        _vocalVolume.value = 0.2f
    }

    fun fadeVocalUp() {
        mixer?.fadeTrackVolume("vocal", 1.0f, 500)
        _vocalVolume.value = 1.0f
    }

    override fun onCleared() {
        super.onCleared()
        observerJobs.forEach { it.cancel() }
        mixer?.release()
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
        fun formatTime(ms: Long): String {
            val seconds = (ms / 1000) % 60
            val minutes = (ms / 1000) / 60
            return String.format("%d:%02d", minutes, seconds)
        }
    }
}
