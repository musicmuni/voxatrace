//[voxatrace](../../../../index.md)/[com.musicmuni.voxatrace.calibra](../../index.md)/[CalibraEffects](../index.md)/[Companion](index.md)/[create](create.md)

# create

[common]\
fun [create](create.md)(config: [CalibraEffectsConfig](../../-calibra-effects-config/index.md) = CalibraEffectsConfig.VOCAL_CHAIN): [CalibraEffects](../index.md)

Create effects chain with configuration.

**Note:** Expects 16kHz audio input. Use SonixResampler to resample if needed.

#### Parameters

common

| | |
|---|---|
| config | Effects configuration (default: VOCAL_CHAIN) |

[common]\
fun [create](create.md)(preset: [EffectsPreset](../../../com.musicmuni.voxatrace.calibra.model/-effects-preset/index.md)): [CalibraEffects](../index.md)

Create effects chain with combo preset.

**Note:** Expects 16kHz audio input. Use SonixResampler to resample if needed.

#### Parameters

common

| | |
|---|---|
| preset | Combo preset (default: VOCAL_CHAIN) |
