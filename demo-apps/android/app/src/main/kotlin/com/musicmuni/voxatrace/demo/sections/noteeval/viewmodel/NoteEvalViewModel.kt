package com.musicmuni.voxatrace.demo.sections.noteeval.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraMusic
import com.musicmuni.voxatrace.calibra.CalibraNoteEval
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.ExercisePattern
import com.musicmuni.voxatrace.calibra.ExerciseResult
import com.musicmuni.voxatrace.calibra.NoteResult
import com.musicmuni.voxatrace.calibra.model.NoteEvalPreset
import com.musicmuni.voxatrace.sonix.AudioMode
import com.musicmuni.voxatrace.sonix.SonixMidiSynthesizer
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.SonixPlayerConfig
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import com.musicmuni.voxatrace.sonix.midi.MidiNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.sqrt

/**
 * Exercise definition for note evaluation.
 */
data class ExerciseInfo(
    val name: String,
    val midiNotes: List<Int>,
    val keyMidi: Int
)

/**
 * ViewModel for note/exercise evaluation using CalibraNoteEval with singalong mode.
 *
 * ## VoxaTrace Integration
 * ```kotlin
 * // 1. Synthesize reference notes
 * val synth = SonixMidiSynthesizer.Builder().soundFontPath(sfPath).build()
 * synth.synthesizeFromNotes(notes = midiNotes, outputPath = outputPath)
 *
 * // 2. Play reference + record student simultaneously (singalong)
 * player = SonixPlayer.create(source = referencePath, audioSession = AudioMode.PLAY_AND_RECORD)
 * recorder = SonixRecorder.create(outputPath, config, audioSession = AudioMode.PLAY_AND_RECORD)
 *
 * // 3. Extract pitch and evaluate
 * val extractor = CalibraPitch.createContourExtractor()
 * val contour = extractor.extract(audio = studentAudio, sampleRate = 16000)
 * ```
 */
class NoteEvalViewModel : ViewModel() {

    // MARK: - Published State

    private val _selectedExercise = MutableStateFlow(0)
    val selectedExercise: StateFlow<Int> = _selectedExercise.asStateFlow()

    private val _isSingalongActive = MutableStateFlow(false)
    val isSingalongActive: StateFlow<Boolean> = _isSingalongActive.asStateFlow()

    private val _hasRecording = MutableStateFlow(false)
    val hasRecording: StateFlow<Boolean> = _hasRecording.asStateFlow()

    private val _recordingDuration = MutableStateFlow(0f)
    val recordingDuration: StateFlow<Float> = _recordingDuration.asStateFlow()

    private val _recordingLevel = MutableStateFlow(0f)
    val recordingLevel: StateFlow<Float> = _recordingLevel.asStateFlow()

    private val _isEvaluating = MutableStateFlow(false)
    val isEvaluating: StateFlow<Boolean> = _isEvaluating.asStateFlow()

    private val _result = MutableStateFlow<ExerciseResult?>(null)
    val result: StateFlow<ExerciseResult?> = _result.asStateFlow()

    private val _status = MutableStateFlow("Select an exercise and tap Singalong")
    val status: StateFlow<String> = _status.asStateFlow()

    // Synthesis state
    private val _isPreparing = MutableStateFlow(false)
    val isPreparing: StateFlow<Boolean> = _isPreparing.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    // Evaluation settings
    private val _selectedPreset = MutableStateFlow(NoteEvalPreset.BALANCED)
    val selectedPreset: StateFlow<NoteEvalPreset> = _selectedPreset.asStateFlow()

    private val _noteDurationMs = MutableStateFlow(1000)
    val noteDurationMs: StateFlow<Int> = _noteDurationMs.asStateFlow()

    /** Available note durations (0.5s to 2s) */
    val availableDurations = listOf(500, 1000, 1500, 2000)

    // Exercise definitions: (name, MIDI notes, key MIDI note)
    val exercises = listOf(
        ExerciseInfo("C Major Scale (ascending)", listOf(48, 50, 52, 53, 55, 57, 59, 60), 48),
        ExerciseInfo("C Major Scale (descending)", listOf(60, 59, 57, 55, 53, 52, 50, 48), 48),
        ExerciseInfo("C Minor Scale", listOf(48, 50, 51, 53, 55, 56, 58, 60), 48),
        ExerciseInfo("C Major Arpeggio", listOf(48, 52, 55, 60), 48),
        ExerciseInfo("G Major Scale", listOf(55, 57, 59, 60, 62, 64, 66, 67), 55),
        ExerciseInfo("Sa Re Ga (C)", listOf(48, 50, 52), 48),
        ExerciseInfo("Sa Re Ga Ma Pa (G)", listOf(55, 57, 59, 60, 62), 55)
    )

    // MARK: - Computed Properties

    val currentExerciseName: String
        get() = exercises[_selectedExercise.value].name

    val currentMidiNotes: List<Int>
        get() = exercises[_selectedExercise.value].midiNotes

    val currentKeyMidi: Int
        get() = exercises[_selectedExercise.value].keyMidi

    // MARK: - Private State

    private var recorder: SonixRecorder? = null
    private var collectedAudio = mutableListOf<Float>()
    private var recordingJob: Job? = null
    private var recordingSampleRate: Int = 16000

    // Singalong
    private var referencePlayer: SonixPlayer? = null
    private var playerObserverJob: Job? = null
    private var synthesizedOutputPath: String? = null
    private var lastSynthesizedExercise: Int = -1
    private var lastSynthesizedDuration: Int = -1

    // MARK: - Actions

    fun selectExercise(index: Int) {
        _selectedExercise.value = index
        _result.value = null
        _hasRecording.value = false
        // Mark reference as needing re-synthesis if exercise changed
        if (index != lastSynthesizedExercise) {
            _isReady.value = false
        }
        _status.value = "Selected: ${exercises[index].name}"
    }

    /** Update the evaluation preset */
    fun setPreset(preset: NoteEvalPreset) {
        _selectedPreset.value = preset
    }

    /** Update note duration and invalidate reference if needed */
    fun setNoteDuration(ms: Int) {
        _noteDurationMs.value = ms
        // Force re-synthesis with new duration
        if (ms != lastSynthesizedDuration) {
            _isReady.value = false
        }
    }

    // MARK: - Singalong Actions

    /** Prepare the singalong session by synthesizing reference audio */
    fun prepare(context: Context) {
        if (_isPreparing.value) return

        _isPreparing.value = true
        _status.value = "Preparing..."

        viewModelScope.launch {
            synthesizeReferenceIfNeeded(context)
            loadPlayerForSingalong()

            _isPreparing.value = false
            if (_isReady.value) {
                _status.value = "Ready! Tap Singalong to start."
            }
        }
    }

    /** Start singalong: play reference and record simultaneously */
    fun startSingalong(context: Context) {
        if (!_isReady.value) {
            // Auto-prepare and start
            viewModelScope.launch {
                prepare(context)
                // Wait for preparation to complete
                while (_isPreparing.value) {
                    kotlinx.coroutines.delay(100)
                }
                if (_isReady.value) {
                    startSingalongInternal(context)
                }
            }
            return
        }

        startSingalongInternal(context)
    }

    private fun startSingalongInternal(context: Context) {
        collectedAudio.clear()
        _hasRecording.value = false
        _result.value = null
        _recordingDuration.value = 0f
        _status.value = "Sing along with the notes..."

        // Create recorder for simultaneous playback+recording with echo cancellation
        val tempPath = "${context.cacheDir}/note_eval_temp.m4a"
        val recorderConfig = SonixRecorderConfig.Builder()
            .preset(SonixRecorderConfig.VOICE)
            .echoCancellation(true)
            .build()
        recorder = SonixRecorder.create(tempPath, recorderConfig, AudioMode.PLAY_AND_RECORD)

        _isSingalongActive.value = true

        // Start recorder first to get actual sample rate
        recorder?.start()

        // Capture actual sample rate (SDK handles resampling internally per ADR-017)
        recordingSampleRate = recorder?.actualSampleRate ?: 16000

        // Start collecting audio
        recordingJob = viewModelScope.launch {
            var sampleCount = 0

            recorder?.audioBuffers?.collect { buffer ->
                val samples = buffer.samples

                collectedAudio.addAll(samples.toList())
                sampleCount += samples.size

                // Calculate RMS for level meter
                val sum = samples.fold(0f) { acc, s -> acc + s * s }
                val rms = sqrt(sum / samples.size)

                _recordingLevel.value = (rms * 5).coerceAtMost(1f)
                _recordingDuration.value = sampleCount / recordingSampleRate.toFloat()
            }
        }

        // Start player
        referencePlayer?.play()
    }

    /** Stop singalong session and auto-evaluate */
    fun stopSingalong() {
        if (!_isSingalongActive.value) return  // Guard against double-calls
        _isSingalongActive.value = false  // Set FIRST to prevent observer re-trigger

        recordingJob?.cancel()
        recorder?.stop()
        referencePlayer?.stop()

        if (collectedAudio.isNotEmpty()) {
            _hasRecording.value = true
            _status.value = "Recording complete. Evaluating..."
            evaluate()
        } else {
            _status.value = "No audio recorded. Try again."
        }
    }

    // MARK: - Private Synthesis Methods

    private suspend fun synthesizeReferenceIfNeeded(context: Context) {
        // Skip if already synthesized for this exercise and duration
        val exercise = _selectedExercise.value
        val duration = _noteDurationMs.value
        val needsResynthesize = exercise != lastSynthesizedExercise ||
                duration != lastSynthesizedDuration
        if (!needsResynthesize) return

        val sfPath = withContext(Dispatchers.IO) {
            copyAssetToFile(context, "harmonium.sf2").absolutePath
        }

        val midiNotes = currentMidiNotes

        // Create MidiNote objects with proper timing (times in milliseconds as Float)
        val notes = midiNotes.mapIndexed { index, midi ->
            val startTime = index.toFloat() * duration
            val endTime = startTime + duration - 50 // Small gap between notes
            MidiNote(note = midi, startTime = startTime, endTime = endTime)
        }

        val outFile = File(context.cacheDir, "note_eval_reference.wav")

        val synth = SonixMidiSynthesizer.Builder()
            .soundFontPath(sfPath)
            .sampleRate(44100)
            .build()

        val success = withContext(Dispatchers.IO) {
            synth.synthesizeFromNotes(notes = notes, outputPath = outFile.absolutePath)
        }

        if (success) {
            synthesizedOutputPath = outFile.absolutePath
            lastSynthesizedExercise = exercise
            lastSynthesizedDuration = duration
        } else {
            _status.value = "Synthesis failed"
        }
    }

    private suspend fun loadPlayerForSingalong() {
        val outputPath = synthesizedOutputPath ?: return

        playerObserverJob?.cancel()
        referencePlayer?.release()

        try {
            // Use playAndRecord mode for simultaneous playback+recording
            val player = SonixPlayer.create(
                source = outputPath,
                config = SonixPlayerConfig.DEFAULT,
                audioSession = AudioMode.PLAY_AND_RECORD
            )
            referencePlayer = player

            // Auto-stop when playback finishes
            playerObserverJob = viewModelScope.launch {
                player.isPlaying.collect { playing ->
                    if (!playing && _isSingalongActive.value) {
                        stopSingalong()
                    }
                }
            }

            _isReady.value = true
        } catch (e: Exception) {
            _status.value = "Player error: ${e.message}"
        }
    }

    private fun evaluate() {
        if (collectedAudio.isEmpty()) {
            _status.value = "No recording to evaluate"
            return
        }

        _isEvaluating.value = true
        _status.value = "Evaluating..."

        viewModelScope.launch {
            val midiNotes = currentMidiNotes
            val keyMidi = currentKeyMidi
            val duration = _noteDurationMs.value
            val preset = _selectedPreset.value

            val studentAudio = collectedAudio.toFloatArray()
            val sampleRate = recordingSampleRate

            // SDK handles resampling internally (ADR-017)
            val extractor = CalibraPitch.createContourExtractor()
            val studentContour = withContext(Dispatchers.Default) {
                extractor.extract(studentAudio, sampleRate)
            }
            extractor.release()

            // Create exercise pattern from MIDI notes
            val pattern = ExercisePattern.fromMidiNotes(
                midiNotes = midiNotes,
                noteDurationMs = duration
            )

            // Get reference key frequency
            val keyHz = CalibraMusic.midiToHz(keyMidi.toFloat())

            // Evaluate using CalibraNoteEval
            val evalResult = CalibraNoteEval.evaluate(
                pattern = pattern,
                student = studentContour,
                referenceKeyHz = keyHz,
                preset = preset
            )

            _result.value = evalResult
            _isEvaluating.value = false
            _status.value = "Evaluation complete: ${evalResult.scorePercent}%"
        }
    }

    fun noteResult(index: Int): NoteResult? {
        return _result.value?.noteResults?.firstOrNull { it.noteIndex == index }
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
        playerObserverJob?.cancel()
        recorder?.stop()
        recorder?.release()
        referencePlayer?.release()
    }
}
