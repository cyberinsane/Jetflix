package com.cyberinsane.jetflix.data.service

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*

private const val BASE_URL = "https://api.themoviedb.org/"
private const val API_VERSION = "3"
private const val API_KEY_KEY = "api_key"
private const val API_KEY_VALUE = "3298880137172cea814f46805f3df4fe"

@KtorExperimentalAPI
fun getDefaultHttpClient() = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = GsonSerializer()
    }
}

fun HttpRequestBuilder.path(path: String, params: List<Pair<String, Any?>>? = null) {
    // header(HttpHeaders.CacheControl, "no-cache")
    url {
        takeFrom(BASE_URL)
        encodedPath = API_VERSION + path
        params?.forEach { parameter(it.first, it.second) }
        parameter(API_KEY_KEY, API_KEY_VALUE)
    }
}