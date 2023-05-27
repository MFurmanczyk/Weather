package com.example.weather.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.ui.view.LocationRationale
import com.example.weather.ui.view.WeatherScreen
import com.example.weather.ui.viewmodel.WeatherViewModel
import com.example.weather.utils.isPermanentlyDenied
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherApp(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) {
       Surface(
           modifier = Modifier
               .fillMaxSize()
               .padding(it),
           color = MaterialTheme.colorScheme.background
       ) {

           val permissionsState = rememberMultiplePermissionsState(
               permissions = listOf(
                   Manifest.permission.ACCESS_FINE_LOCATION,
                   Manifest.permission.ACCESS_COARSE_LOCATION

               )
           )

           val lifecycleOwner = LocalLifecycleOwner.current
           DisposableEffect(
               key1 = lifecycleOwner,
               effect = {
                   val observer = LifecycleEventObserver { _, event ->
                       if (event == Lifecycle.Event.ON_START) {
                           permissionsState.launchMultiplePermissionRequest()
                       }
                   }
                   lifecycleOwner.lifecycle.addObserver(observer)

                   onDispose {
                       lifecycleOwner.lifecycle.removeObserver(observer)
                   }
               }
           )

           permissionsState.permissions.forEach { permissionState ->
               when (permissionState.permission) {
                   Manifest.permission.ACCESS_FINE_LOCATION -> {
                       when {
                           //Permission is granted. App proceeds.
                           permissionState.status.isGranted -> {
                               val weatherViewModel: WeatherViewModel =
                                   viewModel(factory = WeatherViewModel.Factory)
                               WeatherScreen(viewModel = weatherViewModel)
                           }

                           //Permission dialog can be triggered by clicking "Grant" dialog button.
                           permissionState.status.shouldShowRationale -> {
                               val activity = LocalContext.current as? Activity
                               LocationRationale(
                                   title = "Permission required",
                                   text = "Precise location access is required to get accurate weather.",
                                   onConfirm = {
                                       permissionState.launchPermissionRequest()
                                   },
                                   onDismiss = {
                                       activity?.finish()
                                   }
                               )
                           }

                           //If permission is permanently denied the only option to grant it is through
                           //the app settings. User can navigate to settings by clicking the according
                           //dialog button.
                           permissionState.status.isPermanentlyDenied() -> {
                               val activity = LocalContext.current as? Activity
                               LocationRationale(
                                   title = "Permission required",
                                   text = "Precise location access is required to get accurate weather. " +
                                           "You can grant it in app settings.",
                                   onConfirm = {
                                       val intent =
                                           Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                       val uri =
                                           Uri.fromParts("package", activity?.packageName, null)
                                       intent.data = uri
                                       activity?.startActivity(intent)
                                   },
                                   onDismiss = {
                                       activity?.finish()
                                   }
                               )
                           }
                       }
                   }
               }
           }
       }
    }
}