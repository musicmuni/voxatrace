---
sidebar_label: "AppLifecycleObserver"
---


# AppLifecycleObserver

[common]\
expect class [AppLifecycleObserver](index.md)(onForeground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html), onBackground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html))

Platform-specific app lifecycle observer.

Detects app foreground/background transitions for telemetry flushing.

[android]\
actual class [AppLifecycleObserver](index.md)(onForeground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html), onBackground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html))

Android implementation using ActivityLifecycleCallbacks.

[ios]\
actual class [AppLifecycleObserver](index.md)(onForeground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html), onBackground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html))

iOS implementation using NotificationCenter observers.

## Constructors

| | |
|---|---|
| [AppLifecycleObserver](-app-lifecycle-observer.md) | [common]<br/>expect constructor(onForeground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html), onBackground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html))<br/>[android, ios]<br/>actual constructor(onForeground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html), onBackground: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [register](register.md) | [common]<br/>expect fun [register](register.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?)<br/>Start observing lifecycle events.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [register](register.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?) |
| [unregister](unregister.md) | [common]<br/>expect fun [unregister](unregister.md)()<br/>Stop observing lifecycle events.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [unregister](unregister.md)() |
