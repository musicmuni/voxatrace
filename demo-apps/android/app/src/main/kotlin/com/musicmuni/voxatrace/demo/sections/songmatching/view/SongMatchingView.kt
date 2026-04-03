package com.musicmuni.voxatrace.demo.sections.songmatching.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.tessera.TesseraRange
import com.musicmuni.voxatrace.tessera.model.Gender
import com.musicmuni.voxatrace.tessera.model.VocalRangeMatch
import com.musicmuni.voxatrace.tona.PitchDetection
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Song Matching View - demonstrates TesseraRange search vector and match APIs.
 *
 * Given a singer's voice and a song reference, computes search vectors and
 * matches them to produce similarity and difficulty scores.
 */
@Composable
fun SongMatchingView() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isAnalyzing by remember { mutableStateOf(false) }
    var matchResult by remember { mutableStateOf<VocalRangeMatch?>(null) }
    var singerVectorDims by remember { mutableIntStateOf(0) }
    var songVectorDims by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun copyAssetToFile(assetName: String): File {
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

    fun matchSingerToSong() {
        isAnalyzing = true
        matchResult = null
        errorMessage = null

        scope.launch {
            try {
                // 1. Load and decode both audio files
                val singerFile = withContext(Dispatchers.IO) {
                    copyAssetToFile("Chalan_voice.m4a")
                }
                val songFile = withContext(Dispatchers.IO) {
                    copyAssetToFile("Alankaar 01.m4a")
                }

                val singerAudio = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(singerFile.absolutePath)
                }
                val songAudio = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(songFile.absolutePath)
                }

                if (singerAudio == null || songAudio == null) {
                    errorMessage = "Failed to decode audio file(s)"
                    isAnalyzing = false
                    return@launch
                }

                // 2. Extract pitch contours
                val singerContour = withContext(Dispatchers.Default) {
                    val extractor = PitchDetection.createContourExtractor()
                    val contour = extractor.extract(singerAudio.samples, singerAudio.sampleRate)
                    extractor.release()
                    contour
                }

                val songContour = withContext(Dispatchers.Default) {
                    val extractor = PitchDetection.createContourExtractor()
                    val contour = extractor.extract(songAudio.samples, songAudio.sampleRate)
                    extractor.release()
                    contour
                }

                // 3. Compute search vectors
                val singerVec = withContext(Dispatchers.Default) {
                    TesseraRange.computeSearchVector(singerContour, normalize = false)
                }
                val songVec = withContext(Dispatchers.Default) {
                    TesseraRange.computeSearchVector(songContour, normalize = true)
                }

                singerVectorDims = singerVec.size
                songVectorDims = songVec.size

                // 4. Compute match (assume female for Chalan)
                val result = withContext(Dispatchers.Default) {
                    TesseraRange.computeMatch(singerVec, songVec, Gender.FEMALE)
                }

                matchResult = result
            } catch (e: Exception) {
                Napier.e("Song matching failed", e)
                errorMessage = "Error: ${e.message}"
            } finally {
                isAnalyzing = false
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Song Matching",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Match a singer's voice to a song using search vectors",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            onClick = { matchSingerToSong() },
            enabled = !isAnalyzing
        ) {
            Text("Match Singer to Song")
        }

        if (isAnalyzing) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        errorMessage?.let { error ->
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        matchResult?.let { result ->
            MatchResultCard(
                similarity = result.similarity,
                difficulty = result.difficulty,
                singerVectorDims = singerVectorDims,
                songVectorDims = songVectorDims
            )
        }

        ApiInfoCard()
    }
}

@Composable
private fun MatchResultCard(
    similarity: Float,
    difficulty: Int,
    singerVectorDims: Int,
    songVectorDims: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Match Results",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            // Similarity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Similarity",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "%.2f".format(similarity),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = similarityColor(similarity)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Difficulty",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$difficulty / 5",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Similarity bar
            LinearProgressIndicator(
                progress = { similarity.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = similarityColor(similarity)
            )

            HorizontalDivider()

            // Vector info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Singer vector: ${singerVectorDims}-dim",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Song vector: ${songVectorDims}-dim",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Difficulty stars
            Text(
                text = "Difficulty: " + "\u2605".repeat(difficulty) + "\u2606".repeat(5 - difficulty),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun similarityColor(similarity: Float): androidx.compose.ui.graphics.Color {
    return when {
        similarity >= 0.7f -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
        similarity >= 0.4f -> androidx.compose.ui.graphics.Color(0xFFFF9800)
        else -> androidx.compose.ui.graphics.Color(0xFFF44336)
    }
}

@Composable
private fun ApiInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "APIs Demonstrated:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "TesseraRange.computeSearchVector() - 13-dim search vector from contour",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "TesseraRange.computeMatch() - singer vs song similarity + difficulty",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "PitchDetection.createContourExtractor() - pitch extraction",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "SonixDecoder.decode() - audio file loading",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
