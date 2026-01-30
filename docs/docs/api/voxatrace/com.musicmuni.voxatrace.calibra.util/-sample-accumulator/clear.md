//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.util](../index.md)/[SampleAccumulator](index.md)/[clear](clear.md)

# clear

[common]\
fun [clear](clear.md)()

Clear the accumulator buffer.

Call when starting a new audio session to discard any pending samples. Incomplete frames are discarded (no zero-padding artifacts).
