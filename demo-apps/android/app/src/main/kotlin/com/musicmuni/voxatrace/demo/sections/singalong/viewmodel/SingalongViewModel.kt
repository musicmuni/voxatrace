package com.musicmuni.voxatrace.demo.sections.singalong.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraLiveEval
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.model.LessonMaterial
import com.musicmuni.voxatrace.calibra.model.PitchDetectorConfig
import com.musicmuni.voxatrace.calibra.model.PracticePhase
import com.musicmuni.voxatrace.calibra.model.Segment
import com.musicmuni.voxatrace.calibra.model.SegmentResult
import com.musicmuni.voxatrace.demo.sections.singalong.model.LessonData
import com.musicmuni.voxatrace.demo.sections.singalong.model.SessionPreset
import com.musicmuni.voxatrace.demo.sections.singalong.model.SingalongUIState
import com.musicmuni.voxatrace.calibra.model.PitchContour
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.SonixPlayerConfig
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import com.musicmuni.voxatrace.sonix.util.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * ViewModel for Singalong Practice.
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
 * // 1. Create session
 * session = CalibraLiveEval.create(reference, session, detector, player, recorder)
 *
 * // 2. Setup callbacks
 * session.onPhaseChanged { }
 * session.onSegmentComplete { }
 *
 * // 3. Control playback
 * session.playSegment(index)
 * session.pause()
 * session.seekToSegment(index)
 * ```
 */
class SingalongViewModel : ViewModel() {

    // MARK: - Published State

    private val _uiState = MutableStateFlow<SingalongUIState>(SingalongUIState.Idle)
    val uiState: StateFlow<SingalongUIState> = _uiState.asStateFlow()

    private val _segments = MutableStateFlow<List<Segment>>(emptyList())
    val segments: StateFlow<List<Segment>> = _segments.asStateFlow()

    private val _currentSegmentIndex = MutableStateFlow(0)
    val currentSegmentIndex: StateFlow<Int> = _currentSegmentIndex.asStateFlow()

    private val _completedSegmentIndices = MutableStateFlow<Set<Int>>(emptySet())
    val completedSegmentIndices: StateFlow<Set<Int>> = _completedSegmentIndices.asStateFlow()

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

    // MARK: - Computed Properties

    val canGoPrevious: Boolean
        get() = _currentSegmentIndex.value > 0

    val canGoNext: Boolean
        get() = _currentSegmentIndex.value < _segments.value.size - 1

    val canRetry: Boolean
        get() = _practicePhase.value != PracticePhase.SINGING

    val isPracticing: Boolean
        get() = _practicePhase.value == PracticePhase.SINGING

    val currentLyrics: String
        get() {
            val segs = _segments.value
            val index = _currentSegmentIndex.value
            return if (segs.isNotEmpty() && index < segs.size) segs[index].lyrics else ""
        }

    // MARK: - Private State

    private val lessonName = "Alankaar 01"
    private var session: CalibraLiveEval? = null
    private var player: SonixPlayer? = null
    private var recorder: SonixRecorder? = null
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
        session?.playSegment(_currentSegmentIndex.value)
    }

    fun pause() {
        session?.pause()
        _practicePhase.value = PracticePhase.IDLE
    }

    fun goToSegment(index: Int) {
        val segs = _segments.value
        if (index < 0 || index >= segs.size) return
        session?.seekToSegment(index)
    }

    fun nextSegment() {
        if (!canGoNext) return
        session?.playSegment(_currentSegmentIndex.value + 1)
    }

    fun previousSegment() {
        if (!canGoPrevious) return
        session?.playSegment(_currentSegmentIndex.value - 1)
    }

    fun retry() {
        session?.retryCurrentSegment()
        play()
    }

    fun finish() {
        session?.pause()
        _lastResult.value = null
        session?.finishSession()
        _uiState.value = SingalongUIState.Summary
    }

    fun reset() {
        session?.restartSession(fromSegment = 0)
        _uiState.value = SingalongUIState.Ready
        _completedResults.value = emptyMap()
        _completedSegmentIndices.value = emptySet()
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
        _uiState.value = SingalongUIState.Loading

        // 1. Load lesson data
        val lessonData: LessonData
        try {
            lessonData = loadLessonData(context)
        } catch (e: Exception) {
            _uiState.value = SingalongUIState.Error(e.message ?: "Failed to load lesson")
            return
        }

        // 2. Create audio I/O
        try {
            player?.release()
            player = SonixPlayer.create(lessonData.audioPath, SonixPlayerConfig.DEFAULT)

            val tempPath = "${context.cacheDir}/singalong_${UUID.randomUUID()}.m4a"
            val recorderConfig = SonixRecorderConfig.Builder()
                .preset(SonixRecorderConfig.VOICE)
                .echoCancellation(true)
                .build()
            recorder = SonixRecorder.create(tempPath, recorderConfig)
        } catch (e: Exception) {
            _uiState.value = SingalongUIState.Error("Audio setup failed: ${e.message}")
            return
        }

        // 3. Create CalibraLiveEval session
        val detector = CalibraPitch.createDetector(PitchDetectorConfig.BALANCED)
        session = CalibraLiveEval.create(
            reference = lessonData.reference,
            session = _selectedPreset.value.config,
            detector = detector,
            player = player,
            recorder = recorder
        )

        // 4. Setup callbacks & observers
        setupCallbacks()
        setupObservers()

        // 5. Prepare session
        session?.prepare()

        // 6. Update UI
        _segments.value = session?.segments ?: emptyList()
        _uiState.value = SingalongUIState.Ready
    }

    private suspend fun loadLessonData(context: Context): LessonData {
        // Load and parse trans file
        val transContent = withContext(Dispatchers.IO) {
            context.assets.open("$lessonName.trans").bufferedReader().readText()
        }
        val transData = Parser.parseTransString(transContent)
            ?: throw Exception("Failed to parse trans file")

        // Copy audio file
        val audioPath = withContext(Dispatchers.IO) {
            copyAssetToFile(context, "$lessonName.m4a").absolutePath
        }

        // Decode audio
        val audioData = withContext(Dispatchers.IO) {
            SonixDecoder.decode(audioPath)
        } ?: throw Exception("Failed to decode audio")

        // Create segments
        val segments = transData.segments.mapIndexed { index, seg ->
            Segment(
                index = index,
                startSeconds = seg.startTime.toFloat(),
                endSeconds = seg.endTime.toFloat(),
                lyrics = seg.lyrics
            )
        }

        // Load pitch contour (optional optimization)
        val pitchContour: PitchContour? = withContext(Dispatchers.IO) {
            try {
                val pitchContent = context.assets.open("$lessonName.pitchPP").bufferedReader().readText()
                val pitchData = Parser.parsePitchString(pitchContent)
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
            segments = segments,
            keyHz = 196.0f,
            pitchContour = pitchContour
        )

        return LessonData(reference, audioPath)
    }

    private fun setupCallbacks() {
        val s = session ?: return

        s.onPhaseChanged { phase ->
            _practicePhase.value = phase
        }

        s.onSegmentComplete { result ->
            _lastResult.value = result
        }

        s.onSessionComplete { _ ->
            _uiState.value = SingalongUIState.Summary
        }
    }

    private fun setupObservers() {
        val s = session ?: return

        observerJob?.cancel()
        observerJob = viewModelScope.launch {
            s.state.collect { state ->
                _currentSegmentIndex.value = state.activeSegmentIndex ?: 0
                _currentPitch.value = state.currentPitch
                _segmentProgress.value = state.segmentProgress
                _completedSegmentIndices.value = state.completedSegments.toSet()
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
