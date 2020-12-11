package com.cyberinsane.jetflix.data.di

import com.cyberinsane.jetflix.data.repository.MovieRepository
import com.cyberinsane.jetflix.data.repository.TVRepository
import com.cyberinsane.jetflix.data.service.MovieService
import com.cyberinsane.jetflix.data.service.TVService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTvRepository(tvService: TVService) = TVRepository(tvService)

    @Provides
    @Singleton
    fun provideMovieRepository(movieService: MovieService) = MovieRepository(movieService)

}