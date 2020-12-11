package com.cyberinsane.jetflix.domain.model.ext

import com.cyberinsane.jetflix.data.model.DataError
import com.cyberinsane.jetflix.data.model.ResponseWrapper
import com.cyberinsane.jetflix.domain.model.Resource


/**
 * zip
 *
 * Zip multiple ResponseWrappers into a pair of values.
 * An error in one of the two items is propagated to consumer.
 *
 * Example:
 * ```
 * val profile = async { fetchProfileData() }
 * val recentOrders = async { fetchRecentOrders() }
 *
 * val result: ResponseWrapper<Pair<Profile, RecentOrders> =
 *   profile.await().zip(recentOrders.await())
 * ```
 */
fun <T : Any, U : Any> ResponseWrapper<T>.zip(other: ResponseWrapper<U>): ResponseWrapper<Pair<T, U>> {
    return when (this) {
        is ResponseWrapper.Success ->
            when (other) {
                is ResponseWrapper.Success -> ResponseWrapper.Success(Pair(this.data, other.data))
                is ResponseWrapper.Error<*, *> -> other
            }
        is ResponseWrapper.Error<*, *> -> this
    }
}

/**
 * map
 *
 * Transform the value contained within this response wrapper.
 * Error values are not transformed.
 *
 * Example:
 * ```
 * val address: ResponseWrapper<Address> = repository
 *   .fetchRestaurant(2440)
 *   .map { it.address }
 * ```
 */
inline fun <T : Any, U : Any> ResponseWrapper<T>.map(f: (T) -> U): ResponseWrapper<U> =
    when (this) {
        is ResponseWrapper.Success -> ResponseWrapper.Success(f(this.data))
        is ResponseWrapper.Error<*, *> -> this
    }

/**
 * flatMap
 *
 * Transforms the value contained within this response wrapper and unwraps the result.
 * Error values are not transformed.
 *
 * When multiple `flatMap`s are used in series:
 *   - computation stops on the first error.
 *   - success values are propagated.
 *
 * Example:
 * ```
 * val chain = fetchProfile()
 *   .flatMap { fetchRestaurantDetails(it.favorite) }
 *   .flatMap { fetchOrdersForRestaurant(it.restaurantId) }
 *
 * when (chain) {
 *     is Success -> { /* Handle Success */ }
 *     is Error<*,*> -> { /* Handle Error */ }
 * }
 * ```
 */
inline fun <T : Any, U : Any> ResponseWrapper<T>.flatMap(f: (T) -> ResponseWrapper<U>): ResponseWrapper<U> =
    when (val result = map { f(it) }) {
        is ResponseWrapper.Success -> result.data
        is ResponseWrapper.Error<*, *> -> result
    }

/**
 * mapNotNullOrError
 *
 * Apply a transformation function that returns a nullable value.
 *
 * If a null value is encountered, the provided error is returned.
 *
 * Example:
 * ```
 * val address: ResponseWrapper<Address> = repository
 *   .fetchRestaurant(2440)
 *   .mapNotNullOrError(DataError.UnknownError(Throwable("..."))) { it.address }
 * ```
 */
inline fun <T : Any, U : Any> ResponseWrapper<T>.mapNotNullOrError(
    error: DataError<*, *>,
    f: (T) -> U?
): ResponseWrapper<U> =
    when (this) {
        is ResponseWrapper.Success ->
            f(this.data)?.let { ResponseWrapper.Success(it) }
                ?: ResponseWrapper.Error(
                    error
                )
        is ResponseWrapper.Error<*, *> -> this
    }

/**
 * filter
 *
 * Returns a `ValidationError` error if the predicate is not successful.
 * The function has no effect on errors.
 *
 * Example:
 * ```
 * val result = fetchRecentRestaurants()
 *   .filter { it.id > 0 }
 *   .map { "Restaurant ${it.id} is valid!" } // Only maps if the predicate is successful
 * ```
 */
inline fun <T : Any> ResponseWrapper<T>.filter(predicate: (T) -> Boolean): ResponseWrapper<T> =
    when {
        this is ResponseWrapper.Success && predicate(this.data) -> this
        this is ResponseWrapper.Success -> ResponseWrapper.Error(
            DataError.ValidationError(
                "Predicate not met for filter.",
                -1
            )
        )
        else -> this
    }

/**
 * tap
 *
 * Runs a function for side-effects on a success value. Has no effect on the value.
 * The function does not run on errors.
 *
 * Example:
 * ```
 * val chain = fetchProfile()
 *   .tap { println("Pre Transform: s{it}") } // Prints the input profile
 *   .map { transform(it) }
 *   .tap { println("Post Transform: ${it}") } // Prints the transformed profile
 *
 * when (chain) {
 *     is Success -> { /* Handle Success */ }
 *     is Error<*,*> -> { /* Handle Error */ }
 * }
 * ```
 */
inline fun <T : Any> ResponseWrapper<T>.tapSuccess(f: (T) -> Unit): ResponseWrapper<T> =
    when (this) {
        is ResponseWrapper.Success -> {
            f(this.data)
            this
        }
        is ResponseWrapper.Error<*, *> -> {
            this
        }
    }

/**
 * catch
 *
 * Executes another action if the current ResponseWrapper is an error. This works similar to `flatMap`, but
 * only runs on Errors.
 *
 * Example:
 * ```
 * val profile = fetchProfile()
 *   .catch { fetchProfileFromDB() } // Run a fallback action on error.
 *
 * when (profile) {
 *     is Success -> { /* Handle Success */ }
 *     is Error<*,*> -> { /* Handle Error */ }
 * }
 * ```
 */
inline fun <T : Any> ResponseWrapper<T>.catch(f: (DataError<*, *>) -> ResponseWrapper<T>) =
    when (this) {
        is ResponseWrapper.Success -> {
            this
        }
        is ResponseWrapper.Error<*, *> -> {
            f(this.error)
        }
    }

/**
 * sequence
 *
 * Transforms a list of `ResponseWrapper`s to a list wrapped in a `ResponseWrapper`.
 * If any of the items are an `Error` the error is propagated as the result.
 * If all items are success, then the result is a list of the success values.
 *
 * Example:
 * ```
 * val ids = listOf(1,2,3,4,5)
 * val apiCalls: List<ResponseWrapper<Restaurant>> = ids.map { requestRestaurantId(it) }
 * val result = ResponseWrapper<List<Restaurant>> = apiCalls.sequence()
 * when (result) {
 *     is Success ->  { /* Handle Success */ }
 *     is Error<*, *> -> { /* Handle Error */}
 * }
 * ```
 */
fun <T : Any> List<ResponseWrapper<T>>.sequence(): ResponseWrapper<List<T>> {
    if (this.isEmpty()) {
        return ResponseWrapper.Success(emptyList())
    }

    val accumulator = mutableListOf<T>()
    for (item in this) {
        when (item) {
            is ResponseWrapper.Success -> accumulator.add(item.data)
            is ResponseWrapper.Error<*, *> -> {
                return item
            }
        }
    }
    return ResponseWrapper.Success(accumulator)
}

/**
 * tapError
 *
 * Runs a function for side-effects on a error value. Has no effect on the value.This works similar to tap, but only runs on Errors.
 *
 * Example:
 * ```
 * val profile = fetchProfile()
 *   .tapError { fetchProfileFromDB() } // Run a fallback action on error.
 *
 * when (profile) {
 *     is Success -> { /* Handle Success */ }
 *     is Error<*,*> -> { /* Handle Error */ }
 * }
 * ```
 */
inline fun <T : Any> ResponseWrapper<T>.tapError(f: (DataError<Any, Any>) -> Unit): ResponseWrapper<T> =
    when (this) {
        is ResponseWrapper.Success -> {
            this
        }
        is ResponseWrapper.Error<*, *> -> {
            f(this.error)
            this
        }
    }

/**
 * toResource
 *
 * Transforms ResponseWrapper into a Resource to pass to the app
 */
fun <T : Any> ResponseWrapper<T>.toResource(): Resource<T> {
    return when (this) {
        is ResponseWrapper.Success -> Resource.Success(this.data)
        is ResponseWrapper.Error<*, *> -> Resource.Error(this.error)
    }
}

/**
 * asSuccess()
 *
 * Returns the receiver object in a ResponseWrapper.Success
 */
fun <T : Any> T.asSuccess() = ResponseWrapper.Success(this)

/**
 * asError()
 *
 * Return the receiver object as a ResponseWrapper.Error
 */
fun <T : Any, U : Any, E : DataError<T, U>> E.asError() = ResponseWrapper.Error(this)

/**
 * onError
 *
 * Function to run an action when the Result is Error. Action is not called if the response is a Success.
 */
inline fun <T : Any> ResponseWrapper<T>.onError(action: (DataError<*, *>) -> Unit): ResponseWrapper<T> {
    if (this is ResponseWrapper.Error<*, *>) {
        action(this.error)
    }
    return this
}

/**
 * onSuccess
 *
 * Function to run an action when the Result is Success. Action is not called if the response is an Error.
 */
inline fun <T : Any> ResponseWrapper<T>.onSuccess(action: (T) -> Unit): ResponseWrapper<T> {
    if (this is ResponseWrapper.Success) {
        action(this.data)
    }
    return this
}