package com.cricbuzztask.weatherapp.di

import com.cricbuzztask.weatherapp.data.local.dao.WeatherDao
import com.cricbuzztask.weatherapp.data.remote.api.WeatherApiService
import com.cricbuzztask.weatherapp.data.repository.WeatherRepositoryImpl
import com.cricbuzztask.weatherapp.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        apiService: WeatherApiService,
        weatherDao: WeatherDao
    ): WeatherRepository {
        return WeatherRepositoryImpl(apiService, weatherDao)
    }
}