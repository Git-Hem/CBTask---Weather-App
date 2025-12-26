package com.cricbuzztask.weatherapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun formatDate(timestamp: Long, format: String = Constants.DATE_FORMAT_FULL): String {
        return try {
            val date = Date(timestamp * 1000)
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat(Constants.DATE_FORMAT_FULL, Locale.getDefault())
        return sdf.format(Date())
    }

    fun getTimeFromTimestamp(timestamp: Long): String {
        return formatDate(timestamp, Constants.DATE_FORMAT_TIME)
    }

    fun getHourFromTimestamp(timestamp: Long): String {
        return formatDate(timestamp, Constants.DATE_FORMAT_HOUR)
    }

    fun getDayFromTimestamp(timestamp: Long): String {
        return formatDate(timestamp, Constants.DATE_FORMAT_DAY)
    }

    fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = timestamp1 * 1000 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = timestamp2 * 1000 }

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun isExpired(timestamp: Long, durationMinutes: Int): Boolean {
        val currentTime = System.currentTimeMillis()
        val cacheTime = timestamp
        val elapsedMinutes = (currentTime - cacheTime) / (1000 * 60)
        return elapsedMinutes > durationMinutes
    }
}