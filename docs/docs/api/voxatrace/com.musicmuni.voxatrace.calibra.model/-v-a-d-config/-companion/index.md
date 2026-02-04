---
sidebar_label: "Companion"
---


# Companion

[common]\
object [Companion](index.md)

## Properties

| Name | Summary |
|---|---|
| [GENERAL](-g-e-n-e-r-a-l.md) | [common]<br/>val [GENERAL](-g-e-n-e-r-a-l.md): [VADConfig](../index.md)<br/>General-purpose config (RMS-based) |
| [SINGING](-s-i-n-g-i-n-g.md) | [common]<br/>val [SINGING](-s-i-n-g-i-n-g.md): [VADConfig](../index.md)<br/>Singing detection config (YAMNet) |
| [SINGING_REALTIME](-s-i-n-g-i-n-g_-r-e-a-l-t-i-m-e.md) | [common]<br/>val [SINGING_REALTIME](-s-i-n-g-i-n-g_-r-e-a-l-t-i-m-e.md): [VADConfig](../index.md)<br/>Real-time singing detection config (SwiftF0) |
| [SPEECH](-s-p-e-e-c-h.md) | [common]<br/>val [SPEECH](-s-p-e-e-c-h.md): [VADConfig](../index.md)<br/>Speech detection config (Silero) |

## Functions

| Name | Summary |
|---|---|
| [forBackend](for-backend.md) | [common]<br/>fun [forBackend](for-backend.md)(backend: [VADBackend](../../-v-a-d-backend/index.md)): [VADConfig](../index.md)<br/>Create config with sensible defaults for the specified backend. |
