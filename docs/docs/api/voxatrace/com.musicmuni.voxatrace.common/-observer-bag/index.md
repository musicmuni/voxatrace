//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.common](../index.md)/[ObserverBag](index.md)

# ObserverBag

[android]\
class [ObserverBag](index.md)

A utility for managing multiple observer Jobs with unified cleanup.

ObserverBag simplifies the common pattern of tracking multiple coroutine Jobs for cleanup. Instead of declaring separate variables for each job, use a single ObserverBag.

Before:

```kotlin
private var recordingJob: Job? = null
private var durationJob: Job? = null
private var levelJob: Job? = null

override fun onCleared() {
    recordingJob?.cancel()
    durationJob?.cancel()
    levelJob?.cancel()
}
```

After:

```kotlin
private val observers = ObserverBag()

fun setupObservers() {
    observers.add(scope.launch { recorder.isRecording.collect { ... } })
    observers.add(scope.launch { recorder.level.collect { ... } })
}

override fun onCleared() {
    observers.cancelAll()
}
```

## Constructors

| | |
|---|---|
| [ObserverBag](-observer-bag.md) | [android]<br/>constructor() |

## Properties

| Name | Summary |
|---|---|
| [count](count.md) | [android]<br/>val [count](count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br/>The number of tracked jobs. |
| [isEmpty](is-empty.md) | [android]<br/>val [isEmpty](is-empty.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br/>Whether the bag has any tracked jobs. |

## Functions

| Name | Summary |
|---|---|
| [add](add.md) | [android]<br/>fun [add](add.md)(job: Job?)<br/>Adds a job to the bag for management. |
| [cancelAll](cancel-all.md) | [android]<br/>fun [cancelAll](cancel-all.md)()<br/>Cancels all tracked jobs and clears the bag. |
