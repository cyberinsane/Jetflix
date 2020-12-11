package com.cyberinsane.jetflix.data.model

/**
 * ResponseWrapper
 *
 * Wrapper for data/error between data and domain modules
 */
sealed class ResponseWrapper<out T : Any> {

    data class Success<out T : Any>(val data: T) : ResponseWrapper<T>()

    data class Error<T : Any, U : Any>(val error: DataError<T, U>) : ResponseWrapper<Nothing>()
}
