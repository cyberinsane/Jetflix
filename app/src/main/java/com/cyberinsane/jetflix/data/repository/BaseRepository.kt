package com.cyberinsane.jetflix.data.repository

import com.cyberinsane.jetflix.data.model.DataError
import com.cyberinsane.jetflix.data.model.ResponseWrapper
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * BaseRepository
 *
 * Base repository to execute network requests and error handling
 */
open class BaseRepository {

    suspend fun <T : Any> execute(call: suspend () -> T) = try {
        ResponseWrapper.Success(call())
    } catch (ex: Throwable) {
        wrapException<T>(ex)
    }

    fun <T : Any> wrapException(ex: Throwable) = when (ex) {
        is SocketTimeoutException -> {
            ResponseWrapper.Error(DataError.TimeoutError(ex))
        }
        is IOException -> {
            ResponseWrapper.Error(DataError.NetworkError(ex))
        }
        else -> {
            ResponseWrapper.Error(DataError.UnknownError(ex))
        }
    }
}