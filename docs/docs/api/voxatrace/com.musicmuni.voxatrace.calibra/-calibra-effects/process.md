//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraEffects](index.md)/[process](process.md)

# process

[common]\
fun [process](process.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html))

Process audio samples in-place through the effects chain. Effects are applied in order: NoiseGate → Compressor → Reverb

**Note:** Audio must be 16kHz mono. Use SonixResampler to resample if needed.

#### Parameters

common

| | |
|---|---|
| samples | Audio samples to process in-place (mono, normalized -1.0 to 1.0), 16kHz |
