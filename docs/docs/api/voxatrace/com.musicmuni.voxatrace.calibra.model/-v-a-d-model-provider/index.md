//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../index.md)/[VADModelProvider](index.md)

# VADModelProvider

sealed class [VADModelProvider](index.md)

Type-safe model provider for VAD backends.

Each subclass carries the appropriate model loading closure for its backend. Backend is inferred from the provider type, enabling unified API.

## Zero-Config Usage (Recommended)

After `VT.initialize()`, models auto-load from bundled assets:

```kotlin
// Auto-loads bundled Silero model
CalibraVAD.create(VADModelProvider.speech())

// Auto-loads bundled YAMNet + classifier
CalibraVAD.create(VADModelProvider.singing())

// Auto-loads bundled SwiftF0 model
CalibraVAD.create(VADModelProvider.singingRealtime())

// No model required
CalibraVAD.create(VADModelProvider.general)
```

## Custom Model Loading

Pass a custom loader to override bundled models:

```kotlin
CalibraVAD.create(VADModelProvider.speech { loadCustomSileroModel() })
CalibraVAD.create(VADModelProvider.singing { loadCustomYAMNetModels() })
```

#### Inheritors

| |
|---|
| [General](-general/index.md) |
| [Speech](-speech/index.md) |
| [SingingRealtime](-singing-realtime/index.md) |
| [Singing](-singing/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
| [General](-general/index.md) | [common]<br/>data object [General](-general/index.md) : [VADModelProvider](index.md)<br/>GENERAL backend - no model required. Uses RMS-based heuristics for basic voice detection. |
| [Singing](-singing/index.md) | [common]<br/>data class [Singing](-singing/index.md)(val modelProvider: () -&gt; [SingingVADModels](../../com.musicmuni.voxatrace.ai/-singing-v-a-d-models/index.md)) : [VADModelProvider](index.md)<br/>SINGING backend - YAMNet + classifier models. High-accuracy singing/speech vs instrumental classification. |
| [SingingRealtime](-singing-realtime/index.md) | [common]<br/>data class [SingingRealtime](-singing-realtime/index.md)(val modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) : [VADModelProvider](index.md)<br/>SINGING_REALTIME backend - single SwiftF0 ONNX model. Low-latency pitch-based singing detection. |
| [Speech](-speech/index.md) | [common]<br/>data class [Speech](-speech/index.md)(val modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) : [VADModelProvider](index.md)<br/>SPEECH backend - single Silero ONNX model. State-of-the-art accuracy for speech detection. |
