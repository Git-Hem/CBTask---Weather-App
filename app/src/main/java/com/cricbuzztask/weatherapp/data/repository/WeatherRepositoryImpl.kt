package com.cricbuzztask.weatherapp.data.repository


import com.cricbuzztask.weatherapp.BuildConfig
import com.cricbuzztask.weatherapp.data.local.dao.WeatherDao
import com.cricbuzztask.weatherapp.data.local.entity.CityEntity
import com.cricbuzztask.weatherapp.data.local.entity.ForecastEntity
import com.cricbuzztask.weatherapp.data.local.entity.WeatherEntity
import com.cricbuzztask.weatherapp.data.remote.api.WeatherApiService
import com.cricbuzztask.weatherapp.data.remote.dto.ForecastResponse
import com.cricbuzztask.weatherapp.data.remote.dto.WeatherResponse
import com.cricbuzztask.weatherapp.domain.model.City
import com.cricbuzztask.weatherapp.domain.model.Forecast
import com.cricbuzztask.weatherapp.domain.model.Weather
import com.cricbuzztask.weatherapp.domain.repository.WeatherRepository
import com.cricbuzztask.weatherapp.util.Constants
import com.cricbuzztask.weatherapp.util.DateUtils
import com.cricbuzztask.weatherapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService,
    private val weatherDao: WeatherDao
) : WeatherRepository {

    override suspend fun getCurrentWeather(cityName: String, forceRefresh: Boolean): Result<Weather> {
        return try {
            // Check cache first if not forcing refresh
            if (!forceRefresh) {
                val cachedWeather = weatherDao.getWeatherByCity(cityName)
                if (cachedWeather != null && !DateUtils.isExpired(
                        cachedWeather.cachedAt,
                        Constants.CACHE_DURATION_MINUTES
                    )
                ) {
                    return Result.Success(cachedWeather.toWeather())
                }
            }

            // Fetch from API
            val response = apiService.getCurrentWeather(
                city = cityName,
                apiKey = BuildConfig.API_KEY,
                units = Constants.UNITS_METRIC
            )

            // Map and cache
            val weather = response.toWeather()
            weatherDao.insertWeather(weather.toEntity())

            Result.Success(weather)
        } catch (e: Exception) {
            // Try to return cached data on error
            val cachedWeather = weatherDao.getWeatherByCity(cityName)
            if (cachedWeather != null) {
                Result.Success(cachedWeather.toWeather())
            } else {
                Result.Error("Failed to fetch weather: ${e.message}", e)
            }
        }
    }

    override suspend fun getForecast(cityName: String, forceRefresh: Boolean): Result<List<Forecast>> {
        return try {
            // Check cache first if not forcing refresh
            if (!forceRefresh) {
                val cachedForecast = weatherDao.getForecastByCity(cityName)
                if (cachedForecast.isNotEmpty() && !DateUtils.isExpired(
                        cachedForecast.first().cachedAt,
                        Constants.CACHE_DURATION_MINUTES
                    )
                ) {
                    return Result.Success(cachedForecast.map { it.toForecast() })
                }
            }

            // Fetch from API
            val response = apiService.getForecast(
                city = cityName,
                apiKey = BuildConfig.API_KEY,
                units = Constants.UNITS_METRIC,
                count = Constants.FORECAST_ITEMS
            )

            // Map and cache
            val forecasts = response.toForecastList()
            weatherDao.deleteForecastByCity(cityName)
            weatherDao.insertForecast(forecasts.map { it.toEntity(cityName) })

            Result.Success(forecasts)
        } catch (e: Exception) {
            // Try to return cached data on error
            val cachedForecast = weatherDao.getForecastByCity(cityName)
            if (cachedForecast.isNotEmpty()) {
                Result.Success(cachedForecast.map { it.toForecast() })
            } else {
                Result.Error("Failed to fetch forecast: ${e.message}", e)
            }
        }
    }

    override fun getCachedWeather(cityName: String): Flow<Weather?> {
        return weatherDao.getWeatherByCityFlow(cityName).map { it?.toWeather() }
    }

    override fun getCachedForecast(cityName: String): Flow<List<Forecast>> {
        return weatherDao.getForecastByCityFlow(cityName).map { list ->
            list.map { it.toForecast() }
        }
    }

    override suspend fun addCity(cityName: String): Result<Unit> {
        return try {
            // Fetch current weather for the city
            val weather = getCurrentWeather(cityName, forceRefresh = true)

            when (weather) {
                is Result.Success -> {
                    val cityEntity = CityEntity(
                        cityName = weather.data.cityName,
                        currentTemp = weather.data.temperature,
                        weatherDescription = weather.data.weatherDescription,
                        weatherIcon = weather.data.weatherIcon
                    )
                    weatherDao.insertCity(cityEntity)
                    Result.Success(Unit)
                }
                is Result.Error -> weather
                is Result.Loading -> Result.Error("Unexpected loading state")
            }
        } catch (e: Exception) {
            Result.Error("Failed to add city: ${e.message}", e)
        }
    }

    override suspend fun removeCity(cityName: String): Result<Unit> {
        return try {
            weatherDao.deleteCityByName(cityName)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Failed to remove city: ${e.message}", e)
        }
    }

    override fun getSavedCities(): Flow<List<City>> {
        return weatherDao.getAllCities().map { list ->
            list.map { it.toCity() }
        }
    }

    override suspend fun isCitySaved(cityName: String): Boolean {
        return weatherDao.isCitySaved(cityName)
    }
}

// Extension functions for mapping

private fun WeatherResponse.toWeather(): Weather {
    return Weather(
        cityName = name,
        temperature = main.temp,
        tempMin = main.tempMin,
        tempMax = main.tempMax,
        feelsLike = main.feelsLike,
        humidity = main.humidity,
        pressure = main.pressure,
        weatherMain = weather.firstOrNull()?.main ?: "",
        weatherDescription = weather.firstOrNull()?.description ?: "",
        weatherIcon = weather.firstOrNull()?.icon ?: "",
        windSpeed = wind?.speed ?: 0.0,
        timestamp = dt,
        date = DateUtils.formatDate(dt),
        sunrise = sys?.sunrise,
        sunset = sys?.sunset
    )
}

private fun ForecastResponse.toForecastList(): List<Forecast> {
    return list.map { item ->
        Forecast(
            cityName = city.name,
            timestamp = item.dt,
            temperature = item.main.temp,
            tempMin = item.main.tempMin,
            tempMax = item.main.tempMax,
            weatherMain = item.weather.firstOrNull()?.main ?: "",
            weatherDescription = item.weather.firstOrNull()?.description ?: "",
            weatherIcon = item.weather.firstOrNull()?.icon ?: "",
            humidity = item.main.humidity,
            windSpeed = item.wind?.speed ?: 0.0,
            dateText = item.dtTxt
        )
    }
}

private fun Weather.toEntity(): WeatherEntity {
    return WeatherEntity(
        cityName = cityName,
        temperature = temperature,
        tempMin = tempMin,
        tempMax = tempMax,
        feelsLike = feelsLike,
        humidity = humidity,
        pressure = pressure,
        weatherMain = weatherMain,
        weatherDescription = weatherDescription,
        weatherIcon = weatherIcon,
        windSpeed = windSpeed,
        timestamp = timestamp,
        date = date,
        sunrise = sunrise,
        sunset = sunset
    )
}

private fun WeatherEntity.toWeather(): Weather {
    return Weather(
        cityName = cityName,
        temperature = temperature,
        tempMin = tempMin,
        tempMax = tempMax,
        feelsLike = feelsLike,
        humidity = humidity,
        pressure = pressure,
        weatherMain = weatherMain,
        weatherDescription = weatherDescription,
        weatherIcon = weatherIcon,
        windSpeed = windSpeed,
        timestamp = timestamp,
        date = date,
        sunrise = sunrise,
        sunset = sunset
    )
}

private fun Forecast.toEntity(city: String): ForecastEntity {
    return ForecastEntity(
        cityName = city,
        timestamp = timestamp,
        temperature = temperature,
        tempMin = tempMin,
        tempMax = tempMax,
        weatherMain = weatherMain,
        weatherDescription = weatherDescription,
        weatherIcon = weatherIcon,
        humidity = humidity,
        windSpeed = windSpeed,
        dateText = dateText
    )
}

private fun ForecastEntity.toForecast(): Forecast {
    return Forecast(
        cityName = cityName,
        timestamp = timestamp,
        temperature = temperature,
        tempMin = tempMin,
        tempMax = tempMax,
        weatherMain = weatherMain,
        weatherDescription = weatherDescription,
        weatherIcon = weatherIcon,
        humidity = humidity,
        windSpeed = windSpeed,
        dateText = dateText
    )
}

private fun CityEntity.toCity(): City {
    return City(
        name = cityName,
        currentTemp = currentTemp,
        weatherDescription = weatherDescription,
        weatherIcon = weatherIcon
    )
}