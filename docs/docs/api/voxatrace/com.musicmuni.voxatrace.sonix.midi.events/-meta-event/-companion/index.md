//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix.midi.events](../../index.md)/[MetaEvent](../index.md)/[Companion](index.md)

# Companion

[common]\
object [Companion](index.md)

## Properties

| Name | Summary |
|---|---|
| [COPYRIGHT_NOTICE](-c-o-p-y-r-i-g-h-t_-n-o-t-i-c-e.md) | [common]<br/>const val [COPYRIGHT_NOTICE](-c-o-p-y-r-i-g-h-t_-n-o-t-i-c-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 2 |
| [CUE_POINT](-c-u-e_-p-o-i-n-t.md) | [common]<br/>const val [CUE_POINT](-c-u-e_-p-o-i-n-t.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 7 |
| [END_OF_TRACK](-e-n-d_-o-f_-t-r-a-c-k.md) | [common]<br/>const val [END_OF_TRACK](-e-n-d_-o-f_-t-r-a-c-k.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 47 |
| [INSTRUMENT_NAME](-i-n-s-t-r-u-m-e-n-t_-n-a-m-e.md) | [common]<br/>const val [INSTRUMENT_NAME](-i-n-s-t-r-u-m-e-n-t_-n-a-m-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 4 |
| [KEY_SIGNATURE](-k-e-y_-s-i-g-n-a-t-u-r-e.md) | [common]<br/>const val [KEY_SIGNATURE](-k-e-y_-s-i-g-n-a-t-u-r-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 89 |
| [LYRICS](-l-y-r-i-c-s.md) | [common]<br/>const val [LYRICS](-l-y-r-i-c-s.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 5 |
| [MARKER](-m-a-r-k-e-r.md) | [common]<br/>const val [MARKER](-m-a-r-k-e-r.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 6 |
| [MIDI_CHANNEL_PREFIX](-m-i-d-i_-c-h-a-n-n-e-l_-p-r-e-f-i-x.md) | [common]<br/>const val [MIDI_CHANNEL_PREFIX](-m-i-d-i_-c-h-a-n-n-e-l_-p-r-e-f-i-x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 32 |
| [SEQUENCE_NUMBER](-s-e-q-u-e-n-c-e_-n-u-m-b-e-r.md) | [common]<br/>const val [SEQUENCE_NUMBER](-s-e-q-u-e-n-c-e_-n-u-m-b-e-r.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0 |
| [SEQUENCER_SPECIFIC](-s-e-q-u-e-n-c-e-r_-s-p-e-c-i-f-i-c.md) | [common]<br/>const val [SEQUENCER_SPECIFIC](-s-e-q-u-e-n-c-e-r_-s-p-e-c-i-f-i-c.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 127 |
| [SMPTE_OFFSET](-s-m-p-t-e_-o-f-f-s-e-t.md) | [common]<br/>const val [SMPTE_OFFSET](-s-m-p-t-e_-o-f-f-s-e-t.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 84 |
| [TEMPO](-t-e-m-p-o.md) | [common]<br/>const val [TEMPO](-t-e-m-p-o.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 81 |
| [TEXT_EVENT](-t-e-x-t_-e-v-e-n-t.md) | [common]<br/>const val [TEXT_EVENT](-t-e-x-t_-e-v-e-n-t.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 1 |
| [TIME_SIGNATURE](-t-i-m-e_-s-i-g-n-a-t-u-r-e.md) | [common]<br/>const val [TIME_SIGNATURE](-t-i-m-e_-s-i-g-n-a-t-u-r-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 88 |
| [TRACK_NAME](-t-r-a-c-k_-n-a-m-e.md) | [common]<br/>const val [TRACK_NAME](-t-r-a-c-k_-n-a-m-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 3 |

## Functions

| Name | Summary |
|---|---|
| [parseMetaEvent](parse-meta-event.md) | [common]<br/>fun [parseMetaEvent](parse-meta-event.md)(tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), source: BufferedSource): [MetaEvent](../index.md) |
