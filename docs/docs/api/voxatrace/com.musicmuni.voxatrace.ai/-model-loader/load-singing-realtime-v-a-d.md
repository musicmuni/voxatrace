//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.ai](../index.md)/[ModelLoader](index.md)/[loadSingingRealtimeVAD](load-singing-realtime-v-a-d.md)

# loadSingingRealtimeVAD

[common]\
expect fun [loadSingingRealtimeVAD](load-singing-realtime-v-a-d.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)

Load the Singing Realtime VAD model (SwiftF0). Use with VADBackend.SINGING_REALTIME.

Note: This loads the same model as loadSwiftF0() - both are provided for API clarity (VAD use case vs pitch detection use case).

#### Return

Model data as ByteArray

#### Throws

| | |
|---|---|
| IllegalStateException | if model not found or configure() not called |

[android, ios]\
[android, ios]\
actual fun [loadSingingRealtimeVAD](load-singing-realtime-v-a-d.md)(): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)
