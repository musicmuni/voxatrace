---
sidebar_label: "BreathMetrics"
---


# BreathMetrics

[common]\
data class [BreathMetrics](index.md)(val capacity: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val control: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), val isValid: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html))

Result of breath analysis.

Contains metrics for breath capacity and breath control, which are key indicators of vocal technique and breath management.

## Constructors

| | |
|---|---|
| [BreathMetrics](-breath-metrics.md) | [common]<br/>constructor(capacity: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), control: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html), isValid: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [capacity](capacity.md) | [common]<br/>val [capacity](capacity.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Breath capacity in seconds - measures longest sustained phrase |
| [control](control.md) | [common]<br/>val [control](control.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-float/index.html)<br/>Breath control score (0.0 to 1.0) - measures breathing pattern consistency |
| [isValid](is-valid.md) | [common]<br/>val [isValid](is-valid.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether the result is valid (enough data was available) |
