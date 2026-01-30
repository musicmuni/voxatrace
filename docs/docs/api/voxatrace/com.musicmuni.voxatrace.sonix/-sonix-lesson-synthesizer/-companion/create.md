//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix](../../index.md)/[SonixLessonSynthesizer](../index.md)/[Companion](index.md)/[create](create.md)

# create

[common]\
fun [create](create.md)(svaras: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[LessonSvara](../../../com.musicmuni.voxatrace.sonix.model/-lesson-svara/index.md)&gt;, beatLengthMs: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [SonixLessonSynthesizer](../index.md)

Zero-config factory - creates synthesizer with default settings.

#### Return

Ready-to-use SonixLessonSynthesizer

#### Parameters

common

| | |
|---|---|
| svaras | List of svaras to synthesize |
| beatLengthMs | Duration of each beat in milliseconds |
