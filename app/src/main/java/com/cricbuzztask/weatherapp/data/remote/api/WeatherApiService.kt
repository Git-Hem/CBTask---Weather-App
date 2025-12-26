package com.cricbuzztask.weatherapp.data.remote.api

import com.cricbuzztask.weatherapp.data.remote.dto.ForecastResponse
import com.cricbuzztask.weatherapp.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("cnt") count: Int = 24 // 3 days * 8 (3-hour intervals)
    ): ForecastResponse
}