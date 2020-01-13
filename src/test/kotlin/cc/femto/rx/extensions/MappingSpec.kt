package cc.femto.rx.extensions

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.Some
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class MappingSpec : Spek({

    data class State(
        val foo: String = "foo",
        val bar: String? = null
    )

    lateinit var state: Observable<State>

    describe("mapDistinct") {
        lateinit var observer: TestObserver<String>

        beforeEachTest {
            state = Observable.just(
                State(),
                State(bar = "initialized"),
                State(foo = "changed")
            )
            observer = state.mapDistinct { foo }.test()
        }

        it("maps to property and makes emissions distinct") {
            observer.assertValues(
                "foo",
                "changed"
            )
        }
    }

    describe("mapOnce") {
        lateinit var observer: TestObserver<String>

        beforeEachTest {
            state = Observable.just(
                State(),
                State(bar = "initialized"),
                State(foo = "changed")
            )
            observer = state.mapOnce { foo }.test()
        }

        it("maps to property and completes after one emission") {
            observer.assertValues(
                "foo"
            )
        }
    }

    describe("mapOptional") {
        lateinit var observer: TestObserver<Optional<String>>

        beforeEachTest {
            state = Observable.just(
                State(),
                State(bar = "initialized"),
                State(foo = "changed")
            )
            observer = state.mapOptional { bar }.test()
        }

        it("maps to nullable property and wraps in `Optional`") {
            observer.assertValues(
                None,
                Some("initialized"),
                None
            )
        }
    }

    describe("mapOptionalOnce") {
        lateinit var observer: TestObserver<Optional<String>>

        beforeEachTest {
            state = Observable.just(
                State(bar = "initialized"),
                State(bar = null),
                State(bar = "changed")
            )
            observer = state.mapOptionalOnce { bar }.test()
        }

        it("maps to nullable property, wraps in `Optional`, and completes after one emission") {
            observer.assertValues(
                Some("initialized")
            )
        }
    }

    describe("mapOptionalDistinct") {
        lateinit var observer: TestObserver<Optional<String>>

        beforeEachTest {
            state = Observable.just(
                State(),
                State(bar = "initialized"),
                State(foo = "changed", bar = "initialized")
            )
            observer = state.mapOptionalDistinct { bar }.test()
        }

        it("maps to nullable property and wraps in `Optional` and filters for distinct values") {
            observer.assertValues(
                None,
                Some("initialized")
            )
        }
    }

    describe("mapSome") {
        lateinit var observer: TestObserver<String>

        beforeEachTest {
            state = Observable.just(
                State(),
                State(bar = "initialized"),
                State(foo = "changed", bar = "initialized")
            )
            observer = state.mapSome { bar }.test()
        }

        it("maps to nullable property, wraps in `Optional` and filters for `Some`") {
            observer.assertValues(
                "initialized",
                "initialized"
            )
        }
    }

    describe("mapSomeDistinct") {
        lateinit var observer: TestObserver<String>

        beforeEachTest {
            state = Observable.just(
                State(),
                State(bar = "initialized"),
                State(foo = "changed", bar = "initialized")
            )
            observer = state.mapSomeDistinct { bar }.test()
        }

        it("maps to nullable property, wraps in `Optional` and filters for distinct `Some`") {
            observer.assertValues(
                "initialized"
            )
        }
    }

    describe("mapSomeOnce") {
        lateinit var observer: TestObserver<String>

        beforeEachTest {
            state = Observable.just(
                State(),
                State(bar = "initialized"),
                State(foo = "changed", bar = "initialized"),
                State(bar = "changed")
            )
            observer = state.mapSomeOnce { bar }.test()
        }

        it("maps to nullable property, wraps in `Optional`, filters for `Some` and completes after one emission") {
            observer.assertValues(
                "initialized"
            )
        }
    }
})
