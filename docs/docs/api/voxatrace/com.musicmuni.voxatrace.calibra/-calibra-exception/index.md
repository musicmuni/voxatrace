---
sidebar_label: "CalibraException"
---


# CalibraException

[common]\
class [CalibraException](index.md)(val type: [CalibraErrorType](../-calibra-error-type/index.md), val message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val nativeCode: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)? = null) : [VoxaTraceException](../../com.musicmuni.voxatrace.exceptions/-voxa-trace-exception/index.md)

Exception class for Calibra library errors.

## Constructors

| | |
|---|---|
| [CalibraException](-calibra-exception.md) | [common]<br/>constructor(type: [CalibraErrorType](../-calibra-error-type/index.md), message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), nativeCode: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [message](message.md) | [common]<br/>open val [message](message.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Human-readable error message |
| [nativeCode](native-code.md) | [common]<br/>val [nativeCode](native-code.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)? = null<br/>Optional native error code |
| [type](type.md) | [common]<br/>val [type](type.md): [CalibraErrorType](../-calibra-error-type/index.md)<br/>Error type |
