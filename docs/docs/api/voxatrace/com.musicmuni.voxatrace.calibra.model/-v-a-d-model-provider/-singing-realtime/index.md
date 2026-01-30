//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../../index.md)/[VADModelProvider](../index.md)/[SingingRealtime](index.md)

# SingingRealtime

[common]\
data class [SingingRealtime](index.md)(val modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) : [VADModelProvider](../index.md)

SINGING_REALTIME backend - single SwiftF0 ONNX model. Low-latency pitch-based singing detection.

## Constructors

| | |
|---|---|
| [SingingRealtime](-singing-realtime.md) | [common]<br/>constructor(modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [modelProvider](model-provider.md) | [common]<br/>val [modelProvider](model-provider.md): () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Lazy loader for SwiftF0 model bytes |
