package com.cricbuzztask.weatherapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricbuzztask.weatherapp.domain.model.DailyForecast
import com.cricbuzztask.weatherapp.domain.model.Forecast
import com.cricbuzztask.weatherapp.domain.model.Weather
import com.cricbuzztask.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.cricbuzztask.weatherapp.domain.usecase.GetForecastUseCase
import com.cricbuzztask.weatherapp.util.Constants
import com.cricbuzztask.weatherapp.util.DateUtils
import com.cricbuzztask.weatherapp.util.LocationManager
import com.cricbuzztask.weatherapp.util.PreferencesManager
import com.cricbuzztask.weatherapp.util.Result.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val locationManager: LocationManager,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        initializeLocation()
    }

    private fun initializeLocation() {
        viewModelScope.launch {
            val savedCity = preferencesManager.getCurrentCity()

            if (savedCity != null) {
                // Load saved city
                loadWeather(savedCity)
            } else if (locationManager.hasLocationPermission()) {
                // Try to get GPS location
                val currentLocation = locationManager.getCurrentLocation()
                if (currentLocation != null) {
                    preferencesManager.saveCurrentCity(currentLocation)
                    loadWeather(currentLocation)
                } else {
                    // Fall back to default city
                    loadWeather(Constants.DEFAULT_CITY)
                }
            } else {
                // Request permission first time
                _uiState.value = _uiState.value.copy(showLocationPermissionDialog = true)
            }
        }
    }

    fun onLocationPermissionGranted() {
        _uiState.value = _uiState.value.copy(showLocationPermissionDialog = false)
        preferencesManager.setAskedLocationPermission()

        viewModelScope.launch {
            val currentLocation = locationManager.getCurrentLocation()
            if (currentLocation != null) {
                preferencesManager.saveCurrentCity(currentLocation)
                loadWeather(currentLocation)
            } else {
                loadWeather(Constants.DEFAULT_CITY)
            }
        }
    }

    fun onLocationPermissionDenied() {
        _uiState.value = _uiState.value.copy(showLocationPermissionDialog = false)
        preferencesManager.setAskedLocationPermission()
        loadWeather(Constants.DEFAULT_CITY)
    }

    fun loadWeather(cityName: String, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            // Save the city
            preferencesManager.saveCurrentCity(cityName)

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                currentCity = cityName
            )

            // Load current weather
            when (val result = getCurrentWeatherUseCase(cityName, forceRefresh)) {
                is Success<*> -> {
                    _uiState.value = _uiState.value.copy(
                        weather = result.data as Weather?,
                        isLoading = false,
                        error = null,
                        isOffline = false
                    )
                }
                is Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message,
                        isOffline = result.message.contains("network", ignoreCase = true)
                    )
                }
                is Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }

            // Load forecast
            when (val forecastResult = getForecastUseCase(cityName, forceRefresh)) {
                is Success<*> -> {
                    val forecasts = forecastResult.data as List<Forecast>
                    val dailyForecasts = groupForecastsByDay(forecasts)
                    _uiState.value = _uiState.value.copy(
                        hourlyForecasts = forecasts,
                        dailyForecasts = dailyForecasts
                    )
                }
                is Error -> {
                    // Forecast error, but keep showing current weather
                }
                is Loading -> {}
            }
        }
    }

    private fun groupForecastsByDay(forecasts: List<Forecast>): List<DailyForecast> {
        val grouped = forecasts.groupBy { forecast ->
            DateUtils.formatDate(forecast.timestamp, "dd MMM")
        }

        return grouped.map { (date, items) ->
            DailyForecast(
                date = date,
                timestamp = items.first().timestamp,
                tempMin = items.minOf { it.tempMin },
                tempMax = items.maxOf { it.tempMax },
                weatherMain = items.first().weatherMain,
                weatherDescription = items.first().weatherDescription,
                weatherIcon = items.first().weatherIcon
            )
        }.take(3)
    }

    fun refreshWeather() {
        loadWeather(_uiState.value.currentCity, forceRefresh = true)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}