package com.musicmuni.voxatrace.demo.sections.singafter.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraLiveEval
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.model.LessonMaterial
import com.musicmuni.voxatrace.calibra.model.PracticePhase
import com.musicmuni.voxatrace.calibra.model.PitchDetectorConfig
import com.musicmuni.voxatrace.calibra.model.PitchContour
import com.musicmuni.voxatrace.calibra.model.Segment
import com.musicmuni.voxatrace.calibra.model.SegmentResult
import com.musicmuni.voxatrace.demo.sections.singafter.model.PhrasePair
import com.musicmuni.voxatrace.demo.sections.singafter.model.SingafterUIState
import com.musicmuni.voxatrace.demo.sections.singalong.model.SessionPreset
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.SonixPlayerConfig
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * ViewModel for Singafter (call and response) Practice.
 *
 * Responsibilities:
 * - Manages CalibraLiveEval session lifecycle
 * - Transforms session state for UI consumption
 * - Exposes actions for user interactions
 *
 * ## CalibraLiveEval Integration
 *
 * The actual library integration is minimal (~30 lines):
 * ```kotlin
 * // 1. Create session with singafter segments (includes studentStartSeconds/studentEndSeconds)
 * session = CalibraLiveEval.create(reference, session, detector, player, recorder)
 *
 * // 2. Setup callbacks
 * session.onPhaseChanged { }      // Handles LISTENING -> SINGING transition
 * session.onSegmentComplete { }
 *
 * // 3. Control playback
 * session.playSegment(index)
 * session.pause()
 * ```
 */
class SingafterViewModel : ViewModel() {

    // MARK: - Published State

    private val _uiState = MutableStateFlow<SingafterUIState>(SingafterUIState.Idle)
    val uiState: StateFlow<SingafterUIState> = _uiState.asStateFlow()

    private val _phrasePairs = MutableStateFlow<List<PhrasePair>>(emptyList())
    val phrasePairs: StateFlow<List<PhrasePair>> = _phrasePairs.asStateFlow()

    private val _currentPairIndex = MutableStateFlow(0)
    val currentPairIndex: StateFlow<Int> = _currentPairIndex.asStateFlow()

    private val _completedPairIndices = MutableStateFlow<Set<Int>>(emptySet())
    val completedPairIndices: StateFlow<Set<Int>> = _completedPairIndices.asStateFlow()

    private val _completedResults = MutableStateFlow<Map<Int, List<SegmentResult>>>(emptyMap())
    val completedResults: StateFlow<Map<Int, List<SegmentResult>>> = _completedResults.asStateFlow()

    private val _practicePhase = MutableStateFlow(PracticePhase.IDLE)
    val practicePhase: StateFlow<PracticePhase> = _practicePhase.asStateFlow()

    private val _currentPitch = MutableStateFlow(-1f)
    val currentPitch: StateFlow<Float> = _currentPitch.asStateFlow()

    private val _segmentProgress = MutableStateFlow(0f)
    val segmentProgress: StateFlow<Float> = _segmentProgress.asStateFlow()

    private val _lastResult = MutableStateFlow<SegmentResult?>(null)
    val lastResult: StateFlow<SegmentResult?> = _lastResult.asStateFlow()

    private val _selectedPreset = MutableStateFlow(SessionPreset.PRACTICE)
    val selectedPreset: StateFlow<SessionPreset> = _selectedPreset.asStateFlow()

    private val lessonName = "Chalan"

    // MARK: - Computed Properties

    val canGoPrevious: Boolean
        get() = _currentPairIndex.value > 0

    val canGoNext: Boolean
        get() = _currentPairIndex.value < _phrasePairs.value.size - 1

    val canRetry: Boolean
        get() = _practicePhase.value != PracticePhase.SINGING && _practicePhase.value != PracticePhase.LISTENING

    val isPracticing: Boolean
        get() = _practicePhase.value == PracticePhase.SINGING || _practicePhase.value == PracticePhase.LISTENING

    val currentLyrics: String
        get() {
            val pairs = _phrasePairs.value
            val index = _currentPairIndex.value
            return if (pairs.isNotEmpty() && index < pairs.size) pairs[index].lyrics else ""
        }

    /**
     * Status message based on current practice phase.
     */
    val statusMessage: String
        get() = when (_practicePhase.value) {
            PracticePhase.LISTENING -> "Listen to the teacher..."
            PracticePhase.SINGING -> "Your turn! Sing now..."
            PracticePhase.EVALUATED -> "Score: ${((_lastResult.value?.score ?: 0f) * 100).toInt()}%"
            PracticePhase.IDLE -> "Ready"
        }

    // MARK: - Private State

    private var player: SonixPlayer? = null
    private var recorder: SonixRecorder? = null
    private var session: CalibraLiveEval? = null
    private var observerJob: Job? = null
    private var resultsObserverJob: Job? = null
    private var appContext: Context? = null

    // MARK: - Lifecycle

    fun onAppear(context: Context) {
        appContext = context.applicationContext
        viewModelScope.launch {
            loadSession(context)
        }
    }

    fun onDisappear() {
        cleanup()
    }

    // MARK: - Actions

    fun play() {
        session?.startPracticingSegment(_currentPairIndex.value)
    }

    fun pause() {
        session?.pausePlayback()
        _practicePhase.value = PracticePhase.IDLE
    }

    fun goToPair(index: Int) {
        val pairs = _phrasePairs.value
        if (index < 0 || index >= pairs.size) return
        _currentPairIndex.value = index
        session?.seekToSegment(index)
    }

    fun nextPair() {
        if (!canGoNext) return
        val newIndex = _currentPairIndex.value + 1
        _currentPairIndex.value = newIndex
        session?.startPracticingSegment(newIndex)
    }

    fun previousPair() {
        if (!canGoPrevious) return
        val newIndex = _currentPairIndex.value - 1
        _currentPairIndex.value = newIndex
        session?.startPracticingSegment(newIndex)
    }

    fun retry() {
        session?.retryCurrentSegment()
        play()
    }

    fun finish() {
        session?.pausePlayback()
        _lastResult.value = null
        session?.finishSession()
        _uiState.value = SingafterUIState.Summary
    }

    fun reset() {
        session?.restartSession(fromSegment = 0)
        _uiState.value = SingafterUIState.Ready
        _completedResults.value = emptyMap()
        _completedPairIndices.value = emptySet()
        _lastResult.value = null
        _practicePhase.value = PracticePhase.IDLE
    }

    fun changePreset(preset: SessionPreset) {
        if (preset == _selectedPreset.value) return
        if (isPracticing) pause()
        _selectedPreset.value = preset
        cleanup()
        appContext?.let { ctx ->
            viewModelScope.launch {
                loadSession(ctx)
            }
        }
    }

    // MARK: - Session Management

    private suspend fun loadSession(context: Context) {
        _uiState.value = SingafterUIState.Loading

        try {
            // Load and parse trans file from assets
            val transContent = withContext(Dispatchers.IO) {
                context.assets.open("$lessonName.trans").bufferedReader().readText()
            }

            // Parse the trans file for singafter segments
            val json = Json { ignoreUnknownKeys = true }
            val segments = try {
                json.decodeFromString<List<SingafterSegment>>(transContent)
            } catch (e: Exception) {
                _uiState.value = SingafterUIState.Error("Failed to parse trans file: ${e.message}")
                return
            }

            // Group into phrase pairs (teacher + student segments)
            val teacherSegments = segments.filter { it.type == "teacher_vocal" }
            val pairs = teacherSegments.mapNotNull { teacher ->
                val student = segments.find { it.id == teacher.relatedSeg && it.type == "student_vocal" }
                if (student != null) {
                    PhrasePair(
                        index = teacher.id,
                        lyrics = teacher.lyrics,
                        teacherStartTime = teacher.startTime,
                        teacherEndTime = teacher.endTime,
                        studentStartTime = student.startTime,
                        studentEndTime = student.endTime,
                        teacherId = teacher.id
                    )
                } else null
            }

            if (pairs.isEmpty()) {
                _uiState.value = SingafterUIState.Error("No phrase pairs found in lesson")
                return
            }

            _phrasePairs.value = pairs

            // Copy audio file and create player
            val audioPath = withContext(Dispatchers.IO) {
                copyAssetToFile(context, "$lessonName.m4a").absolutePath
            }

            player?.release()
            player = SonixPlayer.create(audioPath, SonixPlayerConfig.DEFAULT)

            // Create recorder
            val tempPath = "${context.cacheDir}/singafter_${UUID.randomUUID()}.m4a"
            val recorderConfig = SonixRecorderConfig.Builder()
                .preset(SonixRecorderConfig.VOICE)
                .echoCancellation(true)
                .build()
            recorder = SonixRecorder.create(tempPath, recorderConfig)

            // Decode audio for reference material
            val audioData = withContext(Dispatchers.IO) {
                SonixDecoder.decode(audioPath)
            }

            if (audioData == null) {
                _uiState.value = SingafterUIState.Error("Failed to decode reference audio")
                return
            }

            // Create Calibra segments for singafter
            val calibraSegments = pairs.mapIndexed { index, pair ->
                Segment(
                    index = index,
                    startSeconds = pair.teacherStartTime,
                    endSeconds = pair.studentEndTime,
                    lyrics = pair.lyrics,
                    studentStartSeconds = pair.studentStartTime,
                    studentEndSeconds = pair.studentEndTime
                )
            }

            // Load pitch contour (optional optimization)
            val pitchContour: PitchContour? = withContext(Dispatchers.IO) {
                try {
                    val pitchContent = context.assets.open("$lessonName.pitchPP").bufferedReader().readText()
                    val pitchData = com.musicmuni.voxatrace.sonix.SonixParser.parsePitchString(pitchContent)
                    if (pitchData != null) {
                        PitchContour.fromArrays(
                            times = pitchData.times,
                            pitches = pitchData.pitchesHz
                        )
                    } else null
                } catch (e: Exception) {
                    null // Pitch file is optional
                }
            }

            // Create reference material
            val reference = LessonMaterial.fromAudio(
                samples = audioData.samples,
                sampleRate = audioData.sampleRate,
                segments = calibraSegments,
                keyHz = 196.0f,
                pitchContour = pitchContour
            )

            // Create pitch detector
            val detector = CalibraPitch.createDetector(PitchDetectorConfig.BALANCED)

            // Create live evaluation session
            session = CalibraLiveEval.create(
                reference = reference,
                session = _selectedPreset.value.config,
                detector = detector,
                player = player,
                recorder = recorder
            )

            setupCallbacks()
            setupObservers()

            session?.prepareSession()

            _uiState.value = SingafterUIState.Ready
        } catch (e: Exception) {
            _uiState.value = SingafterUIState.Error("Error loading lesson: ${e.message}")
        }
    }

    private fun setupCallbacks() {
        val s = session ?: return

        s.onPhaseChanged { phase ->
            _practicePhase.value = phase
        }

        s.onReferenceEnd { _ ->
            // Reference ended - no action needed for singafter
        }

        s.onSegmentComplete { result ->
            _lastResult.value = result
        }

        s.onSessionComplete { _ ->
            _uiState.value = SingafterUIState.Summary
        }
    }

    private fun setupObservers() {
        val s = session ?: return

        observerJob?.cancel()
        observerJob = viewModelScope.launch {
            s.state.collect { state ->
                _currentPairIndex.value = state.activeSegmentIndex ?: 0
                _currentPitch.value = state.currentPitch
                _segmentProgress.value = state.segmentProgress
                _completedPairIndices.value = state.completedSegments.toSet()
            }
        }

        resultsObserverJob?.cancel()
        resultsObserverJob = viewModelScope.launch {
            s.completedSegments.collect { results ->
                _completedResults.value = results
            }
        }
    }

    private fun cleanup() {
        observerJob?.cancel()
        resultsObserverJob?.cancel()
        player?.stop()
        player?.release()
        recorder?.stop()
        recorder?.release()
        session?.close()
        session = null
        player = null
        recorder = null
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
        cleanup()
    }
}

@Serializable
private data class SingafterSegment(
    val id: Int,
    val type: String,
    val lyrics: String,
    val startTime: Float,
    val endTime: Float,
    val relatedSeg: Int = -1
)
