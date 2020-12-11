package com.cyberinsane.jetflix.domain.model.ext

import com.cyberinsane.jetflix.data.model.DataError
import com.cyberinsane.jetflix.domain.model.Resource

/**
 * flatMap
 *
 * Utility method to chain sequential resource calls.
 */
inline fun <T : Any, U : Any> Resource<T>.flatMap(f: (T) -> Resource<U>) =
    when (val result = map(f)) {
        is Resource.Success -> result.data
        is Resource.Error<*, *> -> result
    }

/**
 * tap
 *
 * Utility method to chain sequential resource calls.
 */
inline fun <T : Any, U : Any> Resource<T>.tap(f: (T) -> U) = when (this) {
    is Resource.Success -> {
        f(this.data)
        this
    }
    is Resource.Error<*, *> -> {
        this
    }
}

/**
 * filter
 *
 * Utility method to filter resource by predicate
 */
inline fun <T : Any> Resource<T>.filter(predicate: (T) -> Boolean) =
    when {
        this is Resource.Success && predicate(this.data) -> this
        this is Resource.Success -> Resource.Error(
            DataError.ValidationError(
                "Predicate not met for filter.",
                -1
            )
        )
        else -> this
    }

/**
 * map
 *
 * Utility method to apply a function to a Resource.Success. Errors remain unaffected.
 */
inline fun <T : Any, U : Any> Resource<T>.map(f: (T) -> U) = when (this) {
    is Resource.Success -> Resource.Success(f(this.data))
    is Resource.Error<*, *> -> this
}

/**
 * zip
 *
 * Zip two Resource.Success into a Pair. If one is an error, then the error is presented.
 */
fun <T : Any, U : Any> Resource<T>.zip(other: Resource<U>) = when (this) {
    is Resource.Success -> other.map { Pair(this.data, it) }
    is Resource.Error<*, *> -> this
}

/**
 * onError
 *
 * Function to run an action when the Result is Error. Action is not called if the response is a Success.
 */
inline fun <T : Any> Resource<T>.onError(action: (DataError<*, *>) -> Unit): Resource<T> {
    if (this is Resource.Error<*, *>) {
        action(this.error)
    }
    return this
}

/**
 * onSuccess
 *
 * Function to run an action when the Result is Success. Action is not called if the response is an Error.
 */
inline fun <T : Any> Resource<T>.onSuccess(action: (T) -> Unit): Resource<T> {
    if (this is Resource.Success) {
        action(this.data)
    }
    return this
}