package com.example.weather.ui.viewmodel

import android.location.Address
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.weather.WeatherApplication
import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.model.DailyWeather
import com.example.weather.data.model.HourlyWeather
import com.example.weather.data.repositories.GeocoderRepository
import com.example.weather.data.repositories.LocationRepository
import com.example.weather.data.repositories.WeatherRepository
import com.example.weather.utils.ApiDailyWeatherParameters
import com.example.weather.utils.ApiHourlyWeatherParameters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface WeatherState {
    data class Success(
        val address: Address,
        val currentWeather: CurrentWeather,
        val hourlyWeather: HourlyWeather,
        val dailyWeather: DailyWeather
        ) : WeatherState
    interface Error : WeatherState {
        fun getMessage(): String
    }
    data class ExceptionError(private val e: Exception) : Error {
        override fun getMessage(): String {
            return e.message.toString()
        }
    }
    data class MessageError(private val msg: String) : Error {
        override fun getMessage(): String {
            return msg
        }
    }
    object Loading : WeatherState
}

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
    private val geocoderRepository: GeocoderRepository
) : ViewModel() {

    private val _weatherState:MutableStateFlow<WeatherState> = MutableStateFlow(WeatherState.Loading)
    var weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val _selectedTimestamp: MutableStateFlow<Long> = MutableStateFlow(0)
    var selectedTimestamp = _selectedTimestamp.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _weatherState.update {
            WeatherState.Loading
        }
        getCurrentLocation { location ->
            getWeather(location)
        }

    }

    fun updateTimestamp(timestamp: Long) {
        _selectedTimestamp.update {
            timestamp
        }
    }

    private fun getWeather(location: Location?) {
        viewModelScope.launch {

            if(location != null) {
                _weatherState.update {
                    try {
                        val lat = location.latitude.toFloat()
                        val lng = location.longitude.toFloat()
                        WeatherState.Success(
                            getAddressForLocation(location),
                            getCurrentWeather(lat, lng),
                            getHourlyWeather(lat, lng),
                            getDailyForecast(lat, lng)
                        )
                    } catch (e: IOException) {
                        WeatherState.ExceptionError(e)
                    } catch (e: HttpException) {
                        WeatherState.ExceptionError(e)
                    }
                }
                _selectedTimestamp.update {
                    (weatherState.value as? WeatherState.Success)?.currentWeather?.time ?: 0L
                }
            } else {
                _weatherState.update {
                    WeatherState.MessageError(msg = "Unable to define device location.")
                }
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
                ApiHourlyWeatherParameters.PRECIPITATION_PROBABILITY,
                ApiHourlyWeatherParameters.HUMIDITY,
                ApiHourlyWeatherParameters.APPARENT_TEMPERATURE,
                ApiHourlyWeatherParameters.PRESSURE_SURFACE
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
                ApiDailyWeatherParameters.PRECIPITATION_HOURS,
                ApiDailyWeatherParameters.SUNRISE,
                ApiDailyWeatherParameters.SUNSET
            )
        )
    }

    private fun getCurrentLocation(
        onLocationReceived: (Location?) -> Unit
    ) {
        viewModelScope.launch {
            val currentLocation: Location? = locationRepository.getCurrentLocation()
            if (currentLocation != null) {
                onLocationReceived(currentLocation)
            } else {
                val lastLocation: Location? = locationRepository.getLastLocation()
                onLocationReceived(lastLocation)
            }
        }
    }

    private suspend fun getAddressForLocation(location: Location): Address {
        return geocoderRepository.getFromLocation(location.latitude, location.longitude)
    }


    companion object {
        /**
         * Factory for [WeatherViewModel] that takes [WeatherRepository] as a dependency
        */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WeatherApplication)
                val weatherRepository = application.weatherContainer.weatherRepository
                val locationRepository = application.locationContainer.locationRepository
                val geocoderRepository = application.geocoderContainer.geocoderRepository
                WeatherViewModel(weatherRepository, locationRepository, geocoderRepository)
            }
        }
    }
}