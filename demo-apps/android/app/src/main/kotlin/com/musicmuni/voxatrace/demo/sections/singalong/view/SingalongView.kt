package com.musicmuni.voxatrace.demo.sections.singalong.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.sonix.SonixPlayer
import com.musicmuni.voxatrace.sonix.SonixPlayerConfig
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixResampler
import com.musicmuni.voxatrace.sonix.util.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

data class SegmentInfo(
    val index: Int,
    val lyrics: String,
    val startTime: Float,
    val endTime: Float
)

enum class PracticeState { IDLE, PLAYING, COMPLETE }

/**
 * Singalong Practice View - for lessons where user sings along with teacher.
 */
@Composable
fun SingalongView() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Lesson state
    var lessonLoaded by remember { mutableStateOf(false) }
    var segments by remember { mutableStateOf<List<SegmentInfo>>(emptyList()) }
    var currentSegmentIndex by remember { mutableStateOf(0) }
    val lessonName = "Alankaar 01"

    // Playback state
    var player by remember { mutableStateOf<SonixPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    // Recording state
    var recorder by remember { mutableStateOf<SonixRecorder?>(null) }
    var recordingLevel by remember { mutableFloatStateOf(0f) }

    // Evaluation state
    var segmentScore by remember { mutableFloatStateOf(0f) }
    var practiceState by remember { mutableStateOf(PracticeState.IDLE) }
    var status by remember { mutableStateOf("Ready") }

    // Copy asset to cache
    suspend fun copyAssetToCache(assetName: String): String {
        return withContext(Dispatchers.IO) {
            val cacheFile = File(context.cacheDir, assetName)
            if (!cacheFile.exists()) {
                context.assets.open(assetName).use { input ->
                    FileOutputStream(cacheFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }
            cacheFile.absolutePath
        }
    }

    // Load lesson
    fun loadLesson() {
        scope.launch {
            status = "Loading lesson..."
            try {
                // Parse trans file
                val transContent = withContext(Dispatchers.IO) {
                    context.assets.open("$lessonName.trans").bufferedReader().readText()
                }
                val transData = Parser.parseTransString(transContent)
                if (transData == null) {
                    status = "Failed to parse trans file"
                    return@launch
                }

                // Convert to SegmentInfo
                segments = transData.segments.mapIndexed { index, seg ->
                    SegmentInfo(
                        index = index,
                        lyrics = seg.lyrics,
                        startTime = seg.startTime.toFloat(),
                        endTime = seg.endTime.toFloat()
                    )
                }

                // Copy audio and create player
                val audioPath = copyAssetToCache("$lessonName.mp3")
                player?.release()
                player = SonixPlayer.create(audioPath, SonixPlayerConfig.DEFAULT)

                lessonLoaded = true
                status = "Loaded ${segments.size} segments"
            } catch (e: Exception) {
                status = "Error: ${e.message}"
            }
        }
    }

    // Start practice
    fun startPractice() {
        if (!lessonLoaded || segments.isEmpty()) return

        scope.launch {
            val segment = segments[currentSegmentIndex]

            practiceState = PracticeState.PLAYING
            segmentScore = 0f
            status = "Singing: ${segment.lyrics}"

            // Seek to segment start and play
            player?.seek((segment.startTime * 1000).toLong())
            player?.play()
            isPlaying = true

            // Collect playback state
            launch {
                player?.currentTime?.collect { timeMs ->
                    val segmentEndMs = (segment.endTime * 1000).toLong()
                    if (timeMs >= segmentEndMs && isPlaying) {
                        player?.pause()
                        isPlaying = false
                        practiceState = PracticeState.COMPLETE
                        // Demo score
                        segmentScore = (0.6f + Math.random().toFloat() * 0.35f)
                        status = "Score: ${(segmentScore * 100).toInt()}%"
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player?.release()
            recorder?.release()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Singalong Practice",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Sing along with the reference and get evaluated",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(text = "Status: $status", style = MaterialTheme.typography.bodySmall)

        // Load button
        if (!lessonLoaded) {
            Button(onClick = { loadLesson() }, modifier = Modifier.fillMaxWidth()) {
                Text("Load $lessonName")
            }
        } else {
            // Segment selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Segment:", style = MaterialTheme.typography.labelMedium)
                segments.forEachIndexed { index, _ ->
                    FilterChip(
                        selected = currentSegmentIndex == index,
                        onClick = {
                            if (practiceState == PracticeState.IDLE) {
                                currentSegmentIndex = index
                            }
                        },
                        label = { Text("${index + 1}") }
                    )
                }
            }

            // Current segment info
            if (segments.isNotEmpty()) {
                val segment = segments[currentSegmentIndex]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = segment.lyrics,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "%.1fs - %.1fs".format(segment.startTime, segment.endTime),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Score display
            if (practiceState == PracticeState.COMPLETE) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            segmentScore >= 0.8f -> Color(0xFF4CAF50)
                            segmentScore >= 0.6f -> Color(0xFFFF9800)
                            else -> MaterialTheme.colorScheme.error
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${(segmentScore * 100).toInt()}%",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Control buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (practiceState) {
                    PracticeState.IDLE -> {
                        Button(onClick = { startPractice() }, modifier = Modifier.weight(1f)) {
                            Text("Start Practice")
                        }
                    }
                    PracticeState.PLAYING -> {
                        Button(
                            onClick = {
                                player?.pause()
                                isPlaying = false
                                practiceState = PracticeState.IDLE
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Stop")
                        }
                    }
                    PracticeState.COMPLETE -> {
                        Button(onClick = { practiceState = PracticeState.IDLE }, modifier = Modifier.weight(1f)) {
                            Text("Try Again")
                        }
                        Button(
                            onClick = {
                                if (currentSegmentIndex < segments.size - 1) {
                                    currentSegmentIndex++
                                    practiceState = PracticeState.IDLE
                                }
                            },
                            enabled = currentSegmentIndex < segments.size - 1,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Next")
                        }
                    }
                }
            }
        }
    }
}
