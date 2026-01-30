//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[VocalRangeSession](../index.md)/[Companion](index.md)/[create](create.md)

# create

[common]\
fun [create](create.md)(config: [VocalRangeSessionConfig](../../-vocal-range-session-config/index.md) = VocalRangeSessionConfig(), detector: [CalibraPitch.Detector](../../-calibra-pitch/-detector/index.md)): [VocalRangeSession](../index.md)

Create a session with configuration and detector.

Session takes ownership of the detector and will release it when the session is released.

#### Return

Configured VocalRangeSession instance

#### Parameters

common

| | |
|---|---|
| config | Session configuration (default: auto-flow enabled) |
| detector | Pitch detector (required). Session takes ownership. |
