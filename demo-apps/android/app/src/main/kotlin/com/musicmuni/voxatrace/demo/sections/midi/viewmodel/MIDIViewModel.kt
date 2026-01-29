package com.musicmuni.voxatrace.demo.sections.midi.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.sonix.SonixMidiSynthesizer
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.midi.MidiNote
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * ViewModel for MIDI synthesis using SonixMidiSynthesizer.
 */
class MIDIViewModel : ViewModel() {

    // MARK: - Published State

    private val _status = MutableStateFlow("Ready")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _isSynthesizing = MutableStateFlow(false)
    val isSynthesizing: StateFlow<Boolean> = _isSynthesizing.asStateFlow()

    private val _outputFile = MutableStateFlow<File?>(null)
    val outputFile: StateFlow<File?> = _outputFile.asStateFlow()

    private val _synthesizerVersion = MutableStateFlow<String?>(null)
    val synthesizerVersion: StateFlow<String?> = _synthesizerVersion.asStateFlow()

    private val _selectedSampleRate = MutableStateFlow(44100)
    val selectedSampleRate: StateFlow<Int> = _selectedSampleRate.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    // MARK: - Private State

    private var player: SonixPlayer? = null
    private val observerJobs = mutableListOf<Job>()

    // MARK: - Actions

    fun setSelectedSampleRate(rate: Int) {
        if (!_isSynthesizing.value) {
            _selectedSampleRate.value = rate
        }
    }

    fun synthesize(context: Context) {
        viewModelScope.launch {
            _isSynthesizing.value = true
            _status.value = "Synthesizing (${_selectedSampleRate.value / 1000}kHz)..."

            try {
                // Copy soundfont to file
                val sfFile = withContext(Dispatchers.IO) {
                    copyAssetToFile(context, "harmonium.sf2")
                }

                // C major scale (timing in milliseconds)
                val notes = listOf(
                    MidiNote(note = 60, startTime = 0f, endTime = 400f),     // C4
                    MidiNote(note = 62, startTime = 500f, endTime = 900f),   // D4
                    MidiNote(note = 64, startTime = 1000f, endTime = 1400f), // E4
                    MidiNote(note = 65, startTime = 1500f, endTime = 1900f), // F4
                    MidiNote(note = 67, startTime = 2000f, endTime = 2400f), // G4
                    MidiNote(note = 69, startTime = 2500f, endTime = 2900f), // A4
                    MidiNote(note = 71, startTime = 3000f, endTime = 3400f), // B4
                    MidiNote(note = 72, startTime = 3500f, endTime = 4400f), // C5
                )

                val outFile = File(context.cacheDir, "midi_output.wav")

                val (success, version) = withContext(Dispatchers.IO) {
                    val synth = SonixMidiSynthesizer.Builder()
                        .soundFontPath(sfFile.absolutePath)
                        .sampleRate(_selectedSampleRate.value)
                        .onError { error ->
                            Napier.e("Synth error: $error")
                        }
                        .build()

                    val result = synth.synthesizeFromNotes(
                        notes = notes,
                        outputPath = outFile.absolutePath
                    )
                    Pair(result, synth.version)
                }

                if (!isActive) return@launch

                _synthesizerVersion.value = version
                if (success && outFile.exists() && outFile.length() > 0) {
                    _outputFile.value = outFile
                    _status.value = "Generated (${_selectedSampleRate.value / 1000}kHz): ${outFile.length() / 1024} KB"

                    // Load into SonixPlayer for playback
                    player?.release()
                    val newPlayer = SonixPlayer.create(outFile.absolutePath)
                    player = newPlayer

                    // Setup observer
                    observerJobs.add(viewModelScope.launch {
                        newPlayer.isPlaying.collect { playing ->
                            _isPlaying.value = playing
                        }
                    })
                } else {
                    _status.value = "Synthesis failed"
                }
            } catch (e: Exception) {
                Napier.e("MIDI synthesis failed", e)
                if (isActive) _status.value = "Error: ${e.message}"
            } finally {
                if (isActive) _isSynthesizing.value = false
            }
        }
    }

    fun play() {
        player?.play()
    }

    fun stop() {
        player?.stop()
    }

    override fun onCleared() {
        super.onCleared()
        observerJobs.forEach { it.cancel() }
        player?.release()
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
