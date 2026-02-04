---
sidebar_label: "Singing"
---


# Singing

[common]\
data class [Singing](index.md)(val modelProvider: () -&gt; [SingingVADModels](../../../com.musicmuni.voxatrace.ai/-singing-v-a-d-models/index.md)) : [VADModelProvider](../index.md)

SINGING backend - YAMNet + classifier models. High-accuracy singing/speech vs instrumental classification.

## Constructors

| | |
|---|---|
| [Singing](-singing.md) | [common]<br/>constructor(modelProvider: () -&gt; [SingingVADModels](../../../com.musicmuni.voxatrace.ai/-singing-v-a-d-models/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [modelProvider](model-provider.md) | [common]<br/>val [modelProvider](model-provider.md): () -&gt; [SingingVADModels](../../../com.musicmuni.voxatrace.ai/-singing-v-a-d-models/index.md)<br/>Lazy loader for both models (YAMNet + classifier) |
