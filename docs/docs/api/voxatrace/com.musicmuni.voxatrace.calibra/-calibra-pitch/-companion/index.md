---
sidebar_label: "Companion"
---


# Companion

[common]\
object [Companion](index.md)

## Functions

| Name | Summary |
|---|---|
| [createContourExtractor](create-contour-extractor.md) | [common]<br/>fun [createContourExtractor](create-contour-extractor.md)(config: [ContourExtractorConfig](../../-contour-extractor-config/index.md) = ContourExtractorConfig.DEFAULT, modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)? = null): [CalibraPitch.ContourExtractor](../-contour-extractor/index.md)<br/>Create a pitch contour extractor for batch processing. |
| [createDetector](create-detector.md) | [common]<br/>fun [createDetector](create-detector.md)(config: [PitchDetectorConfig](../../../com.musicmuni.voxatrace.calibra.model/-pitch-detector-config/index.md) = PitchDetectorConfig.BALANCED, modelProvider: () -&gt; [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)? = null): [CalibraPitch.Detector](../-detector/index.md)<br/>Create a realtime pitch detector. |
