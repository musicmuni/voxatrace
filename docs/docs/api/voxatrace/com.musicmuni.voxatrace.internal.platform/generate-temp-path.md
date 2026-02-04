//[voxatrace](../../index.md)/[com.musicmuni.voxatrace.internal.platform](index.md)/[generateTempPath](generate-temp-path.md)

# generateTempPath

[common]\
expect fun [generateTempPath](generate-temp-path.md)(filename: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)

Platform-specific temp file path generation. Returns a unique temp file path for the given filename.

#### Return

Full path to a unique temp file

#### Parameters

common

| | |
|---|---|
| filename | The desired filename (e.g., &quot;audio.wav&quot;) |

[android, ios]\
[android, ios]\
actual fun [generateTempPath](generate-temp-path.md)(filename: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)
