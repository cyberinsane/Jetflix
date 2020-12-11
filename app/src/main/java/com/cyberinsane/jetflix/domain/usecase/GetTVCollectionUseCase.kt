package com.cyberinsane.jetflix.domain.usecase

import com.cyberinsane.jetflix.data.repository.TVRepository
import com.cyberinsane.jetflix.domain.model.Resource
import com.cyberinsane.jetflix.domain.model.TVCollection
import com.cyberinsane.jetflix.domain.model.ext.onError
import com.cyberinsane.jetflix.domain.model.ext.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetTVCollectionUseCase @Inject constructor(val tvRepository: TVRepository) :
    BaseUseCase<Unit, TVCollection>() {
    override suspend fun createSuspend(data: Unit): Resource<TVCollection> {
        return coroutineScope {
            val popularResult = async { tvRepository.getPopular() }
            val trendingResult = async { tvRepository.getTrending() }
            val topRatedResult = async { tvRepository.getTopRated() }

            var result = TVCollection()
            popularResult.await().onSuccess {
                result = result.copy(popular = it.shows)
            }.onError { return@coroutineScope Resource.Error(it) }
            trendingResult.await().onSuccess {
                result = result.copy(trending = it.shows)
            }.onError { return@coroutineScope Resource.Error(it) }
            topRatedResult.await().onSuccess {
                result = result.copy(topRated = it.shows)
            }.onError { return@coroutineScope Resource.Error(it) }

            return@coroutineScope Resource.Success(result)
        }
    }
}