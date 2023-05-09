package com.example.weather.data.model

import com.example.weather.utils.ApiGeneralParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather (
    val temperature: Float? = null,
    @SerialName("windspeed") val windSpeed: Float? = null,
    @SerialName("winddirection") val windDirection: Float? = null,
    @SerialName("weathercode") val weatherCode: Int? = null,
    @SerialName("is_day") val isDay: Int? = null,
    val time: Long? = null
) {
    @Serializable
    data class Response(
        @SerialName(value = ApiGeneralParameters.CURRENT_WEATHER) val body: CurrentWeather
    )
}