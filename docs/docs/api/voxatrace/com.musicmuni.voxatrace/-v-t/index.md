//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace](../index.md)/[VT](index.md)

# VT

[common]\
class [VT](index.md)

VoxaTrace SDK main entry point and initialization.

This class provides:

- 
   SDK initialization with API key (required)
- 
   Logging initialization
- 
   Usage tracking infrastructure

## Initialization (Required)

You MUST call [initialize](-companion/initialize.md) before using any VoxaTrace components (Sonix, Calibra).

```kotlin
// Android - in Application.onCreate()
VT.initialize("sk_live_abc123...", this)
```
```swift
// iOS - in AppDelegate or App init
VT.initialize(apiKey: "sk_live_abc123...")
```

All factory methods in Sonix and Calibra will throw VoxaTraceNotInitializedException if called before initialization.

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br/>object [Companion](-companion/index.md) |
