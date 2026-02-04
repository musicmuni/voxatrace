---
sidebar_label: "VT"
---


# VT

[common]\
class [VT](index.md)

VoxaTrace SDK main entry point and initialization.

This class provides:

- 
   SDK initialization with API key (direct) or proxy endpoint (recommended)
- 
   Logging initialization
- 
   Usage tracking infrastructure

## Initialization Methods

### Recommended: Proxy-based initialization

For production apps, use proxy-based initialization to keep API keys secure on your server:

```kotlin
// Android - in Application.onCreate()
VT.initialize(
    proxyEndpoint = "https://your-server.com/voxatrace/register",
    context = this
)
```
```swift
// iOS - in AppDelegate or App init
VT.initialize(proxyEndpoint: "https://your-server.com/voxatrace/register")
```

Your proxy server should:

1. 
   Authenticate the user with your own auth system
2. 
   Forward the device_id to VoxaTrace API with your sk_live_ key
3. 
   Return the device_token to the SDK

### Alternative: App Attestation (no backend required)

For apps without a backend server, use platform attestation:

```kotlin
// Android - uses Play Integrity API
VT.initializeWithAttestation("sk_live_abc123...", this) { success, error ->
    if (!success) showError("License error: $error")
}
```
```swift
// iOS - uses App Attest (iOS 14+)
VT.initializeWithAttestation(apiKey: "sk_live_abc123...") { success, error in
    if !success { showError("License error: \(error ?? "unknown")") }
}
```

### Direct API key initialization (testing only)

For testing or simple deployments:

```kotlin
// Android - in Application.onCreate()
VT.initializeWithApiKey("sk_live_abc123...", this)
```
```swift
// iOS - in AppDelegate or App init
VT.initializeWithApiKey(apiKey: "sk_live_abc123...")
```

All factory methods in Sonix and Calibra will throw VoxaTraceNotInitializedException if called before initialization.

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
