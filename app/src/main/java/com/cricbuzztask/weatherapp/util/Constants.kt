package com.cricbuzztask.weatherapp.util

object Constants {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val DATABASE_NAME = "weather_database"

    // API Endpoints
    const val FORECAST_ENDPOINT = "forecast"
    const val CURRENT_WEATHER_ENDPOINT = "weather"

    // API Parameters
    const val PARAM_CITY = "q"
    const val PARAM_API_KEY = "appid"
    const val PARAM_UNITS = "units"
    const val PARAM_CNT = "cnt"

    // Units
    const val UNITS_METRIC = "metric"

    // Default values
    const val DEFAULT_CITY = "Mumbai"
    const val FORECAST_DAYS = 3
    const val FORECAST_ITEMS = 24 // 3 days * 8 (3-hour intervals)

    // Cache duration
    const val CACHE_DURATION_MINUTES = 30

    // Date formats
    const val DATE_FORMAT_FULL = "EEEE, dd MMMM yyyy"
    const val DATE_FORMAT_DAY = "dd MMM"
    const val DATE_FORMAT_TIME = "hh:mm a"
    const val DATE_FORMAT_HOUR = "HH:mm"
}