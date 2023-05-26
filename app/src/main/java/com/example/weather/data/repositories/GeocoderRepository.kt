package com.example.weather.data.repositories

import android.location.Address
import com.example.weather.geocoding.GeocoderService
import java.util.Locale

interface GeocoderRepository {
    suspend fun getFromLocation(latitude: Double, longitude: Double): Address
}

class AndroidGeocoderRepository(
    private val geocoderService: GeocoderService
) :GeocoderRepository {

    override suspend fun getFromLocation(latitude: Double, longitude: Double): Address {
        val address = geocoderService.getFromLocation(latitude = latitude, longitude = longitude, maxResults = 1)
        return address?.get(0) ?: Address(Locale.getDefault())
    }

}