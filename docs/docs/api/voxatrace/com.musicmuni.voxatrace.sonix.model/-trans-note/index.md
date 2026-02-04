---
sidebar_label: "TransNote"
---


# TransNote

[common]\
@Serializable

data class [TransNote](index.md)(val tStart: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html), val tEnd: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html), val freqHz: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html), val label: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))

A single note within a transcription segment.

## Constructors

| | |
|---|---|
| [TransNote](-trans-note.md) | [common]<br/>constructor(tStart: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html), tEnd: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html), freqHz: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html), label: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [freqHz](freq-hz.md) | [common]<br/>@SerialName(value = &quot;freqHz&quot;)<br/>val [freqHz](freq-hz.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html)<br/>Frequency in Hz |
| [label](label.md) | [common]<br/>val [label](label.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br/>Svara label (e.g., &quot;S&quot;, &quot;R&quot;, &quot;Ṇ&quot;, &quot;Ṡ&quot;) |
| [tEnd](t-end.md) | [common]<br/>@SerialName(value = &quot;t_end&quot;)<br/>val [tEnd](t-end.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html)<br/>End timestamp in seconds |
| [tStart](t-start.md) | [common]<br/>@SerialName(value = &quot;t_start&quot;)<br/>val [tStart](t-start.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-double/index.html)<br/>Start timestamp in seconds |
