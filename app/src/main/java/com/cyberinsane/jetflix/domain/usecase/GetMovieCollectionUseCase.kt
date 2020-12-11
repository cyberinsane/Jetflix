package com.cyberinsane.jetflix.domain.usecase

import androidx.lifecycle.liveData
import com.cyberinsane.jetflix.data.repository.MovieRepository
import com.cyberinsane.jetflix.domain.model.MovieCollection
import com.cyberinsane.jetflix.domain.model.Resource
import com.cyberinsane.jetflix.domain.model.ext.onError
import com.cyberinsane.jetflix.domain.model.ext.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetMovieCollectionUseCase @Inject constructor(private val movieRepository: MovieRepository) :
    BaseUseCase<Unit, MovieCollection>() {

    override suspend fun createSuspend(data: Unit): Resource<MovieCollection> {

        liveData<String>(Dispatchers.IO) {
            emit("LOL")
        }

        return coroutineScope {

            println("usecase coroutineScope Thread ${Thread.currentThread().name}")

            val nowPlayingRequest = async {
                println("async coroutineScope Thread ${Thread.currentThread().name}")
                movieRepository.getNowPlaying()
            }
            val popularRequest = async { movieRepository.getPopular() }
            val topRatedRequest = async { movieRepository.getTopRated() }
            val trendingRequest = async { movieRepository.getTrending() }

            var result = MovieCollection()
            nowPlayingRequest.await().onSuccess {
                result = result.copy(nowPlaying = it.shows)
            }.onError { return@coroutineScope Resource.Error(it) }

            popularRequest.await().onSuccess {
                result = result.copy(popular = it.shows)
            }.onError { return@coroutineScope Resource.Error(it) }

            topRatedRequest.await().onSuccess {
                result = result.copy(topRated = it.shows)
            }.onError { return@coroutineScope Resource.Error(it) }

            trendingRequest.await().onSuccess {
                result = result.copy(trending = it.shows)
            }.onError { return@coroutineScope Resource.Error(it) }

            return@coroutineScope Resource.Success(result)
        }

    }
}