//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraMelodyEval](index.md)/[evaluate](evaluate.md)

# evaluate

[common]\
fun [evaluate](evaluate.md)(reference: [LessonMaterial](../../com.musicmuni.voxatrace.calibra.model/-lesson-material/index.md), student: [LessonMaterial](../../com.musicmuni.voxatrace.calibra.model/-lesson-material/index.md), contourExtractor: [CalibraPitch.ContourExtractor](../-calibra-pitch/-contour-extractor/index.md)): [SingingResult](../../com.musicmuni.voxatrace.calibra.model/-singing-result/index.md)

Evaluate a melody performance against a reference.

#### Return

Complete evaluation result with per-segment scores

#### Parameters

common

| | |
|---|---|
| reference | The reference material with audio, segments, and key |
| student | The student's lesson material with audio and key.     If student.segments is empty, uses reference.segments.     If student.pitchContour is pre-computed, skips extraction. |
| contourExtractor | Pre-built contour extractor for pitch extraction.     Caller owns the extractor lifecycle. |
