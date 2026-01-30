//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.sonix](../index.md)/[AudioFocusListener](index.md)

# AudioFocusListener

[common]\
interface [AudioFocusListener](index.md)

Listener for audio focus changes.

## Functions

| Name | Summary |
|---|---|
| [onDuck](on-duck.md) | [common]<br/>abstract fun [onDuck](on-duck.md)()<br/>Called when should duck (lower volume temporarily) |
| [onFocusGained](on-focus-gained.md) | [common]<br/>abstract fun [onFocusGained](on-focus-gained.md)()<br/>Called when audio focus is gained |
| [onFocusLost](on-focus-lost.md) | [common]<br/>abstract fun [onFocusLost](on-focus-lost.md)(transient: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html))<br/>Called when audio focus is lost (transient = can resume, permanent = should stop) |
