package com.cricbuzztask.weatherapp.data.local.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.cricbuzztask.weatherapp.data.local.dao.WeatherDao
import com.cricbuzztask.weatherapp.data.local.entity.CityEntity
import com.cricbuzztask.weatherapp.data.local.entity.ForecastEntity
import com.cricbuzztask.weatherapp.data.local.entity.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
        ForecastEntity::class,
        CityEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}