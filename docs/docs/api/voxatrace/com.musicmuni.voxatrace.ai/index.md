//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.ai](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AIModelRegistry](-a-i-model-registry/index.md) | [common]<br/>object [AIModelRegistry](-a-i-model-registry/index.md)<br/>Global registry for AI model providers. |
| [BundleConfig](-bundle-config/index.md) | [common]<br/>object [BundleConfig](-bundle-config/index.md)<br/>Configuration for AI model bundle downloads. |
| [DownloadState](-download-state/index.md) | [common]<br/>sealed class [DownloadState](-download-state/index.md)<br/>Download state for tracking model bundle download progress. |
| [ModelDownloader](-model-downloader/index.md) | [common]<br/>expect object [ModelDownloader](-model-downloader/index.md)<br/>Downloads and extracts AI model bundles on demand.<br/>[android]<br/>actual object [ModelDownloader](-model-downloader/index.md)<br/>Android implementation of ModelDownloader.<br/>[ios]<br/>actual object [ModelDownloader](-model-downloader/index.md)<br/>iOS implementation of ModelDownloader. |
| [ModelLoader](-model-loader/index.md) | [common]<br/>expect object [ModelLoader](-model-loader/index.md)<br/>Platform-specific model loader.<br/>[android]<br/>actual object [ModelLoader](-model-loader/index.md)<br/>Android implementation of ModelLoader.<br/>[ios]<br/>actual object [ModelLoader](-model-loader/index.md)<br/>iOS implementation of ModelLoader. |
| [SingingVADModels](-singing-v-a-d-models/index.md) | [common]<br/>data class [SingingVADModels](-singing-v-a-d-models/index.md)(val yamnet: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), val classifier: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html))<br/>Container for Singing VAD model files. |
