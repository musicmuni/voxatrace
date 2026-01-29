---
sidebar_position: 3
---

# Voice Recorder

Build a voice recorder with level monitoring, format options, and playback.

## What You'll Build

A complete voice recording app:
- Record to M4A, MP3, or WAV
- Real-time level meter
- Playback recorded audio
- List and manage recordings

## Implementation

### Data Model

```kotlin
data class Recording(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val path: String,
    val durationMs: Long,
    val format: AudioFormat,
    val createdAt: Long = System.currentTimeMillis()
)
```

### ViewModel

```kotlin
class VoiceRecorderViewModel : ViewModel() {
    private var recorder: SonixRecorder? = null
    private var player: SonixPlayer? = null

    private val _state = MutableStateFlow(RecorderState())
    val state: StateFlow<RecorderState> = _state.asStateFlow()

    data class RecorderState(
        val isRecording: Boolean = false,
        val isPlaying: Boolean = false,
        val recordingDuration: Long = 0,
        val playbackPosition: Long = 0,
        val playbackDuration: Long = 0,
        val level: Float = 0f,
        val recordings: List<Recording> = emptyList(),
        val selectedFormat: AudioFormat = AudioFormat.M4A
    )

    private val recordingsDir: String
        get() = "${context.filesDir}/recordings"

    init {
        loadRecordings()
    }

    fun setFormat(format: AudioFormat) {
        _state.update { it.copy(selectedFormat = format) }
    }

    fun startRecording(name: String) {
        val extension = when (_state.value.selectedFormat) {
            AudioFormat.M4A -> "m4a"
            AudioFormat.MP3 -> "mp3"
            AudioFormat.WAV -> "wav"
        }
        val path = "$recordingsDir/${name}_${System.currentTimeMillis()}.$extension"

        recorder = SonixRecorder.create(
            path,
            SonixRecorderConfig.Builder()
                .format(_state.value.selectedFormat)
                .onLevelUpdate { level ->
                    _state.update { it.copy(level = level) }
                }
                .build()
        )

        recorder?.start()
        _state.update { it.copy(isRecording = true) }

        // Update duration
        viewModelScope.launch {
            recorder?.duration?.collect { duration ->
                _state.update { it.copy(recordingDuration = duration) }
            }
        }
    }

    fun stopRecording() {
        recorder?.stop()
        recorder?.release()

        val recording = Recording(
            name = "Recording ${_state.value.recordings.size + 1}",
            path = recorder?.outputPath ?: "",
            durationMs = _state.value.recordingDuration,
            format = _state.value.selectedFormat
        )

        recorder = null
        _state.update {
            it.copy(
                isRecording = false,
                recordingDuration = 0,
                level = 0f,
                recordings = it.recordings + recording
            )
        }

        saveRecordings()
    }

    suspend fun playRecording(recording: Recording) {
        player?.release()
        player = SonixPlayer.create(recording.path)

        _state.update {
            it.copy(
                isPlaying = true,
                playbackDuration = player?.duration ?: 0
            )
        }

        viewModelScope.launch {
            player?.currentTime?.collect { position ->
                _state.update { it.copy(playbackPosition = position) }
            }
        }

        viewModelScope.launch {
            player?.isPlaying?.collect { playing ->
                if (!playing && _state.value.isPlaying) {
                    _state.update { it.copy(isPlaying = false, playbackPosition = 0) }
                }
            }
        }

        player?.play()
    }

    fun stopPlayback() {
        player?.stop()
        player?.release()
        player = null
        _state.update { it.copy(isPlaying = false, playbackPosition = 0) }
    }

    fun deleteRecording(recording: Recording) {
        File(recording.path).delete()
        _state.update { it.copy(recordings = it.recordings - recording) }
        saveRecordings()
    }

    private fun loadRecordings() {
        // Load from SharedPreferences or database
    }

    private fun saveRecordings() {
        // Save to SharedPreferences or database
    }

    override fun onCleared() {
        recorder?.release()
        player?.release()
    }
}
```

### UI (Compose)

```kotlin
@Composable
fun VoiceRecorderScreen(viewModel: VoiceRecorderViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Format selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AudioFormat.values().forEach { format ->
                FilterChip(
                    selected = state.selectedFormat == format,
                    onClick = { viewModel.setFormat(format) },
                    label = { Text(format.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Level meter
        LevelMeter(
            level = state.level,
            modifier = Modifier.fillMaxWidth().height(40.dp)
        )

        // Duration display
        Text(
            text = formatDuration(state.recordingDuration),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Record button
        RecordButton(
            isRecording = state.isRecording,
            onClick = {
                if (state.isRecording) {
                    viewModel.stopRecording()
                } else {
                    viewModel.startRecording("Recording")
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Recordings list
        Text("Recordings", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(state.recordings) { recording ->
                RecordingItem(
                    recording = recording,
                    isPlaying = state.isPlaying,
                    onPlay = { viewModel.playRecording(it) },
                    onStop = { viewModel.stopPlayback() },
                    onDelete = { viewModel.deleteRecording(it) }
                )
            }
        }
    }
}

@Composable
fun LevelMeter(level: Float, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Background
        drawRoundRect(
            color = Color.Gray.copy(alpha = 0.3f),
            cornerRadius = CornerRadius(8f)
        )

        // Level bar
        val levelWidth = width * level
        val color = when {
            level < 0.5f -> Color.Green
            level < 0.8f -> Color.Yellow
            else -> Color.Red
        }
        drawRoundRect(
            color = color,
            size = Size(levelWidth, height),
            cornerRadius = CornerRadius(8f)
        )
    }
}

@Composable
fun RecordButton(isRecording: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(if (isRecording) Color.Red else Color.Red.copy(alpha = 0.7f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isRecording) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.White, CircleShape)
            )
        }
    }
}
```

## Key Features

### Format Selection

```kotlin
AudioFormat.M4A  // AAC compressed, small files, good quality
AudioFormat.MP3  // Universal compatibility
AudioFormat.WAV  // Uncompressed, large files, lossless
```

### Quality Presets

```kotlin
SonixRecorderConfig.VOICE     // 16kHz mono - smallest files
SonixRecorderConfig.STANDARD  // 44.1kHz stereo - balanced
SonixRecorderConfig.HIGH      // 48kHz stereo - best quality
```

### Real-time Level Monitoring

```kotlin
recorder?.level?.collect { level ->
    // level is 0.0 to 1.0
    updateVUMeter(level)
}
```

## Next Steps

- [Recording Audio Guide](/docs/guides/recording-audio) - Detailed recording options
- [Playing Audio Guide](/docs/guides/playing-audio) - Playback features
- [Demo App](https://github.com/musicmuni/voxatrace-demos) - Full source
