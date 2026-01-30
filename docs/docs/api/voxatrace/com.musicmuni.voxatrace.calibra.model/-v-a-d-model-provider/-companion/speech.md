//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../../index.md)/[VADModelProvider](../index.md)/[Companion](index.md)/[speech](speech.md)

# speech

[common]\
fun [speech](speech.md)(modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)? = null): [VADModelProvider](../index.md)

Create Speech VAD provider.

If [modelProvider](speech.md) is null, auto-loads from bundled Silero model via [AIModelRegistry](../../../com.musicmuni.voxatrace.ai/-a-i-model-registry/index.md).

#### Parameters

common

| | |
|---|---|
| modelProvider | Optional custom loader for Silero VAD model bytes.     If null, uses bundled model (requires VT.initialize() on Android). |

#### Throws

| | |
|---|---|
| IllegalStateException | if no model provider and bundled model not available |
