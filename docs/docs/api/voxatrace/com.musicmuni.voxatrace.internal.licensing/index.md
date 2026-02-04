---
sidebar_label: "licensing"
---


# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [DeviceToken](-device-token/index.md) | [common]<br/>expect object [DeviceToken](-device-token/index.md)<br/>Secure storage for device authentication tokens.<br/>[android, ios]<br/>[android, ios]<br/>actual object [DeviceToken](-device-token/index.md) |
| [KillSwitch](-kill-switch/index.md) | [common]<br/>expect object [KillSwitch](-kill-switch/index.md)<br/>Kill switch for license enforcement.<br/>[android, ios]<br/>[android, ios]<br/>actual object [KillSwitch](-kill-switch/index.md) |

## Functions

| Name | Summary |
|---|---|
| [generateDeviceId](generate-device-id.md) | [common]<br/>expect fun [generateDeviceId](generate-device-id.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Platform-specific device ID generation. Returns a hashed identifier for the device (SHA-256).<br/>[android, ios]<br/>[android, ios]<br/>actual fun [generateDeviceId](generate-device-id.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
