//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.ai](../index.md)/[AIModelRegistry](index.md)/[registerSingingVAD](register-singing-v-a-d.md)

# registerSingingVAD

[common]\
fun [registerSingingVAD](register-singing-v-a-d.md)(provider: () -&gt; [SingingVADModels](../-singing-v-a-d-models/index.md))

Register Singing VAD model provider for automatic loading. Provides both YAMNet and classifier models.

#### Parameters

common

| | |
|---|---|
| provider | Function that returns SingingVADModels (yamnet + classifier) |
