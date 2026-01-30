//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.ai](../index.md)/[SingingVADModels](index.md)

# SingingVADModels

[common]\
data class [SingingVADModels](index.md)(val yamnet: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), val classifier: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html))

Container for Singing VAD model files.

The Singing VAD uses a two-stage pipeline:

1. 
   YAMNet extracts audio embeddings
2. 
   Classifier head predicts voice/instrumental

## Constructors

| | |
|---|---|
| [SingingVADModels](-singing-v-a-d-models.md) | [common]<br/>constructor(yamnet: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), classifier: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [classifier](classifier.md) | [common]<br/>val [classifier](classifier.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Voice/instrumental classifier (voice_instrumental-audioset-yamnet-1.onnx, ~412KB) |
| [yamnet](yamnet.md) | [common]<br/>val [yamnet](yamnet.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>YAMNet embedding model (audioset-yamnet-1.onnx, ~15MB) |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [common]<br/>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [common]<br/>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
