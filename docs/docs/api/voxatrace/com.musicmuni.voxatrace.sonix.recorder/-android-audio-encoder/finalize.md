//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AndroidAudioEncoder](index.md)/[finalize](finalize.md)

# finalize

[android]\
open suspend override fun [finalize](finalize.md)(outputPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Signal end of input and finalize encoding to file. This drains all queued buffers and writes the final file.

#### Return

true if encoding completed successfully

#### Parameters

android

| | |
|---|---|
| outputPath | Absolute path for output file |
