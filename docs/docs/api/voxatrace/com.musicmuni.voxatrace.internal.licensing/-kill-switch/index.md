---
sidebar_label: "KillSwitch"
---


# KillSwitch

[common]\
expect object [KillSwitch](index.md)

Kill switch for license enforcement.

When activated by the server, all SDK factory methods throw VoxaTraceKilledException. State is persisted locally using secure storage (EncryptedSharedPreferences on Android, Keychain on iOS) to survive app restarts and resist tampering on rooted/jailbroken devices.

Cache expiration: Valid license state expires after 30 days, requiring re-validation. This prevents indefinite offline usage without periodic license checks.

States:

- 
   Never validated: hasValidatedBefore=false, isActive=false
- 
   Valid license: hasValidatedBefore=true, isActive=false
- 
   Invalid/revoked license: hasValidatedBefore=true, isActive=true
- 
   Expired cache: hasValidatedBefore=false (reset), forces re-validation

[android, ios]\
actual object [KillSwitch](index.md)

## Functions

| Name | Summary |
|---|---|
| [activate](activate.md) | [common, android, ios]<br/>[common]<br/>expect fun [activate](activate.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?)<br/>[android, ios]<br/>actual fun [activate](activate.md)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?) |
| [deactivate](deactivate.md) | [common, android, ios]<br/>[common]<br/>expect fun [deactivate](deactivate.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?)<br/>[android, ios]<br/>actual fun [deactivate](deactivate.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?) |
| [getMessage](get-message.md) | [common]<br/>expect fun [getMessage](get-message.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?<br/>Returns the cached error message if kill switch is active<br/>[android, ios]<br/>[android, ios]<br/>actual fun [getMessage](get-message.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? |
| [hasValidatedBefore](has-validated-before.md) | [common]<br/>expect fun [hasValidatedBefore](has-validated-before.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Returns true if we've successfully validated at least once before<br/>[android, ios]<br/>[android, ios]<br/>actual fun [hasValidatedBefore](has-validated-before.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [isActive](is-active.md) | [common]<br/>expect fun [isActive](is-active.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Returns true if the kill switch is active (license invalid/revoked)<br/>[android, ios]<br/>[android, ios]<br/>actual fun [isActive](is-active.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [load](load.md) | [common, android, ios]<br/>[common]<br/>expect fun [load](load.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?)<br/>[android, ios]<br/>actual fun [load](load.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?) |
