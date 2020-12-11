package com.cyberinsane.jetflix.data.service

import com.cyberinsane.jetflix.data.model.ShowResult
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject


class MovieService @Inject constructor(private val client: HttpClient) {

    suspend fun getPopular() = client.get<ShowResult> {
        println("MovieService Thread ${Thread.currentThread().name}")
        path("/movie/popular") }

    suspend fun getTopRated() = client.get<ShowResult> { path("/movie/top_rated") }

    suspend fun getTrending() = client.get<ShowResult> { path("/trending/movie/week") }

    suspend fun getNowPlaying() = client.get<ShowResult> { path("/movie/now_playing") }

}