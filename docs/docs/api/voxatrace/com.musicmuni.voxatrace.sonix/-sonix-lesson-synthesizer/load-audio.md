//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[SonixLessonSynthesizer](index.md)/[loadAudio](load-audio.md)

# loadAudio

[common]\
suspend fun [loadAudio](load-audio.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Load audio files for all svaras.

Must be called before [synthesize](synthesize.md). Can be called again to reload.

#### Return

true if all audio files were loaded successfully
