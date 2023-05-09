package com.example.weather.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weather.WeatherApplication
import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.model.DailyWeather
import com.example.weather.data.model.HourlyWeather
import com.example.weather.data.repositories.WeatherRepository
import com.example.weather.utils.ApiDailyWeatherParameters
import com.example.weather.utils.ApiHourlyWeatherParameters
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface WeatherState {
    data class Success(
        val currentWeather: CurrentWeather,
        val hourlyWeather: HourlyWeather,
        val dailyForecast: DailyWeather
        ) : WeatherState
    data class Error(val exception: Exception) : WeatherState
    object Loading : WeatherState
}

class WeatherViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    var weatherState: WeatherState by mutableStateOf(WeatherState.Loading)
        private set

    init {
        getWeather()
    }

    private fun getWeather() {
        viewModelScope.launch {
            weatherState = WeatherState.Loading
            weatherState = try {
                WeatherState.Success(
                    getCurrentWeather(52.52F, 13.41F),
                    getHourlyWeather(52.52F, 13.41F),
                    getDailyForecast(52.52F, 13.41F)
                )
            } catch (e: IOException) {
                WeatherState.Error(e)
            } catch (e: HttpException) {
                WeatherState.Error(e)
            }
        }
    }

    private suspend fun getCurrentWeather(lat: Float, lng: Float): CurrentWeather {
        return weatherRepository.getCurrentWeather(lat, lng)
    }

    private suspend fun getHourlyWeather(lat: Float, lng: Float): HourlyWeather {
        return weatherRepository.getHourlyWeather(
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

    private suspend fun getDailyForecast(lat: Float, lng: Float): DailyWeather {
        return weatherRepository.getDailyForecast(
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