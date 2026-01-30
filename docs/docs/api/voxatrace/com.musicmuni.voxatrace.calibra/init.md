//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.calibra](index.md)/[init](init.md)

# init

[android]\
fun [CalibraVAD.Companion](-calibra-v-a-d/-companion/index.md).[init](init.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))

Android-specific initialization for CalibraVAD.

On Android, SILERO backend requires Context for loading models from assets. Call CalibraVAD.init before creating VAD instances.

Usage:

```kotlin
// In Application.onCreate() or Activity
CalibraVAD.init(context)

// Now create VAD instances
val vad = CalibraVAD.create()
```
