package com.cricbuzztask.weatherapp.presentation.locations


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricbuzztask.weatherapp.domain.usecase.AddCityUseCase
import com.cricbuzztask.weatherapp.domain.usecase.GetSavedCitiesUseCase
import com.cricbuzztask.weatherapp.domain.usecase.RemoveCityUseCase
import com.cricbuzztask.weatherapp.util.Result
import com.cricbuzztask.weatherapp.util.Result.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val getSavedCitiesUseCase: GetSavedCitiesUseCase,
    private val addCityUseCase: AddCityUseCase,
    private val removeCityUseCase: RemoveCityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationsUiState())
    val uiState: StateFlow<LocationsUiState> = _uiState.asStateFlow()

    init {
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            getSavedCitiesUseCase().collect { cities ->
                _uiState.value = _uiState.value.copy(cities = cities)
            }
        }
    }

    fun addCity(cityName: String) {
        if (cityName.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = addCityUseCase(cityName.trim())) {
                is Success<*> -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        showAddDialog = false,
                        error = null
                    )
                }
                is Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun removeCity(cityName: String) {
        viewModelScope.launch {
            when (removeCityUseCase(cityName)) {
                is Success<*> -> {
                    // City removed successfully
                }
                is Error -> {
                    _uiState.value = _uiState.value.copy(error = "Failed to remove city")
                }
                is Loading -> {}
            }
        }
    }

    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = true, error = null)
    }

    fun hideAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false, error = null)
    }


    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}