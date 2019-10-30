# rx-mapping-extensions

[![](https://jitpack.io/v/cc.femto/rx-mapping-extensions.svg)](https://jitpack.io/#cc.femto/rx-mapping-extensions)
[![](https://jitci.com/gh/hpost/rx-mapping-extensions/svg)](https://jitci.com/gh/hpost/rx-mapping-extensions)

Extension functions that simplify common mapping operations
in [RxJava](https://github.com/ReactiveX/RxJava) streams, especially
when wrapping nullable values in [Optionals](https://github.com/gojuno/koptional).

This project is intended as a companion library to [rx-mvi](https://github.com/hpost/rx-mvi)
as it reduces some of the boilerplate when observing partial state updates.


## Examples

Consider an `Observable` stream that emits `State` objects:

```kotlin
data class State(
  val foo: String = "foo",
  val bar: String? = null
)

fun state(): Observable<State> {
  return Observable.just(
    State(),
    State(bar = "initialized"),
    State(foo = "changed", bar = "initialized")
  )
}

state().subscribe { println(it) }

// --> State(foo=foo, bar=null)
// --> State(foo=foo, bar=initialized)
// --> State(foo=changed, bar=null)
```

### Observable.mapDistinct
Map to one property of the state and ensure
that subsequently emitted values are distinct.

The passed lambda acts as an extension function with
the Observable's type as it's receiver and thus allows
to access properties without having to refer to the lambda parameter.

```kotlin
state().mapDistinct { foo }
  .subscribe { println(it) }

// --> foo
// --> changed // skips middle value with identical `foo`
```


### Observable.mapOptional
Map to one nullable property of the state and wrap in `Optional`

```kotlin
state().mapOptional { bar }
  .subscribe { println(it) }

// --> None
// --> Some(initialized)
// --> None
```


### Observable.mapSome
Map to one nullable property of the state and wrap in `Optional`,
then filter for `Some`

```kotlin
state().mapSome { bar }
  .subscribe { println(it) }

// --> initialized // skips `None`
// --> initialized // `foo` changed, causing another emission
```


### Observable.mapSomeDistinct
Map to one nullable property of the state and wrap in `Optional`,
then filter for `Some` and ensure that subsequently emitted values are distinct

```kotlin
state().mapSomeDistinct { bar }
  .subscribe { println(it) }

// --> initialized // skips `None` and unrelated change of `foo`
```


### Observable.mapSomeOnce
Map to one nullable property of the state and wrap in `Optional`,
then filter for `Some` and ensure that only the first value is observed

```kotlin
state().mapSomeOnce { bar }
  .subscribe { println(it) }

// --> initialized // skips `None` and unsubscribes after one emission
```


## Binaries
```gradle
dependencies {
    implementation "cc.femto:rx-mapping-extensions:0.1"
}
```

Requires the JitPack repository:
```gradle
repositories {
    maven { url "https://jitpack.io" }
}
```
