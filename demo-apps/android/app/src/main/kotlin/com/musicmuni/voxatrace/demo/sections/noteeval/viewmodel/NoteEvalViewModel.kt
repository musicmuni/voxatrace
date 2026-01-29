package com.musicmuni.voxatrace.demo.sections.noteeval.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraMusic
import com.musicmuni.voxatrace.calibra.CalibraNoteEval
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.sonix.AudioSessionManager
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import com.musicmuni.voxatrace.sonix.SonixResampler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Exercise definition for note evaluation.
 */
data class ExerciseInfo(
    val name: String,
    val midiNotes: List<Int>,
    val keyMidi: Int
)

/**
 * ViewModel for note/exercise evaluation using CalibraNoteEval.
 *
 * ## VoxaTrace Integration (~20 lines)
 * ```kotlin
 * // 1. Create exercise pattern
 * val pattern = ExercisePattern.fromMidiNotes(midiNotes, noteDurationMs = 500)
 *
 * // 2. Record student performance
 * recorder = SonixRecorder.create(tempPath, SonixRecorderConfig.VOICE)
 *
 * // 3. Extract pitch and evaluate
 * val extractor = CalibraPitch.createContourExtractor()
 * val contour = extractor.extract(studentAudio, sampleRate = 16000)
 * val result = CalibraNoteEval.evaluate(pattern, student, referenceKeyHz, studentKeyHz, scoreType, leewaySamples)
 * ```
 */
class NoteEvalViewModel : ViewModel() {

    // MARK: - Published State

    private val _selectedExercise = MutableStateFlow(0)
    val selectedExercise: StateFlow<Int> = _selectedExercise.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _hasRecording = MutableStateFlow(false)
    val hasRecording: StateFlow<Boolean> = _hasRecording.asStateFlow()

    private val _recordingDuration = MutableStateFlow(0f)
    val recordingDuration: StateFlow<Float> = _recordingDuration.asStateFlow()

    private val _recordingLevel = MutableStateFlow(0f)
    val recordingLevel: StateFlow<Float> = _recordingLevel.asStateFlow()

    private val _isEvaluating = MutableStateFlow(false)
    val isEvaluating: StateFlow<Boolean> = _isEvaluating.asStateFlow()

    private val _score = MutableStateFlow(0f)
    val score: StateFlow<Float> = _score.asStateFlow()

    private val _status = MutableStateFlow("Select an exercise and record")
    val status: StateFlow<String> = _status.asStateFlow()

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

    // MARK: - Actions

    fun selectExercise(index: Int) {
        _selectedExercise.value = index
        _score.value = 0f
        _hasRecording.value = false
        _status.value = "Selected: ${exercises[index].name}"
    }

    fun startRecording(context: Context) {
        collectedAudio.clear()
        _hasRecording.value = false
        _score.value = 0f
        _recordingDuration.value = 0f
        _status.value = "Recording..."

        val tempPath = "${context.cacheDir}/note_eval_temp.m4a"
        recorder = SonixRecorder.create(tempPath, SonixRecorderConfig.VOICE)

        _isRecording.value = true

        recordingJob = viewModelScope.launch {
            val hwRate = AudioSessionManager.hardwareSampleRate.toInt()
            var sampleCount = 0

            recorder?.start()

            recorder?.audioBuffers?.collect { buffer ->
                val samples16k = SonixResampler.resample(
                    samples = buffer.samples,
                    fromRate = hwRate,
                    toRate = 16000
                )

                collectedAudio.addAll(samples16k.toList())
                sampleCount += samples16k.size

                // Calculate RMS for level meter
                val sum = samples16k.fold(0f) { acc, s -> acc + s * s }
                val rms = kotlin.math.sqrt(sum / samples16k.size)

                _recordingLevel.value = (rms * 5).coerceAtMost(1f)
                _recordingDuration.value = sampleCount / 16000f
            }
        }
    }

    fun stopRecording() {
        recordingJob?.cancel()
        recorder?.stop()
        _isRecording.value = false

        if (collectedAudio.isNotEmpty()) {
            _hasRecording.value = true
            _status.value = "Recording complete. Ready to evaluate."
        } else {
            _status.value = "No audio recorded. Try again."
        }
    }

    fun evaluate() {
        if (collectedAudio.isEmpty()) {
            _status.value = "No recording to evaluate"
            return
        }

        _isEvaluating.value = true
        _status.value = "Evaluating..."

        viewModelScope.launch {
            val keyMidi = currentKeyMidi

            val studentAudio = collectedAudio.toFloatArray()

            // Extract pitch contour from student audio
            val extractor = CalibraPitch.createContourExtractor()
            val studentContour = withContext(Dispatchers.Default) {
                extractor.extract(studentAudio, 16000)
            }
            extractor.release()

            // Convert key MIDI to Hz
            val keyHz = CalibraMusic.midiToHz(keyMidi.toFloat())

            // Simple evaluation: count how many detected pitches are near expected notes
            val detectedPitches = studentContour.samples.filter { it.isSinging }
            val expectedNotes = currentMidiNotes.map { CalibraMusic.midiToHz(it.toFloat()) }

            var matchCount = 0
            for (pitch in detectedPitches) {
                val pitchHz = pitch.pitch
                // Check if pitch is within a semitone of any expected note
                for (expected in expectedNotes) {
                    val ratio = pitchHz / expected
                    if (ratio in 0.94f..1.06f) { // Within ~1 semitone
                        matchCount++
                        break
                    }
                }
            }

            val evalScore = if (detectedPitches.isNotEmpty()) {
                (matchCount.toFloat() / detectedPitches.size).coerceIn(0f, 1f)
            } else 0f

            _score.value = evalScore
            _isEvaluating.value = false
            _status.value = "Evaluation complete: ${(evalScore * 100).toInt()}%"
        }
    }

    override fun onCleared() {
        super.onCleared()
        recordingJob?.cancel()
        recorder?.stop()
        recorder?.release()
    }
}
