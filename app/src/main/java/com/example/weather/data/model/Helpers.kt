package com.example.weather.data.model

data class HourlyWeatherUnit (
    val time: Long,
    val temperature: Float,
    val apparentTemperature: Float,
    val humidity: Float,
    val pressureSurface: Float,
    val weatherCode: Int,
    val isDay: Int,
    val precipitation: Float,
    val precipitationProbability: Float
)

data class DailyWeatherUnit(
    val time: Long,
    val temperatureMax: Float,
    val temperatureMin: Float,
    val precipitation: Float,
    val precipitationHours: Float,
    val sunrise: Long,
    val sunset: Long,
    val weatherCode: Int
)

fun HourlyWeather.toObjectArray(): List<HourlyWeatherUnit> {

    val objectArray = mutableListOf<HourlyWeatherUnit>()

    for(i in time.indices) {
        val newObject = HourlyWeatherUnit(
            time = time[i],
            temperature = temperature[i],
            apparentTemperature = apparentTemperature[i],
            humidity = humidity[i],
            pressureSurface = pressureSurface[i],
            weatherCode = weatherCode[i],
            isDay = isDay[i],
            precipitation = precipitation[i],
            precipitationProbability = precipitationProbability[i]
        )
        objectArray.add(newObject)
    }

    return objectArray.toList()
}

fun DailyWeather.toObjectArray(): List<DailyWeatherUnit> {

    val objectArray = mutableListOf<DailyWeatherUnit>()

    //[i] starts from 1 because 0 is current day
    for(i in 1 until time.size) {
        val newObject = DailyWeatherUnit(
            time = time[i],
            temperatureMax = temperatureMax[i],
            temperatureMin = temperatureMin[i],
            precipitation = precipitation[i],
            precipitationHours = precipitationHours[i],
            sunrise = sunrise[i],
            sunset = sunset[i],
            weatherCode = weatherCode[i],
        )
        objectArray.add(newObject)
    }
    return objectArray.toList()
}