package com.example.weather.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weather.WeatherApplication
import com.example.weather.data.repositories.WeatherRepository
import com.example.weather.utils.ApiDailyWeatherParameters
import com.example.weather.utils.ApiHourlyWeatherParameters
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    init {
        getWeather()
    }

    private fun getWeather() {
        viewModelScope.launch {
            getCurrentWeather(52.52F, 13.41F)
            getHourlyWeather(52.52F, 13.41F)
            getDailyForecast(52.52F, 13.41F)
        }
    }

    private suspend fun getCurrentWeather(lat: Float, lng: Float) {
        weatherRepository.getCurrentWeather(lat, lng)
    }

    private suspend fun getHourlyWeather(lat: Float, lng: Float) {
        weatherRepository.getHourlyWeather(
            lat = lat,
            lng = lng,
            hourly = listOf(
                ApiHourlyWeatherParameters.TEMPERATURE,
                ApiHourlyWeatherParameters.WEATHER_CODE,
                ApiHourlyWeatherParameters.IS_DAY,
                ApiHourlyWeatherParameters.PRECIPITATION,
                ApiHourlyWeatherParameters.PRECIPITATION_PROBABILITY
            )
        )
    }

    private suspend fun getDailyForecast(lat: Float, lng: Float) {
        weatherRepository.getDailyForecast(
            lat = lat,
            lng = lng,
            daily = listOf(
                ApiDailyWeatherParameters.TEMPERATURE_MAX,
                ApiDailyWeatherParameters.TEMPERATURE_MIN,
                ApiDailyWeatherParameters.WEATHER_CODE,
                ApiDailyWeatherParameters.PRECIPITATION,
                ApiDailyWeatherParameters.PRECIPITATION_HOURS
            )
        )
    }

    private suspend fun getCurrentLocation() {

    }

    /**
     * Factory for [WeatherViewModel] that takes [WeatherRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WeatherApplication)
                val weatherRepository = application.weatherContainer.weatherRepository
                WeatherViewModel(weatherRepository)
            }
        }
    }
}