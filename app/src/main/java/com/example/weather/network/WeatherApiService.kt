package com.example.weather.network

import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.model.DailyWeather
import com.example.weather.data.model.HourlyWeather
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
 * [getDailyWeather]
 *
 * methods.
 */
interface WeatherApiService {

    /**
     * Returns [CurrentWeather] object with structure corresponding to API documentation.
     * @see <a href="https://open-meteo.com/en/docs"> API docs</a>
     */
    @GET("forecast")
    suspend fun getCurrentWeather(
        @Query("latitude")lat: Float,
        @Query("longitude")lng: Float,
        @Query("current_weather")currentWeather: Boolean = true,
        @Query("timeformat")timeFormat: String = ApiGeneralParameters.UNIX_TIME,
        @Query("timezone")timeZone: String = ApiGeneralParameters.AUTO
    ): CurrentWeather.Response

    /**
     * Returns [HourlyWeather] object with structure corresponding to API documentation.
     * Method requires provision of [hourly] list with parameters from [com.example.weather.utils.ApiHourlyWeatherParameters].
     * @see <a href="https://open-meteo.com/en/docs"> API docs</a>
     */
    @GET("forecast")
    suspend fun getHourlyWeather(
        @Query("latitude")lat: Float,
        @Query("longitude")lng: Float,
        @Query("hourly")hourly: List<String>,
        @Query("forecast_days") forecastDays: Int = 1,
        @Query("timeformat")timeFormat: String = ApiGeneralParameters.UNIX_TIME,
        @Query("timezone")timeZone: String = ApiGeneralParameters.AUTO
    ) : HourlyWeather.Response

    /**
     * Returns [DailyWeather] object with structure corresponding to API documentation.
     * Method requires provision of [daily] list with parameters from [com.example.weather.utils.ApiDailyWeatherParameters].
     * @see <a href="https://open-meteo.com/en/docs"> API docs</a>
     */
    @GET("forecast")
    suspend fun getDailyWeather(
        @Query("latitude")lat: Float,
        @Query("longitude")lng: Float,
        @Query("daily")daily: List<String>,
        @Query("forecast_days") forecastDays: Int = 7,
        @Query("timeformat")timeFormat: String = ApiGeneralParameters.UNIX_TIME,
        @Query("timezone")timeZone: String = ApiGeneralParameters.AUTO
    ) : DailyWeather.Response

}