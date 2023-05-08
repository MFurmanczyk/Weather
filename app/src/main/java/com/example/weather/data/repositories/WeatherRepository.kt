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



class NetworkWeatherRepository (
    private val weatherApiService: WeatherApiService
) : WeatherRepository {

    override suspend fun getCurrentWeather(
        lat: Float,
        lng: Float
    ): WeatherResponse =
        weatherApiService.getCurrentWeather(
            lat = lat,
            lng = lng
        )

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