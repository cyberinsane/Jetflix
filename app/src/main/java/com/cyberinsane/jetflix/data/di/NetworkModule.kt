package com.cyberinsane.jetflix.data.di

import com.cyberinsane.jetflix.data.service.TVService
import com.cyberinsane.jetflix.data.service.getDefaultHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.ktor.client.*
import io.ktor.util.*
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    @KtorExperimentalAPI
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return getDefaultHttpClient()
    }

    @Provides
    @Singleton
    fun provideTvService(client: HttpClient): TVService {
        return TVService(client)
    }

}
