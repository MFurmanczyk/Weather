package com.example.weather.network

import com.example.weather.data.model.WeatherResponse
import com.example.weather.utils.ApiGeneralParameters
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A public interface that exposes the:
 *
 * [getCurrentWeather]
 *
 * [getHourlyWeather]
 *
 * [getDailyForecast]
 *
 * methods.
 */
interface WeatherApiService {

    /**
     * Returns [WeatherResponse] with [WeatherResponse.currentWeather] field populated.
     * Fields [WeatherResponse.hourlyWeather] and [WeatherResponse.dailyForecast] are null.
     */
    @GET("forecast")
    suspend fun getCurrentWeather(
        @Query("latitude")lat: Float,
        @Query("longitude")lng: Float,
        @Query("current_weather")currentWeather: Boolean = true,
        @Query("timeformat")timeFormat: String = ApiGeneralParameters.UNIX_TIME,
        @Query("timezone")timeZone: String = ApiGeneralParameters.AUTO
    ): WeatherResponse

    /**
     * Returns [WeatherResponse] with [WeatherResponse.hourlyWeather] field populated.
     * Fields [WeatherResponse.currentWeather] and [WeatherResponse.dailyForecast] are null.
     * Method requires provision of [hourly] list with parameters from [com.example.weather.utils.ApiHourlyWeatherParameters].
     */
    @GET("forecast")
    suspend fun getHourlyWeather(
        @Query("latitude")lat: Float,
        @Query("longitude")lng: Float,
        @Query("hourly")hourly: List<String>,
        @Query("forecast_days") forecastDays: Int = 1,
        @Query("timeformat")timeFormat: String = ApiGeneralParameters.UNIX_TIME,
        @Query("timezone")timeZone: String = ApiGeneralParameters.AUTO
    ) : WeatherResponse

    /**
     * Returns [WeatherResponse] with [WeatherResponse.dailyForecast] field populated.
     * Fields [WeatherResponse.hourlyWeather] and [WeatherResponse.currentWeather] are null.
     * Method requires provision of [daily] list with parameters from [com.example.weather.utils.ApiDailyWeatherParameters].
     */
    @GET("forecast")
    suspend fun getDailyForecast(
        @Query("latitude")lat: Float,
        @Query("longitude")lng: Float,
        @Query("daily")daily: List<String>,
        @Query("forecast_days") forecastDays: Int = 7,
        @Query("timeformat")timeFormat: String = ApiGeneralParameters.UNIX_TIME,
        @Query("timezone")timeZone: String = ApiGeneralParameters.AUTO
    ) : WeatherResponse

}