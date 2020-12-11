package com.cyberinsane.jetflix.data.model

import java.io.IOException
import java.net.SocketTimeoutException

/**
 * DataError
 *
 * Wrapper class for all types of errors
 */
sealed class DataError<out T : Any, out U : Any> {

    /**
     * Api Failure Response Error
     */
    data class ApiError<U : Any>(
        val body: U,
        val code: Int,
        val error: Throwable
    ) : DataError<Nothing, U>()

    /**
     * Network error
     */
    data class NetworkError(
        val error: IOException
    ) : DataError<Nothing, Nothing>()

    /**
     * Validation Error
     */
    data class ValidationError<U : Any>(
        val body: U,
        val code: Int,
        val error: Throwable? = null
    ) : DataError<Nothing, U>()

    /**
     * Authorization Error
     */
    data class AuthorizationError<U : Any, T : Any>(
        val message: String,
        val code: Int,
        val error: Throwable
    ) : DataError<U, T>()

    /**
     * API Time Out Error
     */
    data class TimeoutError(
        val error: SocketTimeoutException
    ) : DataError<Nothing, Nothing>()

    /**
     * Unknown Error
     */
    data class UnknownError(
        val error: Throwable
    ) : DataError<Nothing, Nothing>()
}
