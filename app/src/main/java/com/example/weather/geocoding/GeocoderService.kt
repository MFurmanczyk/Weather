package com.example.weather.geocoding

import android.content.Context
import android.location.Address
import android.location.Geocoder

interface GeocoderService {
    suspend fun getFromLocation(latitude: Double, longitude: Double, maxResults: Int): MutableList<Address>?
}

@Suppress("DEPRECATION") //for backward compatibility
class AndroidGeocoderService(context: Context) : GeocoderService {

    private val geocoder = Geocoder(context)

    override suspend fun getFromLocation(
        latitude: Double,
        longitude: Double,
        maxResults: Int
    ): MutableList<Address>? {
        return geocoder.getFromLocation(latitude, longitude, maxResults)
    }
}