//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraVocalRange](index.md)/[getStableNote](get-stable-note.md)

# getStableNote

[common]\
fun [getStableNote](get-stable-note.md)(): [DetectedNote](../-detected-note/index.md)?

Get the stable note detected so far.

For single-note detection phases (e.g., detecting low note, then high note). Returns the note that has been held stable for at least the minimum duration.

#### Return

DetectedNote if a stable note was found, null otherwise
