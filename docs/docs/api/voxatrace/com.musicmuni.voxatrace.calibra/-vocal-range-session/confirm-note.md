//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[VocalRangeSession](index.md)/[confirmNote](confirm-note.md)

# confirmNote

[common]\
fun [confirmNote](confirm-note.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)

Lock the current best note and advance to next phase.

In DETECTING_LOW phase: locks bestLowNote and moves to DETECTING_HIGH In DETECTING_HIGH phase: locks bestHighNote and completes detection

#### Return

true if note was confirmed, false if no best note available
