package com.musicmuni.voxatrace.demo.sections.singafter.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicmuni.voxatrace.calibra.CalibraLiveEval
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.calibra.model.LessonMaterial
import com.musicmuni.voxatrace.calibra.model.PracticePhase
import com.musicmuni.voxatrace.calibra.model.PitchContour
import com.musicmuni.voxatrace.calibra.model.PitchDetectorConfig
import com.musicmuni.voxatrace.calibra.model.Segment
import com.musicmuni.voxatrace.calibra.model.SegmentResult
import com.musicmuni.voxatrace.calibra.model.SessionConfig
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.SonixPlayerConfig
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import com.musicmuni.voxatrace.sonix.util.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream

/**
 * Data class representing a phrase pair (teacher + student segment).
 */
data class PhrasePair(
    val index: Int,
    val lyrics: String,
    val teacherStartTime: Float,
    val teacherEndTime: Float,
    val studentStartTime: Float,
    val studentEndTime: Float,
    val teacherId: Int
)

/**
 * ViewModel for singafter (call and response) evaluation using CalibraLiveEval.
 *
 * ## VoxaTrace Integration (~25 lines)
 * ```kotlin
 * // 1. Create session with singafter segments
 * val segments = phrasePairs.mapIndexed { index, pair ->
 *     Segment.create(
 *         index = index,
 *         startSeconds = pair.teacherStartTime,
 *         endSeconds = pair.studentEndTime,
 *         lyrics = pair.lyrics,
 *         studentStartSeconds = pair.studentStartTime,
 *         studentEndSeconds = pair.studentEndTime
 *     )
 * }
 * session = CalibraLiveEval.create(reference, SessionConfig.PRACTICE, detector, player, recorder)
 *
 * // 2. Setup callbacks
 * session.onPhaseChanged { phase -> }
 * session.onSegmentComplete { result -> }
 *
 * // 3. Start practice - handles LISTENING -> SINGING transition automatically
 * session.playSegment(index = currentPairIndex)
 * ```
 */
class SingafterViewModel : ViewModel() {

    // MARK: - Published State

    private val _lessonLoaded = MutableStateFlow(false)
    val lessonLoaded: StateFlow<Boolean> = _lessonLoaded.asStateFlow()

    private val _phrasePairs = MutableStateFlow<List<PhrasePair>>(emptyList())
    val phrasePairs: StateFlow<List<PhrasePair>> = _phrasePairs.asStateFlow()

    private val _currentPairIndex = MutableStateFlow(0)
    val currentPairIndex: StateFlow<Int> = _currentPairIndex.asStateFlow()

    private val _practicePhase = MutableStateFlow(PracticePhase.IDLE)
    val practicePhase: StateFlow<PracticePhase> = _practicePhase.asStateFlow()

    private val _status = MutableStateFlow("Ready")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _segmentScore = MutableStateFlow(0f)
    val segmentScore: StateFlow<Float> = _segmentScore.asStateFlow()

    private val _feedbackMessage = MutableStateFlow("")
    val feedbackMessage: StateFlow<String> = _feedbackMessage.asStateFlow()

    private val _lastResult = MutableStateFlow<SegmentResult?>(null)
    val lastResult: StateFlow<SegmentResult?> = _lastResult.asStateFlow()

    private val lessonName = "Chalan"

    // MARK: - Computed Properties

    val currentLyrics: String
        get() {
            val pairs = _phrasePairs.value
            val index = _currentPairIndex.value
            return if (pairs.isNotEmpty() && index < pairs.size) pairs[index].lyrics else ""
        }

    val canNavigatePrevious: Boolean
        get() = _currentPairIndex.value > 0 && _practicePhase.value == PracticePhase.IDLE

    val canNavigateNext: Boolean
        get() = _currentPairIndex.value < _phrasePairs.value.size - 1 && _practicePhase.value == PracticePhase.IDLE

    // MARK: - Private State

    private var player: SonixPlayer? = null
    private var recorder: SonixRecorder? = null
    private var session: CalibraLiveEval? = null

    // MARK: - Actions

    fun loadLesson(context: Context) {
        _status.value = "Loading lesson..."

        viewModelScope.launch {
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
                    _status.value = "Failed to parse trans file: ${e.message}"
                    return@launch
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

                _phrasePairs.value = pairs

                // Copy audio file and create player
                val audioPath = withContext(Dispatchers.IO) {
                    copyAssetToFile(context, "$lessonName.m4a").absolutePath
                }

                player?.release()
                player = SonixPlayer.create(audioPath, SonixPlayerConfig.DEFAULT)

                // Create recorder
                val tempPath = "${context.cacheDir}/singafter_temp.m4a"
                val recorderConfig = SonixRecorderConfig.Builder()
                    .preset(SonixRecorderConfig.VOICE)
                    .build()
                recorder = SonixRecorder.create(tempPath, recorderConfig)

                // Decode audio for reference material
                val audioData = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(audioPath)
                }

                if (audioData == null) {
                    _status.value = "Failed to decode reference audio"
                    return@launch
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

                // Create reference material
                val reference = LessonMaterial.fromAudio(
                    samples = audioData.samples,
                    sampleRate = audioData.sampleRate,
                    segments = calibraSegments,
                    keyHz = 196.0f
                )

                // Create pitch detector
                val detector = CalibraPitch.createDetector(PitchDetectorConfig.BALANCED)

                // Create live evaluation session
                session = CalibraLiveEval.create(
                    reference = reference,
                    session = SessionConfig.DEFAULT,
                    detector = detector,
                    player = player,
                    recorder = recorder
                )

                setupCallbacks()

                session?.prepare()

                _lessonLoaded.value = true
                _currentPairIndex.value = 0
                _status.value = "Lesson loaded: ${pairs.size} phrase pairs"
            } catch (e: Exception) {
                _status.value = "Error loading lesson: ${e.message}"
            }
        }
    }

    fun navigatePrevious() {
        if (canNavigatePrevious) {
            _currentPairIndex.value--
        }
    }

    fun navigateNext() {
        if (canNavigateNext) {
            _currentPairIndex.value++
        }
    }

    fun startPractice() {
        if (!_lessonLoaded.value) return

        _segmentScore.value = 0f
        _feedbackMessage.value = ""

        session?.playSegment(_currentPairIndex.value)
    }

    fun forceStop() {
        session?.pause()
        _practicePhase.value = PracticePhase.IDLE
        _status.value = "Stopped"
    }

    private fun setupCallbacks() {
        val s = session ?: return

        s.onPhaseChanged { phase ->
            _practicePhase.value = phase
            _status.value = when (phase) {
                PracticePhase.LISTENING -> "Listen to the teacher..."
                PracticePhase.SINGING -> "Your turn! Sing now..."
                PracticePhase.EVALUATED -> "Score: ${(_segmentScore.value * 100).toInt()}%"
                PracticePhase.IDLE -> "Ready"
            }
        }

        s.onSegmentComplete { result ->
            _lastResult.value = result
            _segmentScore.value = result.score
            _feedbackMessage.value = result.feedbackMessage
        }
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
        player?.stop()
        player?.release()
        recorder?.stop()
        recorder?.release()
        session?.close()
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
