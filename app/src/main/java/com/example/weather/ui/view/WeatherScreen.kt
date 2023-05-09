package com.example.weather.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weather.ui.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    val weatherState = viewModel.weatherState
}