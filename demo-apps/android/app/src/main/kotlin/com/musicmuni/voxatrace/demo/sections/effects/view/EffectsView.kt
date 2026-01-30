package com.musicmuni.voxatrace.demo.sections.effects.view

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.musicmuni.voxatrace.demo.sections.effects.viewmodel.EffectsViewModel

/**
 * Audio effects view demonstrating CalibraEffectsConfig.Builder pattern (ADR-001).
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EffectsView(viewModel: EffectsViewModel = viewModel()) {
    val context = LocalContext.current
    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val selectedPresetIndex by viewModel.selectedPresetIndex.collectAsStateWithLifecycle()
    val reverbMix by viewModel.reverbMix.collectAsStateWithLifecycle()
    val reverbRoomSize by viewModel.reverbRoomSize.collectAsStateWithLifecycle()
    val compressorThreshold by viewModel.compressorThreshold.collectAsStateWithLifecycle()
    val compressorRatio by viewModel.compressorRatio.collectAsStateWithLifecycle()
    val noiseGateThreshold by viewModel.noiseGateThreshold.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onDisappear()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Audio Effects Playground",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = status,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Preset selector
        PresetSelector(
            selectedIndex = selectedPresetIndex,
            presetNames = viewModel.presetNames,
            onSelect = { viewModel.setSelectedPresetIndex(it) }
        )

        // Control buttons
        ControlButtons(
            isRecording = isRecording,
            isPlaying = isPlaying,
            onToggleRecording = {
                if (audioPermissionState.status.isGranted) {
                    viewModel.toggleRecording(context)
                } else {
                    audioPermissionState.launchPermissionRequest()
                }
            },
            onTogglePlayback = { viewModel.togglePlayback(context) }
        )

        // Reverb controls
        ReverbControls(
            mix = reverbMix,
            roomSize = reverbRoomSize,
            onMixChange = { viewModel.updateReverbMix(it) },
            onRoomSizeChange = { viewModel.updateReverbRoomSize(it) }
        )

        // Compressor controls
        CompressorControls(
            threshold = compressorThreshold,
            ratio = compressorRatio,
            onThresholdChange = { viewModel.updateCompressorThreshold(it) },
            onRatioChange = { viewModel.updateCompressorRatio(it) }
        )

        // Noise gate controls
        NoiseGateControls(
            threshold = noiseGateThreshold,
            onThresholdChange = { viewModel.updateNoiseGateThreshold(it) }
        )
    }
}

@Composable
private fun PresetSelector(
    selectedIndex: Int,
    presetNames: List<String>,
    onSelect: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Preset:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = presetNames[selectedIndex],
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                textStyle = MaterialTheme.typography.bodySmall
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                presetNames.forEachIndexed { index, name ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            onSelect(index)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlButtons(
    isRecording: Boolean,
    isPlaying: Boolean,
    onToggleRecording: () -> Unit,
    onTogglePlayback: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onToggleRecording,
            enabled = !isPlaying
        ) {
            Text(if (isRecording) "Stop" else "Record")
        }

        OutlinedButton(
            onClick = onTogglePlayback,
            enabled = !isRecording
        ) {
            Text(if (isPlaying) "Pause" else "Play")
        }
    }
}

@Composable
private fun ReverbControls(
    mix: Float,
    roomSize: Float,
    onMixChange: (Float) -> Unit,
    onRoomSizeChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Reverb",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            // Mix slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Mix:", style = MaterialTheme.typography.bodySmall)
                Text("%.2f".format(mix), style = MaterialTheme.typography.bodySmall)
            }
            Slider(
                value = mix,
                onValueChange = onMixChange,
                valueRange = 0f..1f
            )

            // Room Size slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Room Size:", style = MaterialTheme.typography.bodySmall)
                Text("%.2f".format(roomSize), style = MaterialTheme.typography.bodySmall)
            }
            Slider(
                value = roomSize,
                onValueChange = onRoomSizeChange,
                valueRange = 0f..1f
            )
        }
    }
}

@Composable
private fun CompressorControls(
    threshold: Float,
    ratio: Float,
    onThresholdChange: (Float) -> Unit,
    onRatioChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Compressor",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            // Threshold slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Threshold:", style = MaterialTheme.typography.bodySmall)
                Text("%.1f dB".format(threshold), style = MaterialTheme.typography.bodySmall)
            }
            Slider(
                value = threshold,
                onValueChange = onThresholdChange,
                valueRange = -60f..0f
            )

            // Ratio slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ratio:", style = MaterialTheme.typography.bodySmall)
                Text("%.1f:1".format(ratio), style = MaterialTheme.typography.bodySmall)
            }
            Slider(
                value = ratio,
                onValueChange = onRatioChange,
                valueRange = 1f..20f
            )
        }
    }
}

@Composable
private fun NoiseGateControls(
    threshold: Float,
    onThresholdChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Noise Gate",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            // Threshold slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Threshold:", style = MaterialTheme.typography.bodySmall)
                Text("%.1f dB".format(threshold), style = MaterialTheme.typography.bodySmall)
            }
            Slider(
                value = threshold,
                onValueChange = onThresholdChange,
                valueRange = -80f..0f
            )
        }
    }
}
