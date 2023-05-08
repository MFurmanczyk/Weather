package com.example.weather.data.repositories

import com.example.weather.data.model.WeatherResponse
import com.example.weather.network.WeatherApiService

/**
 * Repository that fetch weather forecast from provided source.
 */
interface WeatherRepository {

    suspend fun getCurrentWeather(
        lat: Float,
        lng: Float
    ): WeatherResponse

    suspend fun getHourlyWeather(
        lat: Float,
        lng: Float,
        hourly: List<String>
    ) : WeatherResponse

    suspend fun getDailyForecast(
        lat: Float,
        lng: Float,
        daily: List<String>
    ) : WeatherResponse
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
    ): WeatherResponse =
        weatherApiService.getCurrentWeather(
            lat = lat,
            lng = lng
        )

    /**
     * Fetches hourly weather forecast for given coordinates: [lat], [lng] and options: [hourly].
     * To populate [hourly] list use constants defined in [com.example.weather.utils.ApiHourlyWeatherParameters]
     */
    override suspend fun getHourlyWeather(
        lat: Float,
        lng: Float,
        hourly: List<String>
    ): WeatherResponse =
        weatherApiService.getHourlyWeather(
            lat = lat,
            lng = lng,
            hourly = hourly
        )

    /**
     * Fetches daily weather forecast for given coordinates: [lat], [lng] and options: [daily].
     * To populate [daily] list use constants defined in [com.example.weather.utils.ApiDailyWeatherParameters]
     */
    override suspend fun getDailyForecast(
        lat: Float,
        lng: Float,
        daily: List<String>
    ): WeatherResponse =
        weatherApiService.getDailyForecast(
            lat = lat,
            lng = lng,
            daily = daily
        )

}