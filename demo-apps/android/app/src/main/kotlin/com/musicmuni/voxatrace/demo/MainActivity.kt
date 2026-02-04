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
import com.musicmuni.voxatrace.VT
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

        // Show loading UI while initializing
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InitializingScreen()
                }
            }
        }

        // Initialize VoxaTrace SDK using App Attestation (Play Integrity API).
        // This verifies the app is running on a genuine device before granting access.
        //
        // ALTERNATIVE: If you have a backend server, use proxy-based initialization:
        // ```
        // try {
        //     VT.initialize(
        //         proxyEndpoint = "https://your-server.com/api/voxatrace/register",
        //         context = this,
        //         debugLogging = BuildConfig.DEBUG
        //     )
        //     // SDK ready immediately - show main content
        // } catch (e: VoxaTraceKilledException) {
        //     // Handle license error
        // }
        // ```
        // See docs/client-proxy-setup.md for proxy server implementation.

        VT.initializeWithAttestation(
            apiKey = BuildConfig.VOXATRACE_API_KEY,
            context = this,
            debugLogging = BuildConfig.DEBUG,
            callback = object : VT.Companion.InitCallback {
                override fun onComplete(success: Boolean, error: String?) {
                    runOnUiThread {
                        if (success) {
                            Napier.i("VoxaTrace SDK initialized successfully")
                            showMainContent()
                        } else {
                            Napier.e("VoxaTrace initialization failed: $error")
                            showErrorContent(error ?: "License validation failed")
                        }
                    }
                }
            }
        )
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
    data object Calibra : Screen()
    data class SonixFeature(val name: String) : Screen()
    data class CalibraFeature(val name: String) : Screen()
}

@Composable
fun VoxaTraceDemoApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    when (val screen = currentScreen) {
        Screen.Home -> HomeScreen(
            onSonixClick = { currentScreen = Screen.Sonix },
            onCalibraClick = { currentScreen = Screen.Calibra }
        )
        Screen.Sonix -> SonixScreen(
            onBack = { currentScreen = Screen.Home },
            onFeatureClick = { feature -> currentScreen = Screen.SonixFeature(feature) }
        )
        Screen.Calibra -> CalibraScreen(
            onBack = { currentScreen = Screen.Home },
            onFeatureClick = { feature -> currentScreen = Screen.CalibraFeature(feature) }
        )
        is Screen.SonixFeature -> SonixFeatureScreen(
            feature = screen.name,
            onBack = { currentScreen = Screen.Sonix }
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
            TopAppBar(title = { Text("VoxaTrace Demo") })
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
    onBack: () -> Unit
) {
    // Handle system back gesture
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
                    "Playback" -> PlaybackView()
                    "Recording" -> RecordingView()
                    "Multi-Track" -> MultiTrackView()
                    "Metronome" -> MetronomeView()
                    "MIDI Synthesis" -> MIDIView()
                    "Decoding" -> DecodingView()
                    "Parser" -> ParserView()
                    else -> Text("Unknown feature: $feature")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalibraFeatureScreen(feature: String, onBack: () -> Unit) {
    // Handle system back gesture
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
                    "Pitch" -> PitchView()
                    "VAD" -> VADView()
                    "Breath Monitor" -> BreathMonitorView()
                    "Vocal Range" -> VocalRangeView()
                    "Speaking Pitch" -> SpeakingPitchView()
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
