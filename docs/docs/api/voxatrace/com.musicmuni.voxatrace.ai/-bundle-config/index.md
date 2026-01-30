//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.ai](../index.md)/[BundleConfig](index.md)

# BundleConfig

[common]\
object [BundleConfig](index.md)

Configuration for AI model bundle downloads.

Bundle versioning is independent of SDK versioning. Multiple SDK versions may use the same bundle version.

URL Design: Every URL returns a zip archive that extracts to a folder. Works uniformly for single-file or multi-file downloads.

Models are downloaded on-demand when features are first used.

## Types

| Name | Summary |
|---|---|
| [ModelFiles](-model-files/index.md) | [common]<br/>object [ModelFiles](-model-files/index.md)<br/>Expected model filenames after extraction |
| [URLs](-u-r-ls/index.md) | [common]<br/>object [URLs](-u-r-ls/index.md)<br/>Per-feature download URLs (API-mirrored naming) |

## Properties

| Name | Summary |
|---|---|
| [BUNDLE_URL](-b-u-n-d-l-e_-u-r-l.md) | [common]<br/>const val [BUNDLE_URL](-b-u-n-d-l-e_-u-r-l.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Full bundle download URL - returns zip containing all models. Kept for legacy/optional full download. |
| [BUNDLE_VERSION](-b-u-n-d-l-e_-v-e-r-s-i-o-n.md) | [common]<br/>const val [BUNDLE_VERSION](-b-u-n-d-l-e_-v-e-r-s-i-o-n.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Bundle version - independent of SDK version |

## Functions

| Name | Summary |
|---|---|
| [urlForModel](url-for-model.md) | [common]<br/>fun [urlForModel](url-for-model.md)(filename: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Get the download URL for a specific model file. Returns the appropriate per-feature zip URL. |
