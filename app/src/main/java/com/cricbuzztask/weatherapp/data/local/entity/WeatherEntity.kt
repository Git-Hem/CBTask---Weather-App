package com.cricbuzztask.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_forecast")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
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
    val sunset: Long?,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "forecast_items")
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
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
    val dateText: String,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_cities")
data class CityEntity(
    @PrimaryKey
    val cityName: String,
    val currentTemp: Double,
    val weatherDescription: String,
    val weatherIcon: String,
    val addedAt: Long = System.currentTimeMillis()
)