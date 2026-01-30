//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../../index.md)/[VADModelProvider](../index.md)/[Companion](index.md)/[singingRealtime](singing-realtime.md)

# singingRealtime

[common]\
fun [singingRealtime](singing-realtime.md)(modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)? = null): [VADModelProvider](../index.md)

Create Singing Realtime VAD provider (uses SwiftF0 pitch model).

If [modelProvider](singing-realtime.md) is null, auto-loads from bundled SwiftF0 model via [AIModelRegistry](../../../com.musicmuni.voxatrace.ai/-a-i-model-registry/index.md).

#### Parameters

common

| | |
|---|---|
| modelProvider | Optional custom loader for SwiftF0 model bytes.     If null, uses bundled model (requires VT.initialize() on Android). |

#### Throws

| | |
|---|---|
| IllegalStateException | if no model provider and bundled model not available |
