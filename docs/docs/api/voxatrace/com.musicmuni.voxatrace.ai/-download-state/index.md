---
sidebar_label: "DownloadState"
---


# DownloadState

sealed class [DownloadState](index.md)

Download state for tracking model bundle download progress.

#### Inheritors

| |
|---|
| [Idle](-idle/index.md) |
| [Downloading](-downloading/index.md) |
| [Completed](-completed/index.md) |
| [Failed](-failed/index.md) |

## Types

| Name | Summary |
|---|---|
| [Completed](-completed/index.md) | [common]<br/>data object [Completed](-completed/index.md) : [DownloadState](index.md)<br/>All models downloaded and extracted successfully |
| [Downloading](-downloading/index.md) | [common]<br/>data class [Downloading](-downloading/index.md)(val progress: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)) : [DownloadState](index.md)<br/>Download in progress with progress fraction (0.0 to 1.0) |
| [Failed](-failed/index.md) | [common]<br/>data class [Failed](-failed/index.md)(val error: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [DownloadState](index.md)<br/>Download or extraction failed |
| [Idle](-idle/index.md) | [common]<br/>data object [Idle](-idle/index.md) : [DownloadState](index.md)<br/>Initial state - no download in progress |
