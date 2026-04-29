package com.musicmuni.voxatrace.demo

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
import androidx.activity.compose.BackHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.musicmuni.voxatrace.demo.sections.recording.view.RecordingView
import com.musicmuni.voxatrace.demo.sections.playback.view.PlaybackView
import com.musicmuni.voxatrace.demo.sections.multitrack.view.MultiTrackView
import com.musicmuni.voxatrace.demo.sections.metronome.view.MetronomeView
import com.musicmuni.voxatrace.demo.sections.midi.view.MIDIView
import com.musicmuni.voxatrace.demo.sections.decoding.view.DecodingView
import com.musicmuni.voxatrace.demo.sections.parser.view.ParserView
import com.musicmuni.voxatrace.demo.sections.pitch.view.PitchView
import com.musicmuni.voxatrace.demo.sections.vad.view.VADView
import com.musicmuni.voxatrace.demo.sections.breathmonitor.view.BreathMonitorView
import com.musicmuni.voxatrace.demo.sections.vocalrange.view.VocalRangeView
import com.musicmuni.voxatrace.demo.sections.speakingpitch.view.SpeakingPitchView
import com.musicmuni.voxatrace.demo.sections.singalong.view.SingalongView
import com.musicmuni.voxatrace.demo.sections.singafter.view.SingafterView
import com.musicmuni.voxatrace.demo.sections.melodyeval.view.MelodyEvalView
import com.musicmuni.voxatrace.demo.sections.noteeval.view.NoteEvalView
import com.musicmuni.voxatrace.demo.sections.intonation.view.IntonationView
import com.musicmuni.voxatrace.demo.sections.agility.view.AgilityView
import com.musicmuni.voxatrace.demo.sections.songmatching.view.SongMatchingView
import com.musicmuni.voxatrace.demo.sections.voiceprofile.view.VoiceProfileView
import com.musicmuni.voxatrace.demo.sections.pitchanalysis.view.PitchAnalysisView
import com.musicmuni.voxatrace.demo.sections.effects.view.EffectsView
import com.musicmuni.voxatrace.VT
import com.musicmuni.voxatrace.ai.AIModels
import com.musicmuni.voxatrace.exceptions.VoxaTraceKilledException
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Napier.base(DebugAntilog())

        // Request microphone permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

        // ⚠️ DEMO ONLY - DO NOT USE IN PRODUCTION
        // This uses direct API key initialization which embeds credentials in the app.
        // For production apps, use one of these secure methods:
        //   - VT.initialize(proxyEndpoint) - Recommended for apps with backend servers
        //   - VT.initializeWithAttestation(apiKey) - For apps without backends (requires Play Store)
        // See docs/client-proxy-setup.md and docs/client-attestation-guide.md
        //
        // === AIModels Preload Examples ===
        //
        // Default: Just pitch model (most apps)
        // preload = AIModels.DEFAULT
        //
        // Pitch + Speech VAD
        // preload = setOf(AIModels.Pitch.REALTIME, AIModels.VAD.SPEECH)
        //
        // All models
        // preload = AIModels.ALL
        //
        // No preload (fully lazy, download on first use)
        // preload = AIModels.NONE
        try {
            VT.initializeForServer(
                apiKey = BuildConfig.VOXATRACE_API_KEY,
                context = this,
                debugLogging = BuildConfig.DEBUG,
                preload = AIModels.ALL
            )
            Napier.i("VoxaTrace SDK initialized successfully")
            showMainContent()
        } catch (e: VoxaTraceKilledException) {
            Napier.e("VoxaTrace initialization failed: ${e.message}")
            showErrorContent(e.message ?: "License validation failed")
        }
    }

    private fun showMainContent() {
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VoxaTraceDemoApp()
                }
            }
        }
    }

    private fun showErrorContent(message: String) {
        setContent {
            MaterialTheme {
                LicenseErrorScreen(message)
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

@Composable
fun InitializingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Initializing VoxaTrace...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

sealed class Screen {
    data object Home : Screen()
    data object Sonix : Screen()
    data object Tona : Screen()
    data object Accura : Screen()
    data object Tessera : Screen()
    data object Calibra : Screen()
    data class SonixFeature(val name: String) : Screen()
    data class TonaFeature(val name: String) : Screen()
    data class AccuraFeature(val name: String) : Screen()
    data class TesseraFeature(val name: String) : Screen()
    data class CalibraFeature(val name: String) : Screen()
}

@Composable
fun VoxaTraceDemoApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    when (val screen = currentScreen) {
        Screen.Home -> HomeScreen(
            onSonixClick = { currentScreen = Screen.Sonix },
            onTonaClick = { currentScreen = Screen.Tona },
            onAccuraClick = { currentScreen = Screen.Accura },
            onTesseraClick = { currentScreen = Screen.Tessera },
            onCalibraClick = { currentScreen = Screen.Calibra }
        )
        Screen.Sonix -> SonixScreen(
            onBack = { currentScreen = Screen.Home },
            onFeatureClick = { feature -> currentScreen = Screen.SonixFeature(feature) }
        )
        Screen.Tona -> ModuleScreen(
            title = "Tona",
            features = tonaFeatures,
            onBack = { currentScreen = Screen.Home },
            onFeatureClick = { feature -> currentScreen = Screen.TonaFeature(feature) }
        )
        Screen.Accura -> ModuleScreen(
            title = "Accura",
            features = accuraFeatures,
            onBack = { currentScreen = Screen.Home },
            onFeatureClick = { feature -> currentScreen = Screen.AccuraFeature(feature) }
        )
        Screen.Tessera -> ModuleScreen(
            title = "Tessera",
            features = tesseraFeatures,
            onBack = { currentScreen = Screen.Home },
            onFeatureClick = { feature -> currentScreen = Screen.TesseraFeature(feature) }
        )
        Screen.Calibra -> CalibraScreen(
            onBack = { currentScreen = Screen.Home },
            onFeatureClick = { feature -> currentScreen = Screen.CalibraFeature(feature) }
        )
        is Screen.SonixFeature -> FeatureScreen(screen.name, { currentScreen = Screen.Sonix })
        is Screen.TonaFeature -> FeatureScreen(screen.name, { currentScreen = Screen.Tona })
        is Screen.AccuraFeature -> FeatureScreen(screen.name, { currentScreen = Screen.Accura })
        is Screen.TesseraFeature -> FeatureScreen(screen.name, { currentScreen = Screen.Tessera })
        is Screen.CalibraFeature -> FeatureScreen(screen.name, { currentScreen = Screen.Calibra })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSonixClick: () -> Unit,
    onTonaClick: () -> Unit,
    onAccuraClick: () -> Unit,
    onTesseraClick: () -> Unit,
    onCalibraClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("VoxaTrace Demo") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CategoryCard(
                    title = "Sonix",
                    subtitle = "Audio Transport",
                    description = "Playback, Recording, MIDI, Metronome, Multi-Track",
                    onClick = onSonixClick
                )
            }
            item {
                CategoryCard(
                    title = "Tona",
                    subtitle = "Pitch Detection & Processing",
                    description = "Realtime Detection, Contour Extraction, Cleanup, Analysis",
                    onClick = onTonaClick
                )
            }
            item {
                CategoryCard(
                    title = "Accura",
                    subtitle = "Intonation Analysis",
                    description = "Pitch accuracy scoring (Equal Temperament / Just Intonation)",
                    onClick = onAccuraClick
                )
            }
            item {
                CategoryCard(
                    title = "Tessera",
                    subtitle = "Voice Metrics",
                    description = "Breath, Agility, Range, Speaking Pitch, Voice Profile",
                    onClick = onTesseraClick
                )
            }
            item {
                CategoryCard(
                    title = "Calibra",
                    subtitle = "Singing Evaluation",
                    description = "Singalong, Singafter, Melody Eval, Note Eval, VAD, Effects",
                    onClick = onCalibraClick
                )
            }
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
    onBack: () -> Unit,
    onFeatureClick: (String) -> Unit
) {
    // Handle system back gesture
    BackHandler(onBack = onBack)

    val features = listOf(
        FeatureItem("Playback", "Audio playback with pitch shifting"),
        FeatureItem("Recording", "Audio recording to M4A/MP3"),
        FeatureItem("Multi-Track", "Multi-track mixing"),
        FeatureItem("Metronome", "Click track with visual feedback"),
        FeatureItem("MIDI Synthesis", "SoundFont-based synthesis"),
        FeatureItem("Decoding", "Audio decode/encode"),
        FeatureItem("Parser", "Parse notation files")
    )

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(features) { feature ->
                FeatureCard(
                    title = feature.name,
                    description = feature.description,
                    onClick = { onFeatureClick(feature.name) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalibraScreen(onBack: () -> Unit, onFeatureClick: (String) -> Unit) {
    // Handle system back gesture
    BackHandler(onBack = onBack)

    val analysisFeatures = listOf(
        FeatureItem("VAD", "Voice activity detection"),
        FeatureItem("Effects", "Audio effects processing")
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

// Feature lists per module

val tonaFeatures = listOf(
    FeatureItem("Pitch", "Pitch detection & processing"),
    FeatureItem("Pitch Analysis", "Histogram & tonal segments")
)

val accuraFeatures = listOf(
    FeatureItem("Intonation", "Pitch accuracy (EQ/JI)")
)

val tesseraFeatures = listOf(
    FeatureItem("Breath Monitor", "Breath capacity & control"),
    FeatureItem("Vocal Agility", "Agility scoring"),
    FeatureItem("Vocal Range", "Detect vocal range"),
    FeatureItem("Song Matching", "Range-based song matching"),
    FeatureItem("Speaking Pitch", "Natural speaking pitch"),
    FeatureItem("Voice Profile", "Multi-metric dashboard")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleScreen(
    title: String,
    features: List<FeatureItem>,
    onBack: () -> Unit,
    onFeatureClick: (String) -> Unit
) {
    BackHandler(onBack = onBack)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
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
            items(features) { feature ->
                FeatureCard(
                    title = feature.name,
                    description = feature.description,
                    onClick = { onFeatureClick(feature.name) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureScreen(feature: String, onBack: () -> Unit) {
    BackHandler(onBack = onBack)

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
                    // Sonix
                    "Playback" -> PlaybackView()
                    "Recording" -> RecordingView()
                    "Multi-Track" -> MultiTrackView()
                    "Metronome" -> MetronomeView()
                    "MIDI Synthesis" -> MIDIView()
                    "Decoding" -> DecodingView()
                    "Parser" -> ParserView()
                    // Tona
                    "Pitch" -> PitchView()
                    "Pitch Analysis" -> PitchAnalysisView()
                    // Accura
                    "Intonation" -> IntonationView()
                    // Tessera
                    "Breath Monitor" -> BreathMonitorView()
                    "Vocal Agility" -> AgilityView()
                    "Vocal Range" -> VocalRangeView()
                    "Song Matching" -> SongMatchingView()
                    "Speaking Pitch" -> SpeakingPitchView()
                    "Voice Profile" -> VoiceProfileView()
                    // Calibra
                    "VAD" -> VADView()
                    "Effects" -> EffectsView()
                    "Singalong" -> SingalongView()
                    "Singafter" -> SingafterView()
                    "Melody Eval" -> MelodyEvalView()
                    "Note Eval" -> NoteEvalView()
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
