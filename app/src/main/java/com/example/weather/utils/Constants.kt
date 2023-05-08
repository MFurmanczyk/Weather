package com.example.weather.utils

/**
 * Variables corresponding to Weather Forecast API documentation for general configuration.
 * @see <a href="https://open-meteo.com/en/docs#:~:text=Every%206%20hours-,API%20Documentation,-The%20API%20endpoint">General Parameters Definition</a>
 */
object ApiGeneralParameters {
    const val UNIX_TIME = "unixtime"
    const val AUTO = "auto"
    const val CURRENT_WEATHER = "current_weather"
    const val DAILY = "daily"
    const val HOURLY = "hourly"
}

/**
 * Variables corresponding to Weather Forecast API documentation for hourly forecast.
 * @see <a href="https://open-meteo.com/en/docs#:~:text=Hourly%20Parameter%20Definition">Hourly Parameters Definition</a>
 */
object ApiHourlyWeatherParameters {
    const val TEMPERATURE = "temperature_2m"
    const val APPARENT_TEMPERATURE = "apparent_temperature"
    const val HUMIDITY = "relativehumidity_2m"
    const val PRECIPITATION = "precipitation"
    const val WEATHER_CODE = "weathercode"
    const val IS_DAY = "is_day"
    const val VISIBILITY = "visibility"
    const val PRESSURE_MSL = "pressure_msl"
    const val PRESSURE_SURFACE = "surface_pressure"
}

/**
 * Variables corresponding to Weather Forecast API documentation for daily forecast.
 * @see <a href="https://open-meteo.com/en/docs#:~:text=Daily%20Parameter%20Definition">Daily Parameters Definition</a>
 */
object ApiDailyWeatherParameters {
    const val TEMPERATURE_MAX = "temperature_2m_max"
    const val TEMPERATURE_MIN = "temperature_2m_min"
}