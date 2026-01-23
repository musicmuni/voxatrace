package com.musicmuni.vozos.demo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.musicmuni.vozos.calibra.Calibra
import com.musicmuni.vozos.demo.calibra.*
import com.musicmuni.vozos.demo.components.OptionChip
import com.musicmuni.vozos.demo.sonix.*
import com.musicmuni.vozos.demo.sonix.simplified.*
import com.musicmuni.vozos.sonix.Sonix
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Napier.base(DebugAntilog())

        // Initialize SDKs
        Calibra.init()
        try {
            Sonix.initialize(BuildConfig.SONIX_API_KEY, this)
        } catch (e: Exception) {
            Napier.e("Sonix initialization failed", e)
            setContent {
                MaterialTheme {
                    LicenseErrorScreen(e.message ?: "License validation failed")
                }
            }
            return
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VozOSDemoApp(context = this)
                }
            }
        }
    }
}

@Composable
fun LicenseErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "License Error",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

sealed class Screen {
    data object Home : Screen()
    data object Sonix : Screen()
    data object Calibra : Screen()
    data class SonixFeature(val name: String, val apiMode: ApiMode) : Screen()
    data class CalibraFeature(val name: String) : Screen()
}

enum class ApiMode { SIMPLE, ADVANCED }

@Composable
fun VozOSDemoApp(context: ComponentActivity) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var apiMode by remember { mutableStateOf(ApiMode.SIMPLE) }

    when (val screen = currentScreen) {
        Screen.Home -> HomeScreen(
            onSonixClick = { currentScreen = Screen.Sonix },
            onCalibraClick = { currentScreen = Screen.Calibra }
        )
        Screen.Sonix -> SonixScreen(
            apiMode = apiMode,
            onApiModeChange = { apiMode = it },
            onBack = { currentScreen = Screen.Home },
            onFeatureClick = { feature -> currentScreen = Screen.SonixFeature(feature, apiMode) }
        )
        Screen.Calibra -> CalibraScreen(
            onBack = { currentScreen = Screen.Home },
            onFeatureClick = { feature -> currentScreen = Screen.CalibraFeature(feature) }
        )
        is Screen.SonixFeature -> SonixFeatureScreen(
            feature = screen.name,
            apiMode = screen.apiMode,
            onBack = { currentScreen = Screen.Sonix },
            context = context
        )
        is Screen.CalibraFeature -> CalibraFeatureScreen(
            feature = screen.name,
            onBack = { currentScreen = Screen.Calibra }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onSonixClick: () -> Unit, onCalibraClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("VozOS Demo") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CategoryCard(
                title = "Sonix",
                subtitle = "Audio Transport",
                description = "Playback, Recording, MIDI, Metronome, Multi-Track",
                onClick = onSonixClick
            )

            CategoryCard(
                title = "Calibra",
                subtitle = "Audio Analysis",
                description = "Pitch Detection, Voice Analysis, Singing Evaluation",
                onClick = onCalibraClick
            )
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    subtitle: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class FeatureItem(val name: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SonixScreen(
    apiMode: ApiMode,
    onApiModeChange: (ApiMode) -> Unit,
    onBack: () -> Unit,
    onFeatureClick: (String) -> Unit
) {
    val baseFeatures = listOf(
        FeatureItem("Playback", "Audio playback with pitch shifting"),
        FeatureItem("Recording", "Audio recording to M4A/MP3"),
        FeatureItem("Multi-Track", "Multi-track mixing"),
        FeatureItem("Metronome", "Click track with visual feedback"),
        FeatureItem("MIDI Synthesis", "SoundFont-based synthesis")
    )

    val advancedFeatures = listOf(
        FeatureItem("Decoding", "Audio decode/encode"),
        FeatureItem("Parser", "Parse notation files")
    )

    val visibleFeatures = if (apiMode == ApiMode.ADVANCED) {
        baseFeatures + advancedFeatures
    } else {
        baseFeatures
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sonix") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // API Mode Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OptionChip(
                    selected = apiMode == ApiMode.SIMPLE,
                    onClick = { onApiModeChange(ApiMode.SIMPLE) },
                    label = "Simple"
                )
                OptionChip(
                    selected = apiMode == ApiMode.ADVANCED,
                    onClick = { onApiModeChange(ApiMode.ADVANCED) },
                    label = "Advanced"
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(visibleFeatures) { feature ->
                    FeatureCard(
                        title = feature.name,
                        description = feature.description,
                        onClick = { onFeatureClick(feature.name) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalibraScreen(onBack: () -> Unit, onFeatureClick: (String) -> Unit) {
    val analysisFeatures = listOf(
        FeatureItem("Pitch", "Pitch detection & processing"),
        FeatureItem("VAD", "Voice activity detection"),
        FeatureItem("Breath Monitor", "Breath duration tracking"),
        FeatureItem("Vocal Range", "Detect vocal range"),
        FeatureItem("Speaking Pitch", "Speaking pitch (shruti)")
    )

    val realtimeEvalFeatures = listOf(
        FeatureItem("Singalong", "Sing along evaluation"),
        FeatureItem("Singafter", "Call and response")
    )

    val offlineEvalFeatures = listOf(
        FeatureItem("Melody Eval", "Melody matching"),
        FeatureItem("Note Eval", "Note accuracy")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calibra") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Analysis Section
            item {
                SectionHeader("Analysis")
            }
            items(analysisFeatures) { feature ->
                FeatureCard(
                    title = feature.name,
                    description = feature.description,
                    onClick = { onFeatureClick(feature.name) }
                )
            }

            // Realtime Evaluation Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader("Realtime Evaluation")
            }
            items(realtimeEvalFeatures) { feature ->
                FeatureCard(
                    title = feature.name,
                    description = feature.description,
                    onClick = { onFeatureClick(feature.name) }
                )
            }

            // Offline Evaluation Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader("Offline Evaluation")
            }
            items(offlineEvalFeatures) { feature ->
                FeatureCard(
                    title = feature.name,
                    description = feature.description,
                    onClick = { onFeatureClick(feature.name) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun FeatureCard(title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SonixFeatureScreen(
    feature: String,
    apiMode: ApiMode,
    onBack: () -> Unit,
    context: ComponentActivity
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(feature) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            SectionCard {
                when (feature) {
                    "Playback" -> if (apiMode == ApiMode.SIMPLE) PlaybackSectionSimplified(context) else PlaybackSection(context)
                    "Recording" -> if (apiMode == ApiMode.SIMPLE) RecordingSectionSimplified(context) else RecordingSection(context)
                    "Multi-Track" -> if (apiMode == ApiMode.SIMPLE) MultiTrackSectionSimplified(context) else MultiTrackSection(context)
                    "Metronome" -> if (apiMode == ApiMode.SIMPLE) MetronomeSectionSimplified() else MetronomeSection()
                    "MIDI Synthesis" -> if (apiMode == ApiMode.SIMPLE) MidiSectionSimplified(context) else MidiSection(context)
                    "Decoding" -> DecodingSection(context)
                    "Parser" -> ParserSection(context)
                    else -> Text("Unknown feature: $feature")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalibraFeatureScreen(feature: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(feature) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            SectionCard {
                when (feature) {
                    "Pitch" -> RealtimePitchSection()
                    "VAD" -> RealtimePitchSection() // VAD is part of RealtimePitchSection
                    "Breath Monitor" -> BreathMonitorSection()
                    "Vocal Range" -> VocalRangeSection()
                    "Speaking Pitch" -> SpeakingPitchDetectorSection()
                    "Singalong" -> SingalongPracticeSection()
                    "Singafter" -> SingafterPracticeSection()
                    "Melody Eval" -> OfflineAnalysisSection()
                    "Note Eval" -> OfflineAnalysisSection()
                    else -> Text("Unknown feature: $feature")
                }
            }
        }
    }
}

@Composable
fun SectionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}
