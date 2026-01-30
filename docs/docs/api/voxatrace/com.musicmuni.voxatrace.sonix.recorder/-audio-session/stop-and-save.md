//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AudioSession](index.md)/[stopAndSave](stop-and-save.md)

# stopAndSave

[common]\
suspend fun [stopAndSave](stop-and-save.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?

Stop recording and save to file. Only works if enableEncoding was set to true.

#### Return

Path to saved file if successful, null otherwise

#### Parameters

common

| | |
|---|---|
| outputPath | Absolute path for the output file (e.g., &quot;/path/to/recording.m4a&quot;) |
