package com.example.weather.data.model

import com.example.weather.utils.ApiDailyWeatherParameters
import com.example.weather.utils.ApiGeneralParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class HourlyWeatherUnit (
    val time: Long? = null,
    val temperature: Float? = null,
    val apparentTemperature: Float? = null,
    val humidity: Float? = null,
    val visibility: Float? = null,
    val pressureMsl: Float? = null,
    val pressureSurface: Float? = null,
    val weatherCode: Int? = null,
    val isDay: Int? = null,
    val precipitation: Float? = null,
    val precipitationProbability: Float? = null
)

data class DailyWeatherUnit(
    val time: Long? = null,
    val temperatureMax: Float? = null,
    val temperatureMin: Float? = null,
    val precipitation: Float? = null,
    val precipitationHours: Float? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val weatherCode: Int? = null
)

fun HourlyWeather.toObjectArray(): List<HourlyWeatherUnit> {

    val objectArray = mutableListOf<HourlyWeatherUnit>()

    for(i in 0 until (time?.size ?: 0)) {
        val newObject = HourlyWeatherUnit(
            time = time?.get(i),
            temperature = temperature?.get(i),
            apparentTemperature = apparentTemperature?.get(i),
            humidity = humidity?.get(i),
            visibility = visibility?.get(i),
            pressureMsl = pressureMsl?.get(i),
            pressureSurface = pressureSurface?.get(i),
            weatherCode = weatherCode?.get(i),
            isDay = isDay?.get(i),
            precipitation = precipitation?.get(i),
            precipitationProbability = precipitationProbability?.get(i)
        )
        objectArray.add(newObject)
    }

    return objectArray.toList()
}

fun DailyWeather.toObjectArray(): List<DailyWeatherUnit> {

    val objectArray = mutableListOf<DailyWeatherUnit>()

    //[i] starts from 1 because 0 is current day
    for(i in 1 until (time?.size ?: 1)) {
        val newObject = DailyWeatherUnit(
            time = time?.get(i),
            temperatureMax = temperatureMax?.get(i),
            temperatureMin = temperatureMin?.get(i),
            precipitation = precipitation?.get(i),
            precipitationHours = precipitationHours?.get(i),
            sunrise = sunrise?.get(i),
            sunset = sunset?.get(i),
            weatherCode = weatherCode?.get(i),
        )
        objectArray.add(newObject)
    }

    return objectArray.toList()
}