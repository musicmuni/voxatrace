---
sidebar_label: "DeviceToken"
---


# DeviceToken

[common]\
expect object [DeviceToken](index.md)

Secure storage for device authentication tokens.

Device tokens are obtained from VoxaTrace backend after device registration (via customer's proxy server) and used for telemetry authentication.

Storage implementations:

- 
   Android: EncryptedSharedPreferences (AES-256)
- 
   iOS: Keychain with kSecAttrAccessibleAfterFirstUnlock

Token lifecycle:

- 
   Tokens expire after 30 days
- 
   Grace period: 7 days after expiry (backend still accepts token)
- 
   Proactive refresh: SDK refreshes when < 7 days remaining
- 
   On hard expiry: SDK queues events locally, re-registers on next init

[android, ios]\
actual object [DeviceToken](index.md)

## Functions

| Name | Summary |
|---|---|
| [clear](clear.md) | [common]<br/>expect fun [clear](clear.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?)<br/>Clear stored device token. Called when token is revoked or device is re-registered.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [clear](clear.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?) |
| [getExpiresAt](get-expires-at.md) | [common]<br/>expect fun [getExpiresAt](get-expires-at.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html)<br/>Get the token expiration timestamp (Unix seconds), or 0 if no token.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [getExpiresAt](get-expires-at.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [getToken](get-token.md) | [common]<br/>expect fun [getToken](get-token.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?<br/>Get the cached device token, or null if not registered.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [getToken](get-token.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? |
| [hasValidToken](has-valid-token.md) | [common]<br/>expect fun [hasValidToken](has-valid-token.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if a valid token exists (not null and not expired).<br/>[android, ios]<br/>[android, ios]<br/>actual fun [hasValidToken](has-valid-token.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [isExpired](is-expired.md) | [common]<br/>expect fun [isExpired](is-expired.md)(gracePeriodDays: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 7): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if token is completely expired (past grace period).<br/>[android, ios]<br/>[android, ios]<br/>actual fun [isExpired](is-expired.md)(gracePeriodDays: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [isExpiringSoon](is-expiring-soon.md) | [common]<br/>expect fun [isExpiringSoon](is-expiring-soon.md)(thresholdDays: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) = 7): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Check if token is expiring soon (within refresh threshold). SDK should proactively refresh when this returns true.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [isExpiringSoon](is-expiring-soon.md)(thresholdDays: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [load](load.md) | [common]<br/>expect fun [load](load.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?)<br/>Load cached device token from secure storage. Should be called during SDK initialization.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [load](load.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?) |
| [save](save.md) | [common]<br/>expect fun [save](save.md)(token: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), expiresAt: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?)<br/>Save device token to secure storage.<br/>[android, ios]<br/>[android, ios]<br/>actual fun [save](save.md)(token: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), expiresAt: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?) |
