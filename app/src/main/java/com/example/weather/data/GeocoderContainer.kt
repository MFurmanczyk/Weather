package com.example.weather.data

import android.content.Context
import com.example.weather.data.repositories.AndroidGeocoderRepository
import com.example.weather.data.repositories.GeocoderRepository
import com.example.weather.geocoding.AndroidGeocoderService

interface GeocoderContainer {
    val geocoderRepository: GeocoderRepository
}

class AndroidGeocoderContainer(context: Context) : GeocoderContainer {

    private val geocoderService = AndroidGeocoderService(context)
    override val geocoderRepository = AndroidGeocoderRepository(geocoderService)

}