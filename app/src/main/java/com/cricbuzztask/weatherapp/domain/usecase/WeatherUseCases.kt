package com.cricbuzztask.weatherapp.domain.usecase
import com.cricbuzztask.weatherapp.domain.model.City
import com.cricbuzztask.weatherapp.domain.model.Forecast
import com.cricbuzztask.weatherapp.domain.model.Weather
import com.cricbuzztask.weatherapp.util.Result
import com.cricbuzztask.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String, forceRefresh: Boolean = false): Result<Weather> {
        return repository.getCurrentWeather(cityName, forceRefresh)
    }
}

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String, forceRefresh: Boolean = false): Result<List<Forecast>> {
        return repository.getForecast(cityName, forceRefresh)
    }
}

class GetCachedWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(cityName: String): Flow<Weather?> {
        return repository.getCachedWeather(cityName)
    }
}

class GetCachedForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(cityName: String): Flow<List<Forecast>> {
        return repository.getCachedForecast(cityName)
    }
}

class AddCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): Result<Unit> {
        return repository.addCity(cityName)
    }
}

class RemoveCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): Result<Unit> {
        return repository.removeCity(cityName)
    }
}

class GetSavedCitiesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<List<City>> {
        return repository.getSavedCities()
    }
}