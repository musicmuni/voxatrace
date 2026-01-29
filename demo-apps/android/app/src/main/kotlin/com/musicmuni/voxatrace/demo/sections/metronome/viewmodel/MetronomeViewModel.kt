package com.musicmuni.voxatrace.demo.sections.metronome.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.sonix.SonixMetronome
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
 * ViewModel for metronome using SonixMetronome.
 */
class MetronomeViewModel : ViewModel() {

    // MARK: - Published State

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _bpm = MutableStateFlow(120f)
    val bpm: StateFlow<Float> = _bpm.asStateFlow()

    private val _beatsPerCycle = MutableStateFlow(4)
    val beatsPerCycle: StateFlow<Int> = _beatsPerCycle.asStateFlow()

    private val _currentBeat = MutableStateFlow(0)
    val currentBeat: StateFlow<Int> = _currentBeat.asStateFlow()

    private val _volume = MutableStateFlow(0.8f)
    val volume: StateFlow<Float> = _volume.asStateFlow()

    private val _status = MutableStateFlow("Ready")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    // MARK: - Private State

    private var metronome: SonixMetronome? = null
    private var samaSamplePath: String? = null
    private var beatSamplePath: String? = null
    private val observerJobs = mutableListOf<Job>()

    // MARK: - Actions

    fun initialize(context: Context) {
        if (_isInitialized.value) return

        viewModelScope.launch {
            try {
                _status.value = "Initializing..."

                // Copy files on IO dispatcher
                val (samaPath, beatPath) = withContext(Dispatchers.IO) {
                    val samaFile = copyAssetToFile(context, "sama_click.wav")
                    val beatFile = copyAssetToFile(context, "beat_click.wav")
                    Pair(samaFile.absolutePath, beatFile.absolutePath)
                }

                samaSamplePath = samaPath
                beatSamplePath = beatPath

                createMetronome(samaPath, beatPath)
            } catch (e: Exception) {
                Napier.e("Metronome init failed", e)
                _status.value = "Error: ${e.message}"
            }
        }
    }

    private fun createMetronome(samaPath: String, beatPath: String) {
        // Clean up existing
        observerJobs.forEach { it.cancel() }
        observerJobs.clear()
        metronome?.release()

        val newMetronome = SonixMetronome.Builder()
            .samaSamplePath(samaPath)
            .beatSamplePath(beatPath)
            .bpm(_bpm.value)
            .beatsPerCycle(_beatsPerCycle.value)
            .volume(_volume.value)
            .onBeat { beat ->
                Napier.d("Beat $beat")
            }
            .onError { error ->
                Napier.e("Metronome error: $error")
                _status.value = "Error: $error"
            }
            .build()

        metronome = newMetronome

        setupObservers(newMetronome)
    }

    private fun setupObservers(metronome: SonixMetronome) {
        observerJobs.add(viewModelScope.launch {
            metronome.currentBeat.collect { beat ->
                _currentBeat.value = beat
            }
        })

        observerJobs.add(viewModelScope.launch {
            metronome.isPlaying.collect { playing ->
                _isRunning.value = playing
            }
        })

        observerJobs.add(viewModelScope.launch {
            metronome.isInitialized.collect { initialized ->
                _isInitialized.value = initialized
                if (initialized) {
                    _status.value = "Initialized"
                }
            }
        })

        observerJobs.add(viewModelScope.launch {
            metronome.error.collect { error ->
                error?.let {
                    _status.value = "Error: ${it.message}"
                }
            }
        })
    }

    fun start() {
        metronome?.start()
        _status.value = "Running"
    }

    fun stop() {
        metronome?.stop()
        _status.value = "Stopped"
        _currentBeat.value = 0
    }

    fun setBpm(newBpm: Float) {
        _bpm.value = newBpm
        metronome?.setBpm(newBpm)
    }

    fun setVolume(newVolume: Float) {
        _volume.value = newVolume
        metronome?.let { it.volume = newVolume }
    }

    fun setBeatsPerCycle(count: Int) {
        if (_beatsPerCycle.value == count) return
        _beatsPerCycle.value = count

        val samaPath = samaSamplePath
        val beatPath = beatSamplePath
        if (samaPath != null && beatPath != null) {
            createMetronome(samaPath, beatPath)
        }
    }

    override fun onCleared() {
        super.onCleared()
        observerJobs.forEach { it.cancel() }
        metronome?.release()
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
