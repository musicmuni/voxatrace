---
sidebar_label: "Speech"
---


# Speech

[common]\
data class [Speech](index.md)(val modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) : [VADModelProvider](../index.md)

SPEECH backend - single Silero ONNX model. State-of-the-art accuracy for speech detection.

## Constructors

| | |
|---|---|
| [Speech](-speech.md) | [common]<br/>constructor(modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [modelProvider](model-provider.md) | [common]<br/>val [modelProvider](model-provider.md): () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br/>Lazy loader for Silero VAD model bytes |
