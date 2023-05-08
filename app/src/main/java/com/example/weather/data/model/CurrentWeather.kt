package com.example.weather.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather (
    val temperature: Float,
    @SerialName("windspeed") val windSpeed: Float,
    @SerialName("winddirection") val windDirection: Float,
    @SerialName("weathercode") val weatherCode: Int,
    @SerialName("is_day") val isDay: Int,
    val time: Long
)