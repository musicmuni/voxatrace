//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraNoteEval](index.md)/[evaluate](evaluate.md)

# evaluate

[common]\
fun [evaluate](evaluate.md)(pattern: [ExercisePattern](../-exercise-pattern/index.md), student: [PitchContour](../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md), referenceKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), studentKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, config: [NoteEvalConfig](../../com.musicmuni.voxatrace.calibra.model/-note-eval-config/index.md) = NoteEvalConfig.DEFAULT): [ExerciseResult](../-exercise-result/index.md)

Evaluate with configuration object.

#### Return

Exercise evaluation result with per-note scores

#### Parameters

common

| | |
|---|---|
| pattern | The exercise pattern with note frequencies and durations |
| student | Pitch contour from student's performance |
| referenceKeyHz | Reference key/tonic frequency in Hz |
| studentKeyHz | Student's key frequency in Hz (0 = same as reference, default 0) |
| config | Note evaluation configuration (default: NoteEvalConfig.DEFAULT) |

[common]\
fun [evaluate](evaluate.md)(pattern: [ExercisePattern](../-exercise-pattern/index.md), student: [PitchContour](../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md), referenceKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), studentKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, preset: [NoteEvalPreset](../../com.musicmuni.voxatrace.calibra.model/-note-eval-preset/index.md)): [ExerciseResult](../-exercise-result/index.md)

Evaluate with preset.

#### Return

Exercise evaluation result with per-note scores

#### Parameters

common

| | |
|---|---|
| pattern | The exercise pattern with note frequencies and durations |
| student | Pitch contour from student's performance |
| referenceKeyHz | Reference key/tonic frequency in Hz |
| studentKeyHz | Student's key frequency in Hz (0 = same as reference, default 0) |
| preset | Note evaluation preset (LENIENT, BALANCED, or STRICT) |

[common]\
fun [evaluate](evaluate.md)(pattern: [ExercisePattern](../-exercise-pattern/index.md), student: [PitchContour](../../com.musicmuni.voxatrace.calibra.model/-pitch-contour/index.md), referenceKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), studentKeyHz: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html) = 0.0f, scoreType: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0, leewaySamples: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 0): [ExerciseResult](../-exercise-result/index.md)

Evaluate a note/exercise performance.

#### Return

Exercise evaluation result with per-note scores

#### Parameters

common

| | |
|---|---|
| pattern | The exercise pattern with note frequencies and durations |
| student | Pitch contour from student's performance |
| referenceKeyHz | Reference key/tonic frequency in Hz |
| studentKeyHz | Student's key frequency in Hz (0 = same as reference, default 0) |
| scoreType | Scoring algorithm type (0 = standard, 1 = strict/weighted) |
| leewaySamples | Tolerance at note boundaries in samples (default 0) |
