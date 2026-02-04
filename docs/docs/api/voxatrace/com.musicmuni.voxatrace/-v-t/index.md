---
sidebar_label: "VT"
---


# VT

[common]\
class [VT](index.md)

VoxaTrace SDK main entry point and initialization.

This class provides:

- 
   SDK initialization via proxy endpoint (recommended) or app attestation
- 
   Logging initialization
- 
   Usage tracking infrastructure

## Initialization Methods

### Recommended: Proxy-based initialization

For production apps with a backend, use proxy-based initialization to keep API keys secure:

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

### Server-side / Desktop / Python bindings

For non-mobile environments (server-side processing, desktop apps, Python bindings, CLI tools):

```kotlin
VT.initializeForServer("sk_live_abc123...")
```
```python
# Python bindings
vt.initialize_for_server("sk_live_abc123...")
```

All factory methods in Sonix and Calibra will throw VoxaTraceNotInitializedException if called before initialization.

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
