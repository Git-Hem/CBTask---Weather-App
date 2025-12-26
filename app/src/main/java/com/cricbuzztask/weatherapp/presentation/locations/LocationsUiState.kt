package com.cricbuzztask.weatherapp.presentation.locations

import com.cricbuzztask.weatherapp.domain.model.City

data class LocationsUiState(
    val cities: List<City> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    var showAddDialog: Boolean = false
)