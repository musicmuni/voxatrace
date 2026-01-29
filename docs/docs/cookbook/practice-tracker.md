---
sidebar_position: 4
---

# Practice Tracker

Build a practice app that tracks singing scores over time and shows improvement.

## What You'll Build

A progress tracking system:
- Record practice sessions with scores
- Show score history per song/segment
- Visualize improvement over time
- Set and track goals

## Data Models

```kotlin
data class PracticeSession(
    val id: String = UUID.randomUUID().toString(),
    val songId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val segmentScores: List<SegmentScore>,
    val overallScore: Float
)

data class SegmentScore(
    val segmentIndex: Int,
    val score: Float,
    val attemptNumber: Int
)

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val segments: List<SegmentInfo>
)

data class SegmentInfo(
    val index: Int,
    val name: String,  // "Verse 1", "Chorus", etc.
    val difficulty: Difficulty
)

enum class Difficulty { EASY, MEDIUM, HARD }

data class Goal(
    val id: String = UUID.randomUUID().toString(),
    val songId: String,
    val targetScore: Float,
    val deadline: Long?,
    val achieved: Boolean = false
)
```

## Repository

```kotlin
class PracticeRepository(private val database: AppDatabase) {

    // Save practice session
    suspend fun savePracticeSession(session: PracticeSession) {
        database.practiceDao().insert(session.toEntity())
    }

    // Get sessions for a song
    suspend fun getSessionsForSong(songId: String): List<PracticeSession> {
        return database.practiceDao().getBySongId(songId).map { it.toModel() }
    }

    // Get score history for a specific segment
    suspend fun getSegmentHistory(songId: String, segmentIndex: Int): List<Float> {
        return getSessionsForSong(songId)
            .flatMap { it.segmentScores }
            .filter { it.segmentIndex == segmentIndex }
            .map { it.score }
    }

    // Get best score for a song
    suspend fun getBestScore(songId: String): Float? {
        return getSessionsForSong(songId).maxOfOrNull { it.overallScore }
    }

    // Get average score over last N sessions
    suspend fun getRecentAverage(songId: String, count: Int = 5): Float? {
        return getSessionsForSong(songId)
            .sortedByDescending { it.timestamp }
            .take(count)
            .map { it.overallScore }
            .average()
            .takeIf { !it.isNaN() }
            ?.toFloat()
    }

    // Get improvement trend (positive = improving)
    suspend fun getImprovementTrend(songId: String): Float {
        val sessions = getSessionsForSong(songId)
            .sortedBy { it.timestamp }
            .takeLast(10)

        if (sessions.size < 2) return 0f

        val firstHalf = sessions.take(sessions.size / 2).map { it.overallScore }.average()
        val secondHalf = sessions.takeLast(sessions.size / 2).map { it.overallScore }.average()

        return (secondHalf - firstHalf).toFloat()
    }
}
```

## ViewModel

```kotlin
class PracticeTrackerViewModel(
    private val repository: PracticeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TrackerState())
    val state: StateFlow<TrackerState> = _state.asStateFlow()

    data class TrackerState(
        val songs: List<SongProgress> = emptyList(),
        val selectedSong: SongProgress? = null,
        val goals: List<Goal> = emptyList()
    )

    data class SongProgress(
        val song: Song,
        val bestScore: Float?,
        val recentAverage: Float?,
        val trend: Float,
        val practiceCount: Int,
        val segmentProgress: List<SegmentProgress>
    )

    data class SegmentProgress(
        val segment: SegmentInfo,
        val bestScore: Float?,
        val recentAverage: Float?,
        val history: List<Float>
    )

    fun loadSongProgress(songId: String) {
        viewModelScope.launch {
            val song = repository.getSong(songId)
            val sessions = repository.getSessionsForSong(songId)

            val segmentProgress = song.segments.map { segment ->
                val history = repository.getSegmentHistory(songId, segment.index)
                SegmentProgress(
                    segment = segment,
                    bestScore = history.maxOrNull(),
                    recentAverage = history.takeLast(5).average().toFloat(),
                    history = history
                )
            }

            val progress = SongProgress(
                song = song,
                bestScore = repository.getBestScore(songId),
                recentAverage = repository.getRecentAverage(songId),
                trend = repository.getImprovementTrend(songId),
                practiceCount = sessions.size,
                segmentProgress = segmentProgress
            )

            _state.update { it.copy(selectedSong = progress) }
        }
    }

    fun recordPractice(
        songId: String,
        segmentResults: List<SegmentResult>
    ) {
        viewModelScope.launch {
            val session = PracticeSession(
                songId = songId,
                segmentScores = segmentResults.map {
                    SegmentScore(
                        segmentIndex = it.segment.index,
                        score = it.score,
                        attemptNumber = it.attemptNumber
                    )
                },
                overallScore = segmentResults.map { it.score }.average().toFloat()
            )

            repository.savePracticeSession(session)

            // Check goals
            checkGoals(songId, session.overallScore)

            // Refresh progress
            loadSongProgress(songId)
        }
    }

    private suspend fun checkGoals(songId: String, score: Float) {
        val goals = repository.getGoalsForSong(songId)
            .filter { !it.achieved && score >= it.targetScore }

        goals.forEach { goal ->
            repository.markGoalAchieved(goal.id)
            // Notify user
        }
    }
}
```

## UI Components

### Progress Dashboard

```kotlin
@Composable
fun ProgressDashboard(progress: SongProgress) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Overall stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(
                label = "Best",
                value = "${((progress.bestScore ?: 0f) * 100).toInt()}%"
            )
            StatCard(
                label = "Average",
                value = "${((progress.recentAverage ?: 0f) * 100).toInt()}%"
            )
            StatCard(
                label = "Practices",
                value = progress.practiceCount.toString()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Trend indicator
        TrendIndicator(trend = progress.trend)

        Spacer(modifier = Modifier.height(24.dp))

        // Segment breakdown
        Text("Segment Progress", style = MaterialTheme.typography.titleMedium)

        progress.segmentProgress.forEach { segment ->
            SegmentProgressRow(segment)
        }
    }
}

@Composable
fun TrendIndicator(trend: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = when {
                trend > 0.05f -> Icons.Default.TrendingUp
                trend < -0.05f -> Icons.Default.TrendingDown
                else -> Icons.Default.TrendingFlat
            },
            contentDescription = null,
            tint = when {
                trend > 0.05f -> Color.Green
                trend < -0.05f -> Color.Red
                else -> Color.Gray
            }
        )
        Text(
            text = when {
                trend > 0.05f -> "Improving!"
                trend < -0.05f -> "Keep practicing"
                else -> "Steady"
            },
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun SegmentProgressRow(progress: SegmentProgress) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = progress.segment.name,
            modifier = Modifier.weight(1f)
        )

        // Mini sparkline
        HistorySparkline(
            history = progress.history,
            modifier = Modifier.width(60.dp).height(24.dp)
        )

        // Best score
        Text(
            text = "${((progress.bestScore ?: 0f) * 100).toInt()}%",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(50.dp)
        )
    }
}
```

### History Chart

```kotlin
@Composable
fun HistoryChart(sessions: List<PracticeSession>, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        if (sessions.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        val padding = 16f

        val maxScore = 1f
        val minScore = 0f

        val points = sessions.mapIndexed { index, session ->
            val x = padding + (index.toFloat() / (sessions.size - 1).coerceAtLeast(1)) * (width - 2 * padding)
            val y = height - padding - (session.overallScore / maxScore) * (height - 2 * padding)
            Offset(x, y)
        }

        // Draw line
        if (points.size > 1) {
            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                points.drop(1).forEach { lineTo(it.x, it.y) }
            }
            drawPath(path, Color.Blue, style = Stroke(width = 3f))
        }

        // Draw points
        points.forEach { point ->
            drawCircle(Color.Blue, radius = 6f, center = point)
        }
    }
}
```

## Integration with CalibraLiveEval

```kotlin
class PracticeSession(
    private val viewModel: PracticeTrackerViewModel,
    private val song: Song
) {
    private var session: CalibraLiveEval? = null
    private val results = mutableListOf<SegmentResult>()

    suspend fun start(player: SonixPlayer, recorder: SonixRecorder) {
        val detector = CalibraPitch.createDetector()

        session = CalibraLiveEval.create(
            reference = song.lessonMaterial,
            detector = detector,
            player = player,
            recorder = recorder
        )

        session?.prepare()

        // Collect segment results
        session?.onSegmentComplete { result ->
            results.add(result)
        }

        // When session completes, save to tracker
        session?.onSessionComplete {
            viewModel.recordPractice(song.id, results)
        }

        // Start first segment
        session?.playSegment(0)
    }

    fun close() {
        session?.close()
    }
}
```

## Next Steps

- [Live Evaluation Guide](/docs/guides/live-evaluation) - Scoring details
- [Karaoke App Recipe](/docs/cookbook/karaoke-app) - Full karaoke implementation
- [Demo App](https://github.com/musicmuni/voxatrace-demos) - Full source
