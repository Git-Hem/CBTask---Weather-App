package com.cricbuzztask.weatherapp.util

object WeatherUtils {

    fun getWeatherIconUrl(iconCode: String): String {
        return "https://openweathermap.org/img/wn/${iconCode}@2x.png"
    }

    fun getWeatherDescription(condition: String): String {
        return condition.lowercase().replaceFirstChar { it.uppercase() }
    }

    fun formatTemperature(temp: Double): String {
        return "${temp.toInt()}°C"
    }

    fun getWeatherEmoji(condition: String): String {
        return when (condition.lowercase()) {
            "clear", "clear sky" -> "☀️"
            "clouds", "few clouds", "scattered clouds", "broken clouds", "overcast clouds" -> "☁️"
            "rain", "light rain", "moderate rain", "heavy rain", "shower rain" -> "🌧️"
            "drizzle", "light drizzle" -> "🌦️"
            "thunderstorm" -> "⛈️"
            "snow", "light snow", "heavy snow" -> "❄️"
            "mist", "fog", "haze" -> "🌫️"
            else -> "🌡️"
        }
    }

    fun isRainy(condition: String): Boolean {
        val rainyConditions = listOf("rain", "drizzle", "shower", "thunderstorm")
        return rainyConditions.any { condition.lowercase().contains(it) }
    }

    fun isSunny(condition: String): Boolean {
        return condition.lowercase().contains("clear")
    }

    fun isCloudy(condition: String): Boolean {
        return condition.lowercase().contains("cloud")
    }
}