package com.musicmuni.calibra.sample

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.musicmuni.calibra.effects.Reverb
import com.musicmuni.calibra.effects.Compressor
import com.musicmuni.calibra.effects.NoiseGate

@Composable
fun EffectsSection() {
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    // Effect parameters
    var reverbMix by remember { mutableFloatStateOf(0.5f) }
    var reverbRoomSize by remember { mutableFloatStateOf(0.5f) }
    var compressorThreshold by remember { mutableFloatStateOf(-20f) }
    var compressorRatio by remember { mutableFloatStateOf(4f) }
    var noiseGateThreshold by remember { mutableFloatStateOf(-40f) }

    // Calibra handles
    var reverbHandle by remember { mutableLongStateOf(0L) }
    var compressorHandle by remember { mutableLongStateOf(0L) }
    var noiseGateHandle by remember { mutableLongStateOf(0L) }

    DisposableEffect(Unit) {
        reverbHandle = Reverb.create(reverbMix, reverbRoomSize)
        compressorHandle = Compressor.create(
            thresholdDb = compressorThreshold,
            ratio = compressorRatio,
            attackMs = 10f,
            releaseMs = 100f,
            autoMakeup = true,
            makeupDb = 0f,
            sampleRate = 16000f
        )
        noiseGateHandle = NoiseGate.create(
            thresholdDb = noiseGateThreshold,
            holdTimeMs = 100f,
            timeConstMs = 10f,
            sampleRate = 16000f
        )

        onDispose {
            if (reverbHandle != 0L) Reverb.destroy(reverbHandle)
            if (compressorHandle != 0L) Compressor.destroy(compressorHandle)
            if (noiseGateHandle != 0L) NoiseGate.destroy(noiseGateHandle)
        }
    }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Audio Effects Playground", style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { isRecording = true }) { Text("Record") }
            Button(onClick = { isRecording = false }) { Text("Stop") }
            Button(onClick = { isPlaying = !isPlaying }) { Text(if (isPlaying) "Pause" else "Play") }
        }

        // Reverb
        Text("Reverb", style = MaterialTheme.typography.titleSmall)
        Text("Mix: ${"%.2f".format(reverbMix)}")
        Slider(
            value = reverbMix,
            onValueChange = {
                reverbMix = it
                if (reverbHandle != 0L) Reverb.setMix(reverbHandle, it)
            }
        )
        Text("Room Size: ${"%.2f".format(reverbRoomSize)}")
        Slider(
            value = reverbRoomSize,
            onValueChange = {
                reverbRoomSize = it
                if (reverbHandle != 0L) Reverb.setRoomSize(reverbHandle, it)
            }
        )

        // Compressor
        Text("Compressor", style = MaterialTheme.typography.titleSmall)
        Text("Threshold: ${"%.1f".format(compressorThreshold)} dB")
        Slider(
            value = compressorThreshold,
            onValueChange = {
                compressorThreshold = it
                if (compressorHandle != 0L) Compressor.setThreshold(compressorHandle, it)
            },
            valueRange = -60f..0f
        )
        Text("Ratio: ${"%.1f".format(compressorRatio)}:1")
        Slider(
            value = compressorRatio,
            onValueChange = {
                compressorRatio = it
                if (compressorHandle != 0L) Compressor.setRatio(compressorHandle, it)
            },
            valueRange = 1f..20f
        )

        // Noise Gate
        Text("Noise Gate", style = MaterialTheme.typography.titleSmall)
        Text("Threshold: ${"%.1f".format(noiseGateThreshold)} dB")
        Slider(
            value = noiseGateThreshold,
            onValueChange = {
                noiseGateThreshold = it
                if (noiseGateHandle != 0L) NoiseGate.setThreshold(noiseGateHandle, it)
            },
            valueRange = -80f..0f
        )
    }
}
