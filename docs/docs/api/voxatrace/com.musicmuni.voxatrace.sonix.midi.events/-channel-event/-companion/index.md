//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix.midi.events](../../index.md)/[ChannelEvent](../index.md)/[Companion](index.md)

# Companion

[common]\
object [Companion](index.md)

## Properties

| Name | Summary |
|---|---|
| [CHANNEL_AFTERTOUCH](-c-h-a-n-n-e-l_-a-f-t-e-r-t-o-u-c-h.md) | [common]<br/>const val [CHANNEL_AFTERTOUCH](-c-h-a-n-n-e-l_-a-f-t-e-r-t-o-u-c-h.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 13 |
| [CONTROLLER](-c-o-n-t-r-o-l-l-e-r.md) | [common]<br/>const val [CONTROLLER](-c-o-n-t-r-o-l-l-e-r.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 11 |
| [NOTE_AFTERTOUCH](-n-o-t-e_-a-f-t-e-r-t-o-u-c-h.md) | [common]<br/>const val [NOTE_AFTERTOUCH](-n-o-t-e_-a-f-t-e-r-t-o-u-c-h.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 10 |
| [NOTE_OFF](-n-o-t-e_-o-f-f.md) | [common]<br/>const val [NOTE_OFF](-n-o-t-e_-o-f-f.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 8 |
| [NOTE_ON](-n-o-t-e_-o-n.md) | [common]<br/>const val [NOTE_ON](-n-o-t-e_-o-n.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 9 |
| [PITCH_BEND](-p-i-t-c-h_-b-e-n-d.md) | [common]<br/>const val [PITCH_BEND](-p-i-t-c-h_-b-e-n-d.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 14 |
| [PROGRAM_CHANGE](-p-r-o-g-r-a-m_-c-h-a-n-g-e.md) | [common]<br/>const val [PROGRAM_CHANGE](-p-r-o-g-r-a-m_-c-h-a-n-g-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 12 |

## Functions

| Name | Summary |
|---|---|
| [parseChannelEvent](parse-channel-event.md) | [common]<br/>fun [parseChannelEvent](parse-channel-event.md)(tick: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), delta: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), type: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), channel: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), source: BufferedSource): [ChannelEvent](../index.md) |
