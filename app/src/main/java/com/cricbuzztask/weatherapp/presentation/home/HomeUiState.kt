package com.cricbuzztask.weatherapp.presentation.home

import com.cricbuzztask.weatherapp.domain.model.DailyForecast
import com.cricbuzztask.weatherapp.domain.model.Forecast
import com.cricbuzztask.weatherapp.domain.model.Weather

data class HomeUiState(
    val weather: Weather? = null,
    val hourlyForecasts: List<Forecast> = emptyList(),
    val dailyForecasts: List<DailyForecast> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOffline: Boolean = false,
    val currentCity: String = "Mumbai",
    val showLocationPermissionDialog: Boolean = false
)