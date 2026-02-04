//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.ai](../index.md)/[ModelDownloader](index.md)/[awaitModel](await-model.md)

# awaitModel

[common]\
expect suspend fun [awaitModel](await-model.md)(filename: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)

Await until a specific model is available (suspends if downloading).

#### Return

Model bytes

#### Parameters

common

| | |
|---|---|
| filename | Model filename from BundleConfig.ModelFiles |

#### Throws

| | |
|---|---|
| IllegalStateException | if download fails or model not available |

[android, ios]\
[android, ios]\
actual suspend fun [awaitModel](await-model.md)(filename: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)
