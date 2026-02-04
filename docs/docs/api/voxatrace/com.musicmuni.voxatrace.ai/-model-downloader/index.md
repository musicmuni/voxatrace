---
sidebar_label: "ModelDownloader"
---


# ModelDownloader

[common]\
expect object [ModelDownloader](index.md)

Downloads and extracts AI model bundles.

All URLs return zip archives that are extracted to the models folder. This unified approach works for single-file or multi-file bundles.

## Usage

Models are downloaded automatically after `VT.initialize()`:

```kotlin
VT.initializeWithApiKey("api_key", context)  // Starts background download

// Later, when using AI features, models are awaited if not ready
val vad = CalibraVAD.create(VADModelProvider.speech())
```

## Sideloading

Downloaded models are fallback only. Custom providers registered via [AIModelRegistry](../-a-i-model-registry/index.md) take priority:

```kotlin
AIModelRegistry.registerSwiftF0 { customLoader() }  // Takes priority
```

[android]\
actual object [ModelDownloader](index.md)

Android implementation of ModelDownloader.

Downloads model bundle from BundleConfig.BUNDLE_URL, extracts to internal storage, and provides model bytes on demand.

[ios]\
actual object [ModelDownloader](index.md)

iOS implementation of ModelDownloader.

Downloads model bundle from BundleConfig.BUNDLE_URL, extracts to Application Support, and provides model bytes on demand.

## Properties

| Name | Summary |
|---|---|
| [downloadState](download-state.md) | [common]<br/>expect val [downloadState](download-state.md): StateFlow&lt;[DownloadState](../-download-state/index.md)&gt;<br/>Download state for observing progress<br/>[android, ios]<br/>[android]<br/>actual val [downloadState](download-state.md): StateFlow&lt;[DownloadState](../-download-state/index.md)&gt;<br/>[ios]<br/>actual val [downloadState](download-state.md): StateFlow&lt;DownloadState&gt; |

## Functions

| Name | Summary |
|---|---|
| [awaitModel](await-model.md) | [common]<br/>expect suspend fun [awaitModel](await-model.md)(filename: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Await until a specific model is available (suspends if downloading).<br/>[android, ios]<br/>[android, ios]<br/>actual suspend fun [awaitModel](await-model.md)(filename: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html) |
| [configure](configure.md) | [common]<br/>expect fun [configure](configure.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?)<br/>Configure the downloader with platform context. Called internally by VT.initialize().<br/>[android, ios]<br/>[android, ios]<br/>actual fun [configure](configure.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?) |
| [getModelsDirectory](get-models-directory.md) | [common]<br/>expect fun [getModelsDirectory](get-models-directory.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Get path to models directory<br/>[android, ios]<br/>[android, ios]<br/>actual fun [getModelsDirectory](get-models-directory.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [isDownloadComplete](is-download-complete.md) | [common]<br/>expect fun [isDownloadComplete](is-download-complete.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if all models are downloaded<br/>[android, ios]<br/>[android, ios]<br/>actual fun [isDownloadComplete](is-download-complete.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [isModelDownloaded](is-model-downloaded.md) | [common]<br/>expect fun [isModelDownloaded](is-model-downloaded.md)(filename: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if a specific model is downloaded<br/>[android, ios]<br/>[android, ios]<br/>actual fun [isModelDownloaded](is-model-downloaded.md)(filename: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [startBackgroundDownload](start-background-download.md) | [common]<br/>expect fun [startBackgroundDownload](start-background-download.md)()<br/>Start background download of bundle (non-blocking)<br/>[android, ios]<br/>[android, ios]<br/>actual fun [startBackgroundDownload](start-background-download.md)() |
