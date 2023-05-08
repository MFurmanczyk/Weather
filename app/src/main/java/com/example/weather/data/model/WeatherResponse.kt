package com.example.weather.data.model

import com.example.weather.utils.ApiGeneralParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("latitude") val latitude: Float,
    @SerialName("longitude") val longitude: Float,
    @SerialName("generationtime_ms") val generationTimeInMillis: Float,
    @SerialName("utc_offset_seconds") val utcOffsetInSeconds: Int,
    @SerialName("timezone") val timeZone: String,
    @SerialName("timezone_abbreviation") val timeZoneAbbreviation: String,
    @SerialName("elevation") val elevation: Float,
    @SerialName(ApiGeneralParameters.CURRENT_WEATHER) val currentWeather: CurrentWeather? = null,
    @SerialName(ApiGeneralParameters.HOURLY) val hourlyWeather: HourlyWeather? = null,
    @SerialName(ApiGeneralParameters.DAILY) val dailyForecast: DailyForecast? = null
)
