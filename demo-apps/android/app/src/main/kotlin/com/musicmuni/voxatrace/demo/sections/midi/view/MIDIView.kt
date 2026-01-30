package com.musicmuni.voxatrace.demo.sections.midi.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.musicmuni.voxatrace.demo.components.OptionChip
import com.musicmuni.voxatrace.demo.sections.midi.viewmodel.MIDIViewModel

/**
 * MIDI View - UI for MIDI synthesis demo.
 *
 * Demonstrates:
 * - SonixMidiSynthesizer Builder pattern
 * - Custom sample rate configuration
 * - synthesizeFromNotes() for programmatic MIDI generation
 * - SonixPlayer for playback of synthesized audio
 */
@Composable
fun MIDIView(viewModel: MIDIViewModel = viewModel()) {
    val context = LocalContext.current

    // Stop playback when leaving the screen (matches iOS onDisappear)
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stop()
        }
    }

    // Collect state from ViewModel
    val status by viewModel.status.collectAsStateWithLifecycle()
    val isSynthesizing by viewModel.isSynthesizing.collectAsStateWithLifecycle()
    val outputFile by viewModel.outputFile.collectAsStateWithLifecycle()
    val synthesizerVersion by viewModel.synthesizerVersion.collectAsStateWithLifecycle()
    val selectedSampleRate by viewModel.selectedSampleRate.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "MIDI Synthesis",
            style = MaterialTheme.typography.titleMedium
        )

        Text(text = "Status: $status", style = MaterialTheme.typography.bodySmall)

        // Show FluidSynth version if available
        synthesizerVersion?.let {
            Text(text = "FluidSynth: $it", style = MaterialTheme.typography.bodySmall)
        }

        // C major scale notes
        Text(
            text = "Notes: C4 - D4 - E4 - F4 - G4 - A4 - B4 - C5",
            style = MaterialTheme.typography.bodySmall
        )

        // Sample rate selection
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Sample Rate:", style = MaterialTheme.typography.bodySmall)
            listOf(44100, 48000).forEach { rate ->
                OptionChip(
                    selected = selectedSampleRate == rate,
                    onClick = { viewModel.setSelectedSampleRate(rate) },
                    label = "${rate / 1000}kHz",
                    enabled = !isSynthesizing
                )
            }
        }

        // Synthesize button
        Button(
            onClick = { viewModel.synthesize(context) },
            enabled = !isSynthesizing
        ) {
            Text(if (isSynthesizing) "Synthesizing..." else "Synthesize")
        }

        // Playback controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.play() },
                enabled = outputFile != null && !isSynthesizing && !isPlaying
            ) {
                Text("Play")
            }

            Button(
                onClick = { viewModel.stop() },
                enabled = isPlaying
            ) {
                Text("Stop")
            }
        }
    }
}
