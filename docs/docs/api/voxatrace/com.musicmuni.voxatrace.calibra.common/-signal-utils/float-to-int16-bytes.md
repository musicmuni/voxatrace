//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.common](../index.md)/[SignalUtils](index.md)/[floatToInt16Bytes](float-to-int16-bytes.md)

# floatToInt16Bytes

[common]\
fun [floatToInt16Bytes](float-to-int16-bytes.md)(samples: [FloatArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float-array/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)

Convert float samples to 16-bit PCM bytes (little-endian).

#### Return

Byte array (2 bytes per sample)

#### Parameters

common

| | |
|---|---|
| samples | Float samples (-1.0 to 1.0) |
