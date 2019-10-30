# rx-mapping-extensions

[![](https://jitpack.io/v/cc.femto/rx-mapping-extensions.svg)](https://jitpack.io/#cc.femto/rx-mapping-extensions)

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
    State(foo = "changed")
  )
}

state().subscribe { println(it) }

// --> State(foo=foo, bar=null)
// --> State(foo=foo, bar=initialized)
// --> State(foo=changed, bar=null)
```

### Observable.mapDistinct
Map the stream to the value returned from `mapper` and ensure
that subsequently emitted values are distinct:

```kotlin
state().mapDistinct { foo }
  .subscribe { println(it) }

// --> State(foo=foo, bar=null)
// --> State(foo=changed, bar=null) // skipped middle value with identical `foo`
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
