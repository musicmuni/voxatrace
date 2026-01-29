package com.musicmuni.voxatrace.demo.sections.pitch.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.musicmuni.voxatrace.calibra.model.PitchPoint

/**
 * PitchPoint explorer view - demonstrates PitchPoint properties.
 */
@Composable
fun PitchPointExplorerView() {
    // Sample pitch point for demonstration
    val samplePoint = remember {
        PitchPoint(
            pitch = 440f,
            confidence = 0.95f,
            timeSeconds = 0.5f
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "PitchPoint Explorer",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Explore the properties of a PitchPoint object",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider()

        // Properties card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "PitchPoint Properties",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )

                PropertyRow("pitch", "${samplePoint.pitch} Hz")
                PropertyRow("confidence", "${(samplePoint.confidence * 100).toInt()}%")
                PropertyRow("timeSeconds", "${samplePoint.timeSeconds} s")
                PropertyRow("isSinging", samplePoint.isSinging.toString())
                PropertyRow("note", samplePoint.note ?: "-")
                PropertyRow("midiNote", samplePoint.midiNote.toString())
                PropertyRow("centsOff", "${samplePoint.centsOff}")
            }
        }

        // Explanation card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Computed Properties:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "isSinging: true if pitch > 0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "midiNote: MIDI note number (A4 = 69)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "centsOff: deviation from nearest note",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PropertyRow(name: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
