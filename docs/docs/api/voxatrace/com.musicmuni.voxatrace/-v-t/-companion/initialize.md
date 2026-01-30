//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace](../../index.md)/[VT](../index.md)/[Companion](index.md)/[initialize](initialize.md)

# initialize

[common]\
fun [initialize](initialize.md)(apiKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)? = null, debugLogging: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false)

Initialize SDK with API key for licensed usage.

Validates the API key synchronously with the server (10 second timeout). This ensures invalid or revoked keys are rejected at initialization, not during normal operation.

```kotlin
// Android - wrap in try-catch to handle license errors
try {
    VT.initialize("sk_live_abc123...", this, debugLogging = true)
} catch (e: VoxaTraceKilledException) {
    // Show error to user, disable audio features
    showError("License error: ${e.message}")
}
```

#### Parameters

common

| | |
|---|---|
| apiKey | Your VoxaTrace API key (starts with &quot;sk_live_&quot; or &quot;sk_test_&quot;) |
| context | Android Context (required on Android, ignored on iOS) |
| debugLogging | Enable debug logging output (default: false for production safety) |

#### Throws

| | |
|---|---|
| [VoxaTraceKilledException](../../../com.musicmuni.voxatrace.exceptions/-voxa-trace-killed-exception/index.md) | if API key is invalid or revoked (401/403) |
