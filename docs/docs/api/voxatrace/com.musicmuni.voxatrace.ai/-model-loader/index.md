---
sidebar_label: "ModelLoader"
---


# ModelLoader

[common]\
expect object [ModelLoader](index.md)

Platform-specific model loader.

Provides access to bundled ONNX models for VAD and pitch detection. Method names align with public VADBackend enum values.

[android]\
actual object [ModelLoader](index.md)

Android implementation of ModelLoader.

Loads models from downloaded files via [ModelDownloader](../-model-downloader/index.md). After `VT.initialize()`, models are downloaded in background and loaded on demand.

[ios]\
actual object [ModelLoader](index.md)

iOS implementation of ModelLoader.

Loads models from downloaded files via [ModelDownloader](../-model-downloader/index.md). After `VT.initialize()`, models are downloaded in background and loaded on demand.

## Functions

| Name | Summary |
|---|---|
| [configure](configure.md) | [common]<br/>expect fun [configure](configure.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?)<br/>Configure the model loader. Must be called before loading any models.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [configure](configure.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?) |
| [hasSingingRealtimeVAD](has-singing-realtime-v-a-d.md) | [common]<br/>expect fun [hasSingingRealtimeVAD](has-singing-realtime-v-a-d.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if Singing Realtime VAD model is available.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [hasSingingRealtimeVAD](has-singing-realtime-v-a-d.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hasSingingVAD](has-singing-v-a-d.md) | [common]<br/>expect fun [hasSingingVAD](has-singing-v-a-d.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if Singing VAD models are available (both YAMNet and classifier).<br/>[android, ios]<br/>[android, ios]<br/>actual fun [hasSingingVAD](has-singing-v-a-d.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hasSpeechVAD](has-speech-v-a-d.md) | [common]<br/>expect fun [hasSpeechVAD](has-speech-v-a-d.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if Speech VAD model is available.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [hasSpeechVAD](has-speech-v-a-d.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hasSwiftF0](has-swift-f0.md) | [common]<br/>expect fun [hasSwiftF0](has-swift-f0.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if SwiftF0 model is available.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [hasSwiftF0](has-swift-f0.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [loadSingingRealtimeVAD](load-singing-realtime-v-a-d.md) | [common]<br/>expect fun [loadSingingRealtimeVAD](load-singing-realtime-v-a-d.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Load the Singing Realtime VAD model (SwiftF0). Use with VADBackend.SINGING_REALTIME.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [loadSingingRealtimeVAD](load-singing-realtime-v-a-d.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html) |
| [loadSingingVAD](load-singing-v-a-d.md) | [common]<br/>expect fun [loadSingingVAD](load-singing-v-a-d.md)(): [SingingVADModels](../-singing-v-a-d-models/index.md)<br/>Load the Singing VAD models (YAMNet + classifier). Use with VADBackend.SINGING.<br/>[android, ios]<br/>[android]<br/>actual fun [loadSingingVAD](load-singing-v-a-d.md)(): [SingingVADModels](../-singing-v-a-d-models/index.md)<br/>[ios]<br/>actual fun [loadSingingVAD](load-singing-v-a-d.md)(): SingingVADModels |
| [loadSpeechVAD](load-speech-v-a-d.md) | [common]<br/>expect fun [loadSpeechVAD](load-speech-v-a-d.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Load the Speech VAD model (Silero). Use with VADBackend.SPEECH.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [loadSpeechVAD](load-speech-v-a-d.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html) |
| [loadSwiftF0](load-swift-f0.md) | [common]<br/>expect fun [loadSwiftF0](load-swift-f0.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Load the SwiftF0 pitch detection model. Use with CalibraPitch for pitch detection.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [loadSwiftF0](load-swift-f0.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html) |
