package com.example.weather.data

import com.example.weather.data.repositories.NetworkWeatherRepository
import com.example.weather.data.repositories.WeatherRepository
import com.example.weather.network.WeatherApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface WeatherContainer {
    val weatherRepository: WeatherRepository
}

class OpenMeteoWeatherContainer : WeatherContainer {

    private val BASE_URL = "https://api.open-meteo.com/v1/"

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
    override val weatherRepository: WeatherRepository by lazy {
        NetworkWeatherRepository(retrofitService)
    }
}