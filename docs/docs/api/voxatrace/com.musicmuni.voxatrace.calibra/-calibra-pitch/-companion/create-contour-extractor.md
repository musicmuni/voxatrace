//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraPitch](../index.md)/[Companion](index.md)/[createContourExtractor](create-contour-extractor.md)

# createContourExtractor

[common]\
fun [createContourExtractor](create-contour-extractor.md)(config: [ContourExtractorConfig](../../-contour-extractor-config/index.md) = ContourExtractorConfig.DEFAULT, modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)? = null): [CalibraPitch.ContourExtractor](../-contour-extractor/index.md)

Create a pitch contour extractor for batch processing.

ADR-001 compliant: Factory takes (config, dependency).

#### Parameters

common

| | |
|---|---|
| config | Contour extractor configuration |
| modelProvider | Function to load ONNX model bytes (required for SWIFT_F0).     Example: `{ ModelLoader.loadSwiftF0() }` after adding ai-models dependency. |
