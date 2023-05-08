package com.example.weather

import android.app.Application
import com.example.weather.data.OpenMeteoWeatherContainer
import com.example.weather.data.WeatherContainer

class WeatherApplication : Application() {

    lateinit var weatherContainer: WeatherContainer

    override fun onCreate() {
        super.onCreate()
        weatherContainer = OpenMeteoWeatherContainer()
    }
}