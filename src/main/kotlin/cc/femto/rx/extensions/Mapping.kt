package cc.femto.rx.extensions

import com.gojuno.koptional.Optional
import com.gojuno.koptional.rxjava2.filterSome
import com.gojuno.koptional.toOptional
import io.reactivex.Observable

/**
 * Map the stream to the value returned from [mapper] and apply [Observable.distinctUntilChanged]
 * to ensure the stream won't emit if the value in [mapper] has not changed
 *
 * Usage:
 * <code>
 *   stream.mapDistinct { foo }
 *     .subscribe { /* access distinct value */ }
 * </code>
 */
inline fun <T, R : Any> Observable<T>.mapDistinct(crossinline mapper: T.() -> R): Observable<R> =
    this.map { mapper(it) }.distinctUntilChanged()

/**
 * Map the stream to the value returned from [mapper] and then
 * apply [Observable.take] with value `1` to ensure the stream will
 * complete after emitting one value
 *
 * Usage:
 * <code>
 *   stream.mapOnce { foo }
 *     .subscribe { /* access at most one value */ }
 * </code>
 */
inline fun <T, R : Any> Observable<T>.mapOnce(crossinline mapper: T.() -> R): Observable<R> =
    this.map { mapper(it) }.take(1)

/**
 * Map the stream to the value returned from [mapper], which is wrapped in an [Optional]
 *
 * Usage:
 * <code>
 *   stream.mapOptional { foo }
 *     .subscribe { /* access value wrapped in Optional */ }
 * </code>
 */
inline fun <T, R : Any> Observable<T>.mapOptional(crossinline mapper: T.() -> R?): Observable<Optional<R>> =
    this.map { mapper(it).toOptional() }

/**
 * Applies [mapOptional] and then [Observable.distinctUntilChanged] to ensure
 * the stream won't emit if the value in [mapper] has not changed
 *
 * Usage:
 * <code>
 *   stream.mapOptionalDistinct { foo }
 *     .subscribe { /* access distinct optional value if present */ }
 * </code>
 *
 * @see mapOptional
 */
inline fun <T, R : Any> Observable<T>.mapOptionalDistinct(crossinline mapper: T.() -> R?): Observable<Optional<R>> =
        this.mapOptional(mapper).distinctUntilChanged()

/**
 * Applies [mapOptional] and then [filterSome] to the stream
 *
 * Usage:
 * <code>
 *   stream.mapSome { foo }
 *     .subscribe { /* access non-optional value if present */ }
 * </code>
 *
 * NB: The resulting stream won't emit any value if [mapper] returns null
 *
 * @see mapOptional
 */
inline fun <T, R : Any> Observable<T>.mapSome(crossinline mapper: T.() -> R?): Observable<R> =
    this.mapOptional(mapper).filterSome()

/**
 * Applies [mapSome] and then [Observable.distinctUntilChanged] to ensure
 * the stream won't emit if the value in [mapper] has not changed
 *
 * Usage:
 * <code>
 *   stream.mapSomeDistinct { foo }
 *     .subscribe { /* access distinct non-optional value if present */ }
 * </code>
 *
 * @see mapSome
 */
inline fun <T, R : Any> Observable<T>.mapSomeDistinct(crossinline mapper: T.() -> R?): Observable<R> =
    this.mapSome(mapper).distinctUntilChanged()

/**
 * Applies [mapSome] and then [Observable.take] with value `1` to ensure
 * the stream will complete after emitting the value in [mapper] once
 *
 * Usage:
 * <code>
 *   stream.mapSomeOnce { foo }
 *     .subscribe { /* access first non-optional value if present */ }
 * </code>
 *
 * @see mapSome
 */
inline fun <T, R : Any> Observable<T>.mapSomeOnce(crossinline mapper: T.() -> R?): Observable<R> =
    this.mapSome(mapper).take(1)
