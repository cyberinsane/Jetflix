package com.cyberinsane.jetflix.data.service

import com.cyberinsane.jetflix.data.model.ShowResult
import io.ktor.client.*
import io.ktor.client.request.*
import javax.inject.Inject


class TVService @Inject constructor(private val client: HttpClient) {

    suspend fun getPopular() = client.get<ShowResult> { path("/tv/popular") }

    suspend fun getTopRated() = client.get<ShowResult> { path("/tv/top_rated") }

    suspend fun getTrending() = client.get<ShowResult> { path("/trending/tv/week") }

}