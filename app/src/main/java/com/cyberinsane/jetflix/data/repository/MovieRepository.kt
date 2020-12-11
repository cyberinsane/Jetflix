package com.cyberinsane.jetflix.data.repository

import com.cyberinsane.jetflix.data.service.MovieService
import javax.inject.Inject

class MovieRepository @Inject constructor(val service: MovieService) : BaseRepository() {

    suspend fun getPopular() = execute {
        service.getPopular()
    }

    suspend fun getTrending() = execute {
        service.getTrending()
    }

    suspend fun getTopRated() = execute {
        service.getTopRated()
    }

    suspend fun getNowPlaying() = execute {
        service.getNowPlaying()
    }

}