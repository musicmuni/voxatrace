package com.musicmuni.voxatrace.demo.sections.playback.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.sonix.AudioMode
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.SonixPlayerConfig
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
 * ViewModel for audio playback using SonixPlayer.
 *
 * ## VoxaTrace Integration (~20 lines)
 * ```kotlin
 * // 1. Create player with Config + Factory pattern
 * val config = SonixPlayerConfig.Builder()
 *     .volume(0.8f)
 *     .pitch(-2f)
 *     .loopCount(3)
 *     .onComplete { }
 *     .onLoopComplete { loopIndex, totalLoops -> }
 *     .onError { }
 *     .build()
 * val player = SonixPlayer.create(source, config, audioSession = AudioMode.PLAYBACK)
 *
 * // 2. Observe state via StateFlows
 * player.isPlaying.collect { }
 * player.currentTime.collect { }
 *
 * // 3. Control playback
 * player.play() / player.pause() / player.stop() / player.seek(ms)
 * player.volume = 0.5f / player.pitch = 2f
 * ```
 */
class PlaybackViewModel : ViewModel() {

    // MARK: - Published State

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentTimeMs = MutableStateFlow(0L)
    val currentTimeMs: StateFlow<Long> = _currentTimeMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _volume = MutableStateFlow(0.8f)
    val volume: StateFlow<Float> = _volume.asStateFlow()

    private val _pitch = MutableStateFlow(0f)
    val pitch: StateFlow<Float> = _pitch.asStateFlow()

    private val _loopCount = MutableStateFlow(1)
    val loopCount: StateFlow<Int> = _loopCount.asStateFlow()

    private val _status = MutableStateFlow("Ready")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded.asStateFlow()

    // MARK: - Private State

    private var player: SonixPlayer? = null
    private val observerJobs = mutableListOf<Job>()

    // MARK: - Actions

    fun initialize(context: Context) {
        if (_isLoaded.value) return

        viewModelScope.launch {
            try {
                _status.value = "Loading..."

                // Copy asset to file on IO dispatcher
                val assetFile = withContext(Dispatchers.IO) {
                    copyAssetToFile(context, "sample.m4a")
                }

                // Create player using Config + Factory pattern (ADR-001)
                val playerConfig = SonixPlayerConfig.Builder()
                    .volume(_volume.value)
                    .pitch(_pitch.value)
                    .loopCount(_loopCount.value)
                    .onComplete {
                        Napier.d("Playback completed!")
                        _status.value = "Playback complete"
                    }
                    .onLoopComplete { loopIndex, totalLoops ->
                        Napier.d("Loop $loopIndex of $totalLoops completed")
                    }
                    .onError { error ->
                        Napier.e("Playback error: $error")
                        _status.value = "Error: $error"
                    }
                    .build()

                val newPlayer = SonixPlayer.create(
                    source = assetFile.absolutePath,
                    config = playerConfig,
                    audioSession = AudioMode.PLAYBACK
                )

                player = newPlayer
                _durationMs.value = newPlayer.duration
                _isLoaded.value = true
                _status.value = "Loaded: sample.m4a"
                Napier.d("Audio loaded, duration: ${newPlayer.duration} ms")

                setupObservers(newPlayer)
            } catch (e: Exception) {
                Napier.e("Player init failed", e)
                _status.value = "Error: ${e.message}"
            }
        }
    }

    fun play() {
        player?.play()
    }

    fun pause() {
        player?.pause()
    }

    fun stop() {
        player?.stop()
        _currentTimeMs.value = 0
    }

    fun seek(fraction: Float) {
        val seekPos = (fraction * _durationMs.value).toLong()
        player?.seek(seekPos)
    }

    fun setVolume(newVolume: Float) {
        _volume.value = newVolume
        player?.let { it.volume = newVolume }
    }

    fun setPitch(newPitch: Float) {
        _pitch.value = newPitch
        player?.let { it.pitch = newPitch }
    }

    fun setLoopCount(count: Int) {
        _loopCount.value = count
        player?.let { it.loopCount = count }
    }

    fun fadeIn() {
        viewModelScope.launch {
            player?.fadeIn(1.0f, 1000)
        }
    }

    fun fadeOut() {
        viewModelScope.launch {
            player?.fadeOut(1000)
        }
    }

    // MARK: - Private Methods

    private fun setupObservers(player: SonixPlayer) {
        observerJobs.add(viewModelScope.launch {
            player.isPlaying.collect { playing ->
                _isPlaying.value = playing
            }
        })

        observerJobs.add(viewModelScope.launch {
            player.currentTime.collect { time ->
                _currentTimeMs.value = time
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        observerJobs.forEach { it.cancel() }
        player?.release()
    }

    // MARK: - Helpers

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
