package com.example.weather.data

import android.content.Context
import com.example.weather.data.repositories.FusedLocationRepository
import com.example.weather.data.repositories.LocationRepository
import com.example.weather.location.FusedLocationService

interface LocationContainer {
    val locationRepository: LocationRepository
}

class FusedLocationContainer(context: Context): LocationContainer {

    private val locationService = FusedLocationService(context)

    override val locationRepository: LocationRepository = FusedLocationRepository(locationService)
}