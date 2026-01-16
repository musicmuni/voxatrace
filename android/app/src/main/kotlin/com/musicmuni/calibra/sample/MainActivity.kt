package com.musicmuni.calibra.sample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.musicmuni.vozos.calibra.Calibra
import com.musicmuni.vozos.sonix.Sonix

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted
        } else {
            // Permission denied
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SDKs
        Calibra.init()
        Sonix.initialize(BuildConfig.SONIX_API_KEY, this)

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
                    CalibraSampleApp()
                }
            }
        }
    }
}

@Composable
fun CalibraSampleApp() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Calibra Sample App",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        HorizontalDivider()

        SectionCard {
            SingalongPracticeSection()
        }
        SectionCard {
            SingafterPracticeSection()
        }

        HorizontalDivider()

        Text(
            text = "Other Demos",
            style = MaterialTheme.typography.titleSmall
        )

        SectionCard {
            RealtimePitchSection()
        }
        SectionCard {
            EffectsSection()
        }
        SectionCard {
            OfflineAnalysisSection()
        }
        SectionCard {
            VocalRangeSection()
        }
        SectionCard {
            BreathMonitorSection()
        }
        SectionCard {
            SpeakingPitchDetectorSection()
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SectionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}
