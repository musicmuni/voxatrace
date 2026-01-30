//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.sonix.midi](../../index.md)/[MidiTrack](../index.md)/[Companion](index.md)

# Companion

[common]\
object [Companion](index.md)

## Properties

| Name | Summary |
|---|---|
| [IDENTIFIER](-i-d-e-n-t-i-f-i-e-r.md) | [common]<br/>val [IDENTIFIER](-i-d-e-n-t-i-f-i-e-r.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html) |

## Functions

| Name | Summary |
|---|---|
| [createTempoTrack](create-tempo-track.md) | [common]<br/>fun [createTempoTrack](create-tempo-track.md)(): [MidiTrack](../index.md)<br/>Create a standard tempo track with TimeSignature and Tempo events |
| [parse](parse.md) | [common]<br/>fun [parse](parse.md)(source: BufferedSource): [MidiTrack](../index.md)<br/>Parse a MidiTrack from BufferedSource |
