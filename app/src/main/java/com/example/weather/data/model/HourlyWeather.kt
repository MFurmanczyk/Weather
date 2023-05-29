package com.example.weather.data.model

import com.example.weather.utils.ApiGeneralParameters
import com.example.weather.utils.ApiHourlyWeatherParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyWeather(
    val time: List<Long>,
    @SerialName(value = ApiHourlyWeatherParameters.TEMPERATURE)val temperature: List<Float>,
    @SerialName(value = ApiHourlyWeatherParameters.APPARENT_TEMPERATURE) val apparentTemperature: List<Float>,
    @SerialName(value = ApiHourlyWeatherParameters.HUMIDITY) val humidity: List<Float>,
    @SerialName(value = ApiHourlyWeatherParameters.PRESSURE_SURFACE) val pressureSurface: List<Float>,
    @SerialName(value = ApiHourlyWeatherParameters.WEATHER_CODE) val weatherCode: List<Int>,
    @SerialName(value = ApiHourlyWeatherParameters.IS_DAY) val isDay: List<Int>,
    @SerialName(value = ApiHourlyWeatherParameters.PRECIPITATION) val precipitation: List<Float>,
    @SerialName(value = ApiHourlyWeatherParameters.PRECIPITATION_PROBABILITY) val precipitationProbability: List<Float>
) {
    @Serializable
    data class Response(
        @SerialName(value = ApiGeneralParameters.HOURLY) val body: HourlyWeather
    )
}