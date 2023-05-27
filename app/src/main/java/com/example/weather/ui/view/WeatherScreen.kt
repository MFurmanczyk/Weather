package com.example.weather.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.weather.ui.viewmodel.WeatherState
import com.example.weather.ui.viewmodel.WeatherViewModel

/**
 *
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    val weatherState: WeatherState by viewModel.weatherState.collectAsState()

    val refreshState = rememberPullRefreshState(
        refreshing = weatherState is WeatherState.Loading,
        onRefresh = viewModel::refresh
    )

    Box(modifier.pullRefresh(refreshState)) {
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

        }
        PullRefreshIndicator(weatherState is WeatherState.Loading, refreshState, Modifier.align(Alignment.TopCenter))
    }
}
