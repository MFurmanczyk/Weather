package com.example.weather.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.ui.view.WeatherScreen
import com.example.weather.ui.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
           val weatherViewModel: WeatherViewModel = viewModel(factory = WeatherViewModel.Factory)
           WeatherScreen(viewModel = weatherViewModel)
       }
    }
}