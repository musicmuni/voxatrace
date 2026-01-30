//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra.evaluation.live](../index.md)/[PostSegmentAction](index.md)

# PostSegmentAction

[common]\
enum [PostSegmentAction](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PostSegmentAction](index.md)&gt; 

Actions that can be taken after a segment completes.

## Entries

| | |
|---|---|
| [RETRY_SAME_SEGMENT](-r-e-t-r-y_-s-a-m-e_-s-e-g-m-e-n-t/index.md) | [common]<br/>[RETRY_SAME_SEGMENT](-r-e-t-r-y_-s-a-m-e_-s-e-g-m-e-n-t/index.md)<br/>Score below threshold and attempts remaining - retry the same segment |
| [ADVANCE_TO_NEXT](-a-d-v-a-n-c-e_-t-o_-n-e-x-t/index.md) | [common]<br/>[ADVANCE_TO_NEXT](-a-d-v-a-n-c-e_-t-o_-n-e-x-t/index.md)<br/>Score met threshold or max attempts reached - advance to next segment |
| [WAIT_FOR_USER_INPUT](-w-a-i-t_-f-o-r_-u-s-e-r_-i-n-p-u-t/index.md) | [common]<br/>[WAIT_FOR_USER_INPUT](-w-a-i-t_-f-o-r_-u-s-e-r_-i-n-p-u-t/index.md)<br/>Auto-advance disabled - wait for user to choose next action |
| [SESSION_COMPLETE](-s-e-s-s-i-o-n_-c-o-m-p-l-e-t-e/index.md) | [common]<br/>[SESSION_COMPLETE](-s-e-s-s-i-o-n_-c-o-m-p-l-e-t-e/index.md)<br/>This was the last segment - session is complete |

## Properties

| Name | Summary |
|---|---|
| [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827) | [common]<br/>val [name](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-372974862%2FProperties%2F-204059827): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827) | [common]<br/>val [ordinal](../../com.musicmuni.voxatrace.sonix.util/-audio-utils/-fade-out-function/-l-i-n-e-a-r/index.md#-739389684%2FProperties%2F-204059827): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br/>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [PostSegmentAction](index.md)<br/>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br/>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[PostSegmentAction](index.md)&gt;<br/>Returns an array containing the constants of this enum type, in the order they're declared. |
