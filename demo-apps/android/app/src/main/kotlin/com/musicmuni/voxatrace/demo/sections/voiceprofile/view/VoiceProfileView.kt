package com.musicmuni.voxatrace.demo.sections.voiceprofile.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.musicmuni.voxatrace.demo.sections.voiceprofile.viewmodel.VoiceProfileViewModel

/**
 * Voice Profile View - multi-metric analysis dashboard via Tessera.analyze().
 */
@Composable
fun VoiceProfileView(viewModel: VoiceProfileViewModel = viewModel()) {
    val context = LocalContext.current

    val breathCapacity by viewModel.breathCapacity.collectAsStateWithLifecycle()
    val breathControl by viewModel.breathControl.collectAsStateWithLifecycle()
    val agilityScore by viewModel.agilityScore.collectAsStateWithLifecycle()
    val vocalRangeLow by viewModel.vocalRangeLow.collectAsStateWithLifecycle()
    val vocalRangeHigh by viewModel.vocalRangeHigh.collectAsStateWithLifecycle()
    val vocalRangeOctaves by viewModel.vocalRangeOctaves.collectAsStateWithLifecycle()
    val isAnalyzing by viewModel.isAnalyzing.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Voice Profile",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Multi-metric analysis \u2014 breath, agility, and range in one call",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            onClick = { viewModel.analyzeOffline(context) },
            enabled = !isAnalyzing
        ) {
            Text("Analyze Voice")
        }

        if (isAnalyzing) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Dashboard card with 3 sections side by side
        if (vocalRangeLow.isNotEmpty()) {
            DashboardCard(
                breathCapacity = breathCapacity,
                breathControl = breathControl,
                agilityScore = agilityScore,
                vocalRangeLow = vocalRangeLow,
                vocalRangeHigh = vocalRangeHigh,
                vocalRangeOctaves = vocalRangeOctaves
            )
        }

        ApiInfoCard()
    }
}

@Composable
private fun DashboardCard(
    breathCapacity: Float,
    breathControl: Float,
    agilityScore: Float,
    vocalRangeLow: String,
    vocalRangeHigh: String,
    vocalRangeOctaves: Float
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
                text = "Voice Profile Result",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Breath section
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Breath",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "%.1f s".format(breathCapacity),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "capacity",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "%.0f%%".format(breathControl * 100),
                        style = MaterialTheme.typography.titleMedium,
                        color = when {
                            breathControl >= 0.7f -> Color(0xFF4CAF50)
                            breathControl >= 0.4f -> Color(0xFFFF9800)
                            else -> MaterialTheme.colorScheme.error
                        }
                    )
                    Text(
                        text = "control",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Agility section
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Agility",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "%.0f%%".format(agilityScore * 100),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            agilityScore >= 0.7f -> Color(0xFF4CAF50)
                            agilityScore >= 0.4f -> Color(0xFFFF9800)
                            else -> MaterialTheme.colorScheme.error
                        }
                    )
                    Text(
                        text = "score",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Range section
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Range",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$vocalRangeLow \u2014 $vocalRangeHigh",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "%.1f octaves".format(vocalRangeOctaves),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                text = "Tessera.analyze() - multi-metric batch analysis",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "TesseraMetric.ALL - breath, agility, vocal range",
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
