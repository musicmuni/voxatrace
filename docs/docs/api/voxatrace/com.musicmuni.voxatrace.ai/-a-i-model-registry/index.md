---
sidebar_label: "AIModelRegistry"
---


# AIModelRegistry

[common]\
object [AIModelRegistry](index.md)

Global registry for AI model providers.

Allows apps to register model loaders once at startup, which are then automatically used by CalibraPitch and CalibraVAD factories when creating AI-based detectors.

## Zero-Config Usage (Recommended)

After calling `VT.initialize()`, models are auto-loaded from bundled assets:

```kotlin
// At app startup
VT.initializeWithApiKey("api_key", context)

// Later, anywhere in the app - models auto-load from bundled assets
val detector = CalibraPitch.createDetector(config)
val vad = CalibraVAD.create(VADModelProvider.speech())
```

## Sideloading Custom Models

To use custom models instead of bundled ones, register providers:

```kotlin
// Override globally
AIModelRegistry.registerSwiftF0 { loadCustomPitchModel() }
AIModelRegistry.registerSpeechVAD { loadCustomSileroModel() }

// Or per-call (supported by factory methods)
val vad = CalibraVAD.create(VADModelProvider.speech { customLoader() })
```

## Functions

| Name | Summary |
|---|---|
| [clear](clear.md) | [common]<br/>fun [clear](clear.md)()<br/>Clear all registered providers (for testing). |
| [hasSingingVAD](has-singing-v-a-d.md) | [common]<br/>fun [hasSingingVAD](has-singing-v-a-d.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if Singing VAD models are available (registered or bundled). |
| [hasSpeechVAD](has-speech-v-a-d.md) | [common]<br/>fun [hasSpeechVAD](has-speech-v-a-d.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if Speech VAD model is available (registered or bundled). |
| [hasSwiftF0](has-swift-f0.md) | [common]<br/>fun [hasSwiftF0](has-swift-f0.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if SwiftF0 model is available (registered or bundled). |
| [registerSingingVAD](register-singing-v-a-d.md) | [common]<br/>fun [registerSingingVAD](register-singing-v-a-d.md)(provider: () -&gt; [SingingVADModels](../-singing-v-a-d-models/index.md))<br/>Register Singing VAD model provider for automatic loading. Provides both YAMNet and classifier models. |
| [registerSpeechVAD](register-speech-v-a-d.md) | [common]<br/>fun [registerSpeechVAD](register-speech-v-a-d.md)(provider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html))<br/>Register Speech VAD (Silero) model provider for automatic loading. |
| [registerSwiftF0](register-swift-f0.md) | [common]<br/>fun [registerSwiftF0](register-swift-f0.md)(provider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html))<br/>Register SwiftF0 model provider for automatic loading. Used by CalibraPitch and SingingRealtime VAD. |
