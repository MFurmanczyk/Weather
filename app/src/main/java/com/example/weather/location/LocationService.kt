package com.example.weather.location

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Interface provides methods that every location service must implement.
 */
interface LocationService {
    suspend fun getCurrentLocation(): Location?
}

class FusedLocationService(private val context: Context) : LocationService {

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCurrentLocation(): Location? {

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        val isGpsEnabled = locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled && !(hasAccessCoarseLocationPermission)) {
            return null
        }

        val currentLocation = fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)

        return suspendCancellableCoroutine { cont ->
            currentLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(result) {} // Resume coroutine with location result
                    } else {
                        cont.resume(null) {} // Resume coroutine with null location result
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it) {}  // Resume coroutine with location result
                }
                addOnFailureListener {
                    cont.resume(null) {} // Resume coroutine with null location result
                }
                addOnCanceledListener {
                    cont.cancel() // Cancel the coroutine
                }
            }
        }
    }
}