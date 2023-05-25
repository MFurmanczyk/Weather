package com.example.weather

import android.app.Application
import com.example.weather.data.FusedLocationContainer
import com.example.weather.data.LocationContainer
import com.example.weather.data.OpenMeteoWeatherContainer
import com.example.weather.data.WeatherContainer

class WeatherApplication : Application() {

    lateinit var weatherContainer: WeatherContainer
    lateinit var locationContainer: LocationContainer

    override fun onCreate() {
        super.onCreate()
        weatherContainer = OpenMeteoWeatherContainer()
        locationContainer = FusedLocationContainer(this)
    }
}