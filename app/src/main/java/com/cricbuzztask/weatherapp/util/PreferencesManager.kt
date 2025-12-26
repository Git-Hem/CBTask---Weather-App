package com.cricbuzztask.weatherapp.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(
        "weather_prefs",
        Context.MODE_PRIVATE
    )

    fun saveCurrentCity(cityName: String) {
        sharedPreferences.edit().putString(KEY_CURRENT_CITY, cityName).apply()
    }

    fun getCurrentCity(): String? {
        return sharedPreferences.getString(KEY_CURRENT_CITY, null)
    }

    fun hasAskedLocationPermission(): Boolean {
        return sharedPreferences.getBoolean(KEY_ASKED_LOCATION, false)
    }

    fun setAskedLocationPermission() {
        sharedPreferences.edit().putBoolean(KEY_ASKED_LOCATION, true).apply()
    }

    companion object {
        private const val KEY_CURRENT_CITY = "current_city"
        private const val KEY_ASKED_LOCATION = "asked_location"
    }
}