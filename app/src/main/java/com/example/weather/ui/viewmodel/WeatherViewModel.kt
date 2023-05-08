package com.example.weather.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weather.WeatherApplication
import com.example.weather.data.repositories.WeatherRepository
import kotlinx.coroutines.launch


class WeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    fun getWeather() {
        viewModelScope.launch {

        }
    }

    private suspend fun getCurrentWeather(lat: Float, lng: Float) {
        weatherRepository.getCurrentWeather(lat, lng)
    }

    private fun getHourlyWeather(lat: Float, lng: Float) {

    }

    private fun getDailyForecast(lat: Float, lng: Float) {

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