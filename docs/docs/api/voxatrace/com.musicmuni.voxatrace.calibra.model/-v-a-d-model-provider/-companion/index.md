---
sidebar_label: "Companion"
---


# Companion

[common]\
object [Companion](index.md)

## Properties

| Name | Summary |
|---|---|
| [general](general.md) | [common]<br/>val [general](general.md): [VADModelProvider](../index.md)<br/>GENERAL backend - no model required |

## Functions

| Name | Summary |
|---|---|
| [singing](singing.md) | [common]<br/>fun [singing](singing.md)(modelProvider: () -&gt; [SingingVADModels](../../../com.musicmuni.voxatrace.ai/-singing-v-a-d-models/index.md)? = null): [VADModelProvider](../index.md)<br/>Create Singing VAD provider (uses YAMNet + classifier). |
| [singingRealtime](singing-realtime.md) | [common]<br/>fun [singingRealtime](singing-realtime.md)(modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)? = null): [VADModelProvider](../index.md)<br/>Create Singing Realtime VAD provider (uses SwiftF0 pitch model). |
| [speech](speech.md) | [common]<br/>fun [speech](speech.md)(modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)? = null): [VADModelProvider](../index.md)<br/>Create Speech VAD provider. |
