package com.cyberinsane.jetflix.domain.model

import com.cyberinsane.jetflix.data.model.DataError


/**
 * Resource
 *
 * Wrapper class for data/errors passed to the app layer
 */
sealed class Resource<out T : Any> {

    /**
     * Represents a successful retrieval.
     */
    class Success<out T : Any>(val data: T) : Resource<T>()

    /**
     * Represents a failed retrieval.
     */
    class Error<T : Any, U : Any>(val error: DataError<T, U>) :
        Resource<Nothing>()

}
