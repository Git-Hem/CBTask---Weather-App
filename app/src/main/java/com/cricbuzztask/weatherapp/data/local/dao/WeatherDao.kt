package com.cricbuzztask.weatherapp.data.local.dao


import androidx.room.*
import com.cricbuzztask.weatherapp.data.local.entity.CityEntity
import com.cricbuzztask.weatherapp.data.local.entity.ForecastEntity
import com.cricbuzztask.weatherapp.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    // Weather operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_forecast WHERE cityName = :cityName ORDER BY cachedAt DESC LIMIT 1")
    suspend fun getWeatherByCity(cityName: String): WeatherEntity?

    @Query("SELECT * FROM weather_forecast WHERE cityName = :cityName ORDER BY cachedAt DESC LIMIT 1")
    fun getWeatherByCityFlow(cityName: String): Flow<WeatherEntity?>

    @Query("DELETE FROM weather_forecast WHERE cityName = :cityName")
    suspend fun deleteWeatherByCity(cityName: String)

    // Forecast operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: List<ForecastEntity>)

    @Query("SELECT * FROM forecast_items WHERE cityName = :cityName ORDER BY timestamp ASC")
    suspend fun getForecastByCity(cityName: String): List<ForecastEntity>

    @Query("SELECT * FROM forecast_items WHERE cityName = :cityName ORDER BY timestamp ASC")
    fun getForecastByCityFlow(cityName: String): Flow<List<ForecastEntity>>

    @Query("DELETE FROM forecast_items WHERE cityName = :cityName")
    suspend fun deleteForecastByCity(cityName: String)

    // City operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    @Query("SELECT * FROM saved_cities ORDER BY addedAt DESC")
    fun getAllCities(): Flow<List<CityEntity>>

    @Query("SELECT * FROM saved_cities ORDER BY addedAt DESC")
    suspend fun getAllCitiesList(): List<CityEntity>

    @Delete
    suspend fun deleteCity(city: CityEntity)

    @Query("DELETE FROM saved_cities WHERE cityName = :cityName")
    suspend fun deleteCityByName(cityName: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_cities WHERE cityName = :cityName)")
    suspend fun isCitySaved(cityName: String): Boolean

    // Clear all data
    @Query("DELETE FROM weather_forecast")
    suspend fun clearAllWeather()

    @Query("DELETE FROM forecast_items")
    suspend fun clearAllForecast()

    @Query("DELETE FROM saved_cities")
    suspend fun clearAllCities()
}