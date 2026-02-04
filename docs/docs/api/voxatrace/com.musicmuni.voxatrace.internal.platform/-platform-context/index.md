---
sidebar_label: "PlatformContext"
---


# PlatformContext

[common]\
object [PlatformContext](index.md)

Stores platform context for use by common utilities.

On Android, this holds the Application context for accessing system services. On iOS, this is typically null (not needed).

Set via VT.initialize in the voxatrace module.

## Functions

| Name | Summary |
|---|---|
| [get](get.md) | [common]<br/>fun [get](get.md)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?<br/>Get the platform context. |
| [set](set.md) | [common]<br/>fun [set](set.md)(context: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?)<br/>Set the platform context. Called internally by VT.initialize(). |
