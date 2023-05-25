package com.example.weather.ui

import android.Manifest
import android.app.Activity
import android.util.Log
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
import com.google.accompanist.permissions.rememberPermissionState
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
           val permissionState = rememberPermissionState(
               permission = Manifest.permission.ACCESS_COARSE_LOCATION
           )

           val lifecycleOwner = LocalLifecycleOwner.current
           DisposableEffect(
               key1 = lifecycleOwner,
               effect = {
                   val observer = LifecycleEventObserver { _, event ->
                       if(event == Lifecycle.Event.ON_START) {
                           permissionState.launchPermissionRequest()
                       }
                   }
                   lifecycleOwner.lifecycle.addObserver(observer)

                   onDispose {
                       lifecycleOwner.lifecycle.removeObserver(observer)
                   }
               }
           )

           when {
               permissionState.status.isGranted -> {
                   Log.d("BUG", "Granted.")
                   val weatherViewModel: WeatherViewModel =
                       viewModel(factory = WeatherViewModel.Factory)
                   WeatherScreen(viewModel = weatherViewModel)
               }

               permissionState.status.shouldShowRationale -> {
                   Log.d("BUG", "Rationale.")
                   val activity = LocalContext.current as? Activity
                   LocationRationale(
                       title = "Permission required",
                       text = "Location access is required to get accurate weather.",
                       onConfirm = {
                           permissionState.launchPermissionRequest()
                       },
                       onDismiss = {
                           activity?.finish()
                       }
                   )
               }

               permissionState.status.isPermanentlyDenied() -> {
                   val activity = LocalContext.current as? Activity
                   LocationRationale(
                       title = "Permission required",
                       text = "Location access is required to get accurate weather. " +
                               "You can grant it in app settings.",
                       onConfirm = {

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