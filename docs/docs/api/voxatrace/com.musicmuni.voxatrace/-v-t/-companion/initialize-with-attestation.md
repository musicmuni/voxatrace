//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace](../../index.md)/[VT](../index.md)/[Companion](index.md)/[initializeWithAttestation](initialize-with-attestation.md)

# initializeWithAttestation

[common]\
fun [initializeWithAttestation](initialize-with-attestation.md)(apiKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)? = null, debugLogging: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, callback: [VT.Companion.InitCallback](-init-callback/index.md))

Initialize SDK using platform attestation for apps without a backend server.

Uses Play Integrity (Android) or App Attest (iOS) to verify the app is running on a genuine device, then registers with VoxaTrace to get a device token.

**Note:** This requires the API key to be embedded in the app, which is less secure than proxy-based initialization. Use only when a backend proxy is not available.

```kotlin
// Android
VT.initializeWithAttestation("sk_live_abc123...", this) { success, error ->
    if (success) {
        // SDK ready to use
    } else {
        showError("License error: $error")
    }
}
```

#### Parameters

common

| | |
|---|---|
| apiKey | Your VoxaTrace API key |
| context | Android Context (required on Android, ignored on iOS) |
| debugLogging | Enable debug logging |
| callback | Called when initialization completes (on main thread) |
