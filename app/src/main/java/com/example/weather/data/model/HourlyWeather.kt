package com.example.weather.data.model

import com.example.weather.utils.ApiGeneralParameters
import com.example.weather.utils.ApiHourlyWeatherParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyWeather(
    val time: List<Long>? = null,
    @SerialName(value = ApiHourlyWeatherParameters.TEMPERATURE)val temperature: List<Float>? = null,
    @SerialName(value = ApiHourlyWeatherParameters.APPARENT_TEMPERATURE) val apparentTemperature: List<Float>? = null,
    @SerialName(value = ApiHourlyWeatherParameters.HUMIDITY) val humidity: List<Float>? = null,
    @SerialName(value = ApiHourlyWeatherParameters.VISIBILITY) val visibility: List<Float>? = null,
    @SerialName(value = ApiHourlyWeatherParameters.PRESSURE_MSL) val pressureMsl: List<Float>? = null,
    @SerialName(value = ApiHourlyWeatherParameters.PRESSURE_SURFACE) val pressureSurface: List<Float>? = null,
    @SerialName(value = ApiHourlyWeatherParameters.WEATHER_CODE) val weatherCode: List<Int>? = null,
    @SerialName(value = ApiHourlyWeatherParameters.IS_DAY) val isDay: List<Int>? = null,
    @SerialName(value = ApiHourlyWeatherParameters.PRECIPITATION) val precipitation: List<Float>? = null,
    @SerialName(value = ApiHourlyWeatherParameters.PRECIPITATION_PROBABILITY) val precipitationProbability: List<Float>? = null
) {
    @Serializable
    data class Response(
        @SerialName(value = ApiGeneralParameters.HOURLY) val body: HourlyWeather
    )
}