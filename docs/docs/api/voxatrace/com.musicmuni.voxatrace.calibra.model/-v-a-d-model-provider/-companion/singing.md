//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra.model](../../index.md)/[VADModelProvider](../index.md)/[Companion](index.md)/[singing](singing.md)

# singing

[common]\
fun [singing](singing.md)(modelProvider: () -&gt; [SingingVADModels](../../../com.musicmuni.voxatrace.ai/-singing-v-a-d-models/index.md)? = null): [VADModelProvider](../index.md)

Create Singing VAD provider (uses YAMNet + classifier).

If [modelProvider](singing.md) is null, auto-loads from bundled models via [AIModelRegistry](../../../com.musicmuni.voxatrace.ai/-a-i-model-registry/index.md).

#### Parameters

common

| | |
|---|---|
| modelProvider | Optional custom loader for SingingVADModels (yamnet + classifier).     If null, uses bundled models (requires VT.initialize() on Android). |

#### Throws

| | |
|---|---|
| IllegalStateException | if no model provider and bundled models not available |
