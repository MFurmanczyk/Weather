package com.example.weather.data.model

import com.example.weather.utils.ApiDailyWeatherParameters
import com.example.weather.utils.ApiGeneralParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyWeather(
    val time: List<Long>?,
    @SerialName(value = ApiDailyWeatherParameters.TEMPERATURE_MAX) val temperatureMax: List<Float>? = null,
    @SerialName(value = ApiDailyWeatherParameters.TEMPERATURE_MIN) val temperatureMin: List<Float>? = null,
    @SerialName(value = ApiDailyWeatherParameters.PRECIPITATION) val precipitation: List<Float>? = null,
    @SerialName(value = ApiDailyWeatherParameters.PRECIPITATION_HOURS) val precipitationHours: List<Float>? = null,
    @SerialName(value = ApiDailyWeatherParameters.SUNRISE) val sunrise: List<Long>? = null,
    @SerialName(value = ApiDailyWeatherParameters.SUNSET) val sunset: List<Long>? = null,
    @SerialName(value = ApiDailyWeatherParameters.WEATHER_CODE) val weatherCode: List<Int>? = null
) {
    @Serializable
    data class Response(
        @SerialName(value = ApiGeneralParameters.DAILY) val body: DailyWeather
    )
}