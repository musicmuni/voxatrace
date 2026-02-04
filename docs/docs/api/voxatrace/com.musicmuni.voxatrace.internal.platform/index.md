---
sidebar_label: "platform"
---


# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AppLifecycleObserver](-app-lifecycle-observer/index.md) | [common]<br/>expect class [AppLifecycleObserver](-app-lifecycle-observer/index.md)(onForeground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html), onBackground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html))<br/>Platform-specific app lifecycle observer.<br/>[android]<br/>actual class [AppLifecycleObserver](-app-lifecycle-observer/index.md)(onForeground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html), onBackground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html))<br/>Android implementation using ActivityLifecycleCallbacks.<br/>[ios]<br/>actual class [AppLifecycleObserver](-app-lifecycle-observer/index.md)(onForeground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html), onBackground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html))<br/>iOS implementation using NotificationCenter observers. |
| [ArrayInterop](-array-interop/index.md) | [ios]<br/>object [ArrayInterop](-array-interop/index.md)<br/>High-performance array interop between Kotlin and Swift. |
| [Logger](-logger/index.md) | [common]<br/>object [Logger](-logger/index.md)<br/>Centralized logging for VoxaTrace SDK. |
| [PlatformContext](-platform-context/index.md) | [common]<br/>object [PlatformContext](-platform-context/index.md)<br/>Stores platform context for use by common utilities. |

## Properties

| Name | Summary |
|---|---|
| [platformFileSystem](platform-file-system.md) | [common]<br/>expect val [platformFileSystem](platform-file-system.md): FileSystem<br/>Platform-specific FileSystem access.<br/>[android, ios]<br/>[android, ios]<br/>actual val [platformFileSystem](platform-file-system.md): FileSystem |

## Functions

| Name | Summary |
|---|---|
| [currentTimeMillis](current-time-millis.md) | [common]<br/>expect fun [currentTimeMillis](current-time-millis.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Returns current time in milliseconds since Unix epoch.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [currentTimeMillis](current-time-millis.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [generateTempPath](generate-temp-path.md) | [common]<br/>expect fun [generateTempPath](generate-temp-path.md)(filename: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Platform-specific temp file path generation. Returns a unique temp file path for the given filename.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [generateTempPath](generate-temp-path.md)(filename: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [getHardwareSampleRate](get-hardware-sample-rate.md) | [common]<br/>expect fun [getHardwareSampleRate](get-hardware-sample-rate.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>Get the hardware's preferred sample rate for audio recording.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [getHardwareSampleRate](get-hardware-sample-rate.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
