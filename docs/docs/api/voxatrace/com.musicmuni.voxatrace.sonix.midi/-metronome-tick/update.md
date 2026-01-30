//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.midi](../index.md)/[MetronomeTick](index.md)/[update](update.md)

# update

[common]\
fun [update](update.md)(ticksElapsed: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Update metronome state based on elapsed ticks.

#### Return

true if a new beat occurred (caller should dispatch this event)

#### Parameters

common

| | |
|---|---|
| ticksElapsed | Number of MIDI ticks that have elapsed |
