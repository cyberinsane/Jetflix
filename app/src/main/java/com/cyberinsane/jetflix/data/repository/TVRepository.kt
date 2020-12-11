package com.cyberinsane.jetflix.data.repository

import com.cyberinsane.jetflix.data.service.TVService
import javax.inject.Inject

class TVRepository @Inject constructor(private val service: TVService) : BaseRepository() {

    suspend fun getPopular() = execute {
        service.getPopular()
    }

    suspend fun getTrending() = execute {
        service.getTrending()
    }

    suspend fun getTopRated() = execute {
        service.getTopRated()
    }

}