//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraLiveEval](index.md)/[prepareSession](prepare-session.md)

# prepareSession

[common]\
suspend fun [prepareSession](prepare-session.md)()

Prepare the session for practice.

Loads reference audio and creates the LiveEvaluator for scoring. Call this before [startPracticingSegment](start-practicing-segment.md).

This is a suspend function that runs heavy work on a background dispatcher. The UI remains responsive while preparation happens.

**Note:** Reference audio must be 16kHz. Use SonixDecoder with default settings to decode audio files (it returns 16kHz by default).
