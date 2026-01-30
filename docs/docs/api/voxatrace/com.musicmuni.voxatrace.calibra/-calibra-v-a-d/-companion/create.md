//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraVAD](../index.md)/[Companion](index.md)/[create](create.md)

# create

[common]\
fun [create](create.md)(config: [VADConfig](../../../com.musicmuni.voxatrace.calibra.model/-v-a-d-config/index.md), modelProvider: [VADModelProvider](../../../com.musicmuni.voxatrace.calibra.model/-v-a-d-model-provider/index.md)): [CalibraVAD](../index.md)

Create VAD with config and model provider.

ADR-001 compliant: Factory takes (config, dependency).

```kotlin
val config = VADConfig.Builder()
    .preset(VADConfig.SPEECH)
    .threshold(0.4f)
    .build()
val vad = CalibraVAD.create(config, VADModelProvider.Speech { ModelLoader.loadSpeechVAD() })
```

#### Return

Configured CalibraVAD instance

#### Parameters

common

| | |
|---|---|
| config | VAD configuration |
| modelProvider | Type-safe model provider that supplies the model |

[common]\
fun [create](create.md)(modelProvider: [VADModelProvider](../../../com.musicmuni.voxatrace.calibra.model/-v-a-d-model-provider/index.md)): [CalibraVAD](../index.md)

Create VAD with model provider (convenience, uses default config for backend).

Backend is inferred from the provider type. Each backend has appropriate defaults baked in. Use VADConfig.Builder for customization.

```kotlin
// GENERAL backend (no model required)
val vad = CalibraVAD.create(VADModelProvider.General)

// SPEECH backend (Silero)
val vad = CalibraVAD.create(VADModelProvider.Speech { ModelLoader.loadSpeechVAD() })

// SINGING backend (YAMNet)
val vad = CalibraVAD.create(VADModelProvider.Singing { ModelLoader.loadSingingVAD() })

// SINGING_REALTIME backend (SwiftF0)
val vad = CalibraVAD.create(VADModelProvider.SingingRealtime { ModelLoader.loadSingingRealtimeVAD() })
```

#### Return

Configured CalibraVAD instance

#### Parameters

common

| | |
|---|---|
| modelProvider | Type-safe model provider that determines backend |
