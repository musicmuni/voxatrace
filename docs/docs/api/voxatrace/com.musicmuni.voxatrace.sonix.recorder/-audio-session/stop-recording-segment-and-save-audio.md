//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix.recorder](../index.md)/[AudioSession](index.md)/[stopRecordingSegmentAndSaveAudio](stop-recording-segment-and-save-audio.md)

# stopRecordingSegmentAndSaveAudio

[common]\
suspend fun [stopRecordingSegmentAndSaveAudio](stop-recording-segment-and-save-audio.md)(segmentIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html), outputDir: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?

Stop recording the current segment and save to file.

#### Return

Path to segment audio file, or null if not saved

#### Parameters

common

| | |
|---|---|
| segmentIndex | The index of the segment to stop |
| outputDir | Directory to save the segment audio file |
