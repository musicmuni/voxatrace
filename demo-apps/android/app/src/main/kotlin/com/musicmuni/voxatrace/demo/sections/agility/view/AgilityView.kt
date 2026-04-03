package com.musicmuni.voxatrace.demo.sections.agility.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.musicmuni.voxatrace.sonix.SonixDecoder
import com.musicmuni.voxatrace.tessera.TesseraAgility
import com.musicmuni.voxatrace.tessera.model.AgilityContour
import com.musicmuni.voxatrace.tessera.model.AgilityScore
import com.musicmuni.voxatrace.tona.PitchDetection
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Vocal Agility View - offline analysis using TesseraAgility.
 *
 * Pattern B: composable-local state (no ViewModel).
 */
@Composable
fun AgilityView() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isAnalyzing by remember { mutableStateOf(false) }
    var agilityScore by remember { mutableFloatStateOf(0f) }
    var rmsPeak by remember { mutableFloatStateOf(0f) }
    var oscPeak by remember { mutableFloatStateOf(0f) }
    var hasResult by remember { mutableStateOf(false) }

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

    fun analyzeOffline() {
        isAnalyzing = true
        hasResult = false
        agilityScore = 0f
        rmsPeak = 0f
        oscPeak = 0f

        scope.launch {
            try {
                val audioFile = withContext(Dispatchers.IO) {
                    copyAssetToFile("Alankaar 01_voice.m4a")
                }

                val audioData = withContext(Dispatchers.IO) {
                    SonixDecoder.decode(audioFile.absolutePath)
                }

                if (audioData == null) {
                    Napier.e("Failed to decode audio file")
                    isAnalyzing = false
                    return@launch
                }

                // Extract pitch contour
                val extractor = PitchDetection.createContourExtractor()
                val contour = withContext(Dispatchers.IO) {
                    extractor.extract(audioData.samples, audioData.sampleRate)
                }
                extractor.close()

                // Compute agility contour (intermediate) and score
                val ac = withContext(Dispatchers.Default) {
                    TesseraAgility.computeContour(contour)
                }
                val score = withContext(Dispatchers.Default) {
                    TesseraAgility.computeScore(ac)
                }

                agilityScore = score.scores.firstOrNull() ?: 0f
                rmsPeak = ac.rms.maxOrNull() ?: 0f
                oscPeak = ac.rmsOsc.maxOrNull() ?: 0f
                hasResult = true
            } catch (e: Exception) {
                Napier.e("Agility analysis failed", e)
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
            text = "Vocal Agility",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Analyze vocal agility from an audio file using TesseraAgility",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            onClick = { analyzeOffline() },
            enabled = !isAnalyzing
        ) {
            Text("Analyze Alankaar Voice")
        }

        if (isAnalyzing) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (hasResult) {
            AgilityResultCard(
                agilityScore = agilityScore,
                rmsPeak = rmsPeak,
                oscPeak = oscPeak
            )
        }

        ApiInfoCard()
    }
}

@Composable
private fun AgilityResultCard(
    agilityScore: Float,
    rmsPeak: Float,
    oscPeak: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Agility Result",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Agility Score",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "%.0f%%".format(agilityScore * 100),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            HorizontalDivider()

            Text(
                text = "Contour Summary",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "RMS Peak",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "%.4f".format(rmsPeak),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Oscillation Peak",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "%.4f".format(oscPeak),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
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
                text = "TesseraAgility.computeScore() - vocal agility scoring",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "TesseraAgility.computeContour() - agility contour intermediate",
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
