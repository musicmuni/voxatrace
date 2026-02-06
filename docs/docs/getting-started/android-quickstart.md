---
sidebar_position: 2
---

# Android Quickstart

Build a simple pitch detector in 5 minutes.

## What You'll Build

A minimal app that:
1. Records audio from the microphone
2. Detects pitch in real-time
3. Displays the detected note

## Prerequisites

- Android Studio Arctic Fox or later
- VoxaTrace installed (see [Installation](./installation))
- Microphone permission granted

## Step 1: Request Microphone Permission

In your Activity or Fragment:

```kotlin
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val RECORD_AUDIO_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestMicrophonePermission()
    }

    private fun requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION
            )
        } else {
            startPitchDetection()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_PERMISSION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startPitchDetection()
        }
    }
}
```

## Step 2: Create the Pitch Detector

```kotlin
import com.musicmuni.voxatrace.calibra.CalibraPitch
import com.musicmuni.voxatrace.sonix.SonixRecorder
import com.musicmuni.voxatrace.sonix.SonixRecorderConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {
    private var recorder: SonixRecorder? = null
    private var detector: CalibraPitch.Detector? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private fun startPitchDetection() {
        scope.launch {
            // Create recorder with default voice settings
            recorder = SonixRecorder.createTemporary(SonixRecorderConfig.VOICE)

            // Create pitch detector with default settings
            detector = CalibraPitch.createDetector()

            // Start recording
            recorder?.start()

            // Process audio buffers
            recorder?.audioBuffers?.collect { buffer ->
                val samples = FloatArray(buffer.sampleCount)
                buffer.fillFloatSamples(samples)

                // Detect pitch
                val point = detector?.detect(samples, buffer.sampleRate) ?: return@collect

                // Update UI
                if (point.pitch > 0) {
                    val note = pitchToNote(point.pitch)
                    updatePitchDisplay(note, point.pitch, point.confidence)
                } else {
                    updatePitchDisplay("--", 0f, 0f)
                }
            }
        }
    }

    private fun updatePitchDisplay(note: String, frequency: Float, confidence: Float) {
        runOnUiThread {
            // Update your UI here
            // textViewNote.text = note
            // textViewFrequency.text = "${frequency.toInt()} Hz"
            println("Note: $note, Frequency: ${frequency.toInt()} Hz, Confidence: ${(confidence * 100).toInt()}%")
        }
    }

    private fun pitchToNote(frequency: Float): String {
        val noteNames = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
        val a4 = 440.0
        val semitones = 12 * kotlin.math.log2(frequency / a4) + 69
        val noteIndex = (semitones.toInt() % 12 + 12) % 12
        val octave = (semitones.toInt() / 12) - 1
        return "${noteNames[noteIndex]}$octave"
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        detector?.close()
        recorder?.release()
    }
}
```

## What You'll See

When you sing into the microphone, Logcat will show:

```text
Note: A4, Frequency: 440 Hz, Confidence: 92%
Note: A4, Frequency: 441 Hz, Confidence: 89%
Note: B4, Frequency: 494 Hz, Confidence: 87%
Note: --, Frequency: 0 Hz, Confidence: 0%   ← breath/silence
Note: C5, Frequency: 523 Hz, Confidence: 91%
```

The app is:

1. Recording audio buffers from the microphone (~50ms chunks)
2. Running pitch detection on each buffer
3. Converting frequency to musical note name
4. Showing confidence (how certain the detection is)

**Troubleshooting:**

- Seeing lots of `--` entries? Make sure microphone permission is granted
- Low confidence values? Sing closer to the device, reduce background noise
- No output at all? Check that `recorder?.start()` is being called

## Step 3: Add UI (Optional)

Create a simple layout in `activity_main.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="16dp">

    <TextView
        android:id="@+id/textViewNote"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="--"
        android:textSize="72sp"
        android:textStyle="bold" />

    <TextView
        android:id="@+id/textViewFrequency"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="0 Hz"
        android:textSize="24sp"
        android:layout_marginTop="8dp" />

    <ProgressBar
        android:id="@+id/progressConfidence"
        style="@style/Widget.AppCompat.ProgressBar.Horizontal"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        android:max="100"
        android:progress="0"
        android:layout_marginTop="16dp" />

</LinearLayout>
```

## Complete Example with Compose

If you're using Jetpack Compose:

```kotlin
@Composable
fun PitchDetectorScreen() {
    var note by remember { mutableStateOf("--") }
    var frequency by remember { mutableStateOf(0f) }
    var confidence by remember { mutableStateOf(0f) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        var recorder: SonixRecorder? = null
        var detector: CalibraPitch.Detector? = null

        scope.launch {
            recorder = SonixRecorder.createTemporary(SonixRecorderConfig.VOICE)
            detector = CalibraPitch.createDetector()
            recorder?.start()

            recorder?.audioBuffers?.collect { buffer ->
                val samples = FloatArray(buffer.sampleCount)
                buffer.fillFloatSamples(samples)
                val point = detector?.detect(samples, buffer.sampleRate) ?: return@collect

                if (point.pitch > 0) {
                    note = pitchToNote(point.pitch)
                    frequency = point.pitch
                    confidence = point.confidence
                } else {
                    note = "--"
                    frequency = 0f
                    confidence = 0f
                }
            }
        }

        onDispose {
            detector?.close()
            recorder?.release()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = note, fontSize = 72.sp, fontWeight = FontWeight.Bold)
        Text(text = "${frequency.toInt()} Hz", fontSize = 24.sp)
        LinearProgressIndicator(
            progress = confidence,
            modifier = Modifier.width(200.dp).padding(top = 16.dp)
        )
    }
}
```

## Next Steps

- [Detecting Pitch Guide](../guides/detecting-pitch) - Deep dive into pitch detection options
- [Recording Audio Guide](../guides/recording-audio) - Learn about recording features
- [Live Evaluation Guide](../guides/live-evaluation) - Score singing against reference
