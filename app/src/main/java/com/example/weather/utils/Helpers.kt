package com.example.weather.utils

import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

fun getTime(timestamp: Long?): String {
    return try {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        val netDate = Date(timestamp?.times(1000) ?: 0)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

fun getDate(timestamp: Long?): String {
    return try {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        val netDate = Date(timestamp?.times(1000) ?: 0)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}