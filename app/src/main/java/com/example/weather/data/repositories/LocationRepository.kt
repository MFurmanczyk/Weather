package com.example.weather.data.repositories

import android.location.Location
import com.example.weather.location.LocationService

interface LocationRepository {
    suspend fun getCurrentLocation(): Location?

    suspend fun getLastLocation(): Location?
}

class FusedLocationRepository(
    private val locationService: LocationService
) : LocationRepository{

    override suspend fun getCurrentLocation(): Location? {
        return locationService.getCurrentLocation()
    }

    override suspend fun getLastLocation(): Location? {
        return locationService.getLastLocation()
    }

}