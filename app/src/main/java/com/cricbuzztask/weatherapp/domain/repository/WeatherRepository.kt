package com.cricbuzztask.weatherapp.domain.repository

import com.cricbuzztask.weatherapp.domain.model.City
import com.cricbuzztask.weatherapp.domain.model.Forecast
import com.cricbuzztask.weatherapp.domain.model.Weather
import com.cricbuzztask.weatherapp.util.Result
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    // Get current weather
    suspend fun getCurrentWeather(cityName: String, forceRefresh: Boolean = false): com.cricbuzztask.weatherapp.util.Result<Weather>

    // Get forecast
    suspend fun getForecast(cityName: String, forceRefresh: Boolean = false): com.cricbuzztask.weatherapp.util.Result<List<Forecast>>

    // Get cached weather
    fun getCachedWeather(cityName: String): Flow<Weather?>

    // Get cached forecast
    fun getCachedForecast(cityName: String): Flow<List<Forecast>>

    // City management
    suspend fun addCity(cityName: String): com.cricbuzztask.weatherapp.util.Result<Unit>

    suspend fun removeCity(cityName: String): Result<Unit>

    fun getSavedCities(): Flow<List<City>>

    suspend fun isCitySaved(cityName: String): Boolean
}