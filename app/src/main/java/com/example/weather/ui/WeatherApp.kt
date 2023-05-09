package com.example.weather.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.ui.view.WeatherScreen
import com.example.weather.ui.viewmodel.WeatherViewModel


@Composable
fun WeatherApp(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) {
       Surface(
           modifier = Modifier
               .fillMaxSize()
               .padding(it),
           color = MaterialTheme.colors.background
       ) {
           val weatherViewModel: WeatherViewModel = viewModel(factory = WeatherViewModel.Factory)
           WeatherScreen(viewModel = weatherViewModel)
       }
    }
}