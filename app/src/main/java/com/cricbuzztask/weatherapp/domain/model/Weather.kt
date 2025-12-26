package com.cricbuzztask.weatherapp.domain.model

data class Weather(
    val cityName: String,
    val temperature: Double,
    val tempMin: Double,
    val tempMax: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val windSpeed: Double,
    val timestamp: Long,
    val date: String,
    val sunrise: Long?,
    val sunset: Long?
)

data class Forecast(
    val cityName: String,
    val timestamp: Long,
    val temperature: Double,
    val tempMin: Double,
    val tempMax: Double,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val humidity: Int,
    val windSpeed: Double,
    val dateText: String
)

data class City(
    val name: String,
    val currentTemp: Double,
    val weatherDescription: String,
    val weatherIcon: String
)

data class DailyForecast(
    val date: String,
    val timestamp: Long,
    val tempMin: Double,
    val tempMax: Double,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String
)