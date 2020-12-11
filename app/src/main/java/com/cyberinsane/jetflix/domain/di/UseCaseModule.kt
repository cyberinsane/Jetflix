package com.cyberinsane.jetflix.domain.di

import com.cyberinsane.jetflix.data.repository.MovieRepository
import com.cyberinsane.jetflix.data.repository.TVRepository
import com.cyberinsane.jetflix.domain.usecase.GetMovieCollectionUseCase
import com.cyberinsane.jetflix.domain.usecase.GetTVCollectionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@InstallIn(ApplicationComponent::class)
@Module
object UseCaseModule {

    @Provides
    fun provideGetTVCollectionUseCase(tvRepository: TVRepository) =
        GetTVCollectionUseCase(tvRepository)

    @Provides
    fun provideGetMovieCollectionUseCase(movieRepository: MovieRepository) =
        GetMovieCollectionUseCase(movieRepository)

}