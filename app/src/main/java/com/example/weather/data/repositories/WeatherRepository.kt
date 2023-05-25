package com.example.weather.data.repositories

import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.model.DailyWeather
import com.example.weather.data.model.HourlyWeather
import com.example.weather.network.WeatherApiService

/**
 * Repository that fetch weather forecast from provided source.
 */
interface WeatherRepository {

    suspend fun getCurrentWeather(
        lat: Float,
        lng: Float
    ): CurrentWeather

    suspend fun getHourlyWeather(
        lat: Float,
        lng: Float,
        hourly: List<String>
    ) : HourlyWeather

    suspend fun getDailyForecast(
        lat: Float,
        lng: Float,
        daily: List<String>
    ) : DailyWeather
}

/**
 * Network Implementation of [WeatherRepository] that fetches forecast from weatherApi.
 */
class NetworkWeatherRepository (
    private val weatherApiService: WeatherApiService
) : WeatherRepository {

    /**
     * Fetches current weather for given coordinates: [lat], [lng].
     */
    override suspend fun getCurrentWeather(
        lat: Float,
        lng: Float
    ): CurrentWeather =
        weatherApiService.getCurrentWeather(
            lat = lat,
            lng = lng
        ).body

    /**
     * Fetches hourly weather forecast during present day for given coordinates: [lat], [lng] and options: [hourly].
     * To populate [hourly] list use constants defined in [com.example.weather.utils.ApiHourlyWeatherParameters]
     */
    override suspend fun getHourlyWeather(
        lat: Float,
        lng: Float,
        hourly: List<String>
    ): HourlyWeather =
        weatherApiService.getHourlyWeather(
            lat = lat,
            lng = lng,
            hourly = hourly
        ).body

    /**
     * Fetches daily weather forecast for given coordinates: [lat], [lng] and options: [daily].
     * To populate [daily] list use constants defined in [com.example.weather.utils.ApiDailyWeatherParameters]
     */
    override suspend fun getDailyForecast(
        lat: Float,
        lng: Float,
        daily: List<String>
    ): DailyWeather =
        weatherApiService.getDailyWeather(
            lat = lat,
            lng = lng,
            daily = daily
        ).body
}