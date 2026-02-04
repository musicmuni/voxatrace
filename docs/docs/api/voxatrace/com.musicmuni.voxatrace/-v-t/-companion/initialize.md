//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace](../../index.md)/[VT](../index.md)/[Companion](index.md)/[initialize](initialize.md)

# initialize

[common]\
fun [initialize](initialize.md)(proxyEndpoint: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)? = null, debugLogging: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false)

Initialize SDK with proxy endpoint for secure production deployment.

This is the recommended initialization method for production apps. Your API key stays secure on your server, and the SDK receives a device-specific token for telemetry.

Device token lifecycle:

- 
   Tokens expire after 30 days
- 
   SDK automatically refreshes tokens when < 7 days remaining
- 
   7-day grace period allows continued operation during refresh
- 
   Fully transparent to app developers - no action required

```kotlin
// Android
try {
    VT.initialize(
        proxyEndpoint = "https://your-server.com/voxatrace/register",
        context = this,
        debugLogging = BuildConfig.DEBUG
    )
} catch (e: VoxaTraceKilledException) {
    // License error - disable audio features
    showError("License error: ${e.message}")
}
```

Your proxy server implementation:

```kotlin
POST /voxatrace/register
Request:  { "device_id": "sha256_hash" }
Response: { "device_token": "dt_xxx", "expires_at": 1735689600 }

Proxy should:
1. Verify user is authenticated (your auth system)
2. POST to https://api.musicmuni.com/v1/voxatrace/devices/register
   with Authorization: Bearer sk_live_xxx
3. Forward response to SDK
```

#### Parameters

common

| | |
|---|---|
| proxyEndpoint | Your proxy server's device registration endpoint |
| context | Android Context (required on Android, ignored on iOS) |
| debugLogging | Enable debug logging output (default: false) |

#### Throws

| | |
|---|---|
| [VoxaTraceKilledException](../../../com.musicmuni.voxatrace.exceptions/-voxa-trace-killed-exception/index.md) | if registration fails or license is invalid |
