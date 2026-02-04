---
sidebar_label: "AttestationProvider"
---


# AttestationProvider

[common]\
expect class [AttestationProvider](index.md)

Platform-specific app attestation for secure device authentication.

This is used as a fallback for customers without backend proxy infrastructure. The app uses platform attestation to prove it's running on a genuine device, allowing VoxaTrace to issue a device token without a proxy server.

Implementations:

- 
   Android: Play Integrity API
- 
   iOS: App Attest (iOS 14+)

Usage flow:

1. 
   SDK calls [isAvailable](is-available.md) to check if attestation is supported
2. 
   If available, SDK calls [generateAttestation](generate-attestation.md) with a server challenge
3. 
   SDK sends attestation to VoxaTrace /attest endpoint
4. 
   Backend verifies attestation with Google/Apple
5. 
   On success, backend returns device_token

[android]\
actual class [AttestationProvider](index.md)

Android implementation using Google Play Integrity API.

Requirements:

- 
   App must be distributed via Google Play Store (or internal test track)
- 
   Device must have Google Play Services
- 
   Dependency: com.google.android.play:integrity:1.3.0

The Play Integrity API provides:

- 
   Device integrity: Is this a genuine Android device?
- 
   App integrity: Is this a genuine, unmodified app from Play Store?
- 
   Account details: Is the user's Play account in good standing?

[ios]\
actual class [AttestationProvider](index.md)

iOS implementation using App Attest (DCAppAttestService).

Requirements:

- 
   iOS 14.0 or later
- 
   Physical device (not simulator)
- 
   App Attest entitlement enabled

App Attest provides:

- 
   Hardware-backed attestation that the app is running on a genuine Apple device
- 
   Cryptographic proof that the app hasn't been modified
- 
   Protection against app cloning and tampering

## Constructors

| | |
|---|---|
| [AttestationProvider](-attestation-provider.md) | [android, ios]<br/>constructor()<br/>[common]<br/>expect constructor() |

## Functions

| Name | Summary |
|---|---|
| [generateAttestation](generate-attestation.md) | [common]<br/>expect fun [generateAttestation](generate-attestation.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?, challenge: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), callback: [AttestationCallback](../-attestation-callback/index.md))<br/>Generate platform attestation.<br/>[android]<br/>actual fun [generateAttestation](generate-attestation.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?, challenge: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), callback: [AttestationCallback](../-attestation-callback/index.md)) |
| [getPlatform](get-platform.md) | [common]<br/>expect fun [getPlatform](get-platform.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Get the platform identifier for this attestation provider.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [getPlatform](get-platform.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [isAvailable](is-available.md) | [common]<br/>expect fun [isAvailable](is-available.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if platform attestation is available on this device.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [isAvailable](is-available.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
