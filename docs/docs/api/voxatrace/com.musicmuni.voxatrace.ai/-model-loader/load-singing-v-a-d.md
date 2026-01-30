//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.ai](../index.md)/[ModelLoader](index.md)/[loadSingingVAD](load-singing-v-a-d.md)

# loadSingingVAD

[common]\
expect fun [loadSingingVAD](load-singing-v-a-d.md)(): [SingingVADModels](../-singing-v-a-d-models/index.md)

Load the Singing VAD models (YAMNet + classifier). Use with VADBackend.SINGING.

Returns both required models:

- 
   yamnet: audioset-yamnet-1.onnx (~15MB)
- 
   classifier: voice_instrumental-audioset-yamnet-1.onnx (~412KB)

#### Return

SingingVADModels containing both model ByteArrays

#### Throws

| | |
|---|---|
| IllegalStateException | if models not found or configure() not called |

[android, ios]\
[android]\
actual fun [loadSingingVAD](load-singing-v-a-d.md)(): [SingingVADModels](../-singing-v-a-d-models/index.md)

[ios]\
actual fun [loadSingingVAD](load-singing-v-a-d.md)(): SingingVADModels
