package com.example.weather.ui.view

import android.location.Address
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weather.R
import com.example.weather.data.model.CurrentWeather
import com.example.weather.ui.viewmodel.WeatherState
import com.example.weather.ui.viewmodel.WeatherViewModel
import com.example.weather.utils.WeatherImages
import com.example.weather.utils.getTime
import java.util.Locale
import kotlin.math.roundToInt

/**
 *
 */
@ExperimentalMaterialApi
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {

        val weatherState: WeatherState by viewModel.weatherState.collectAsState()

        val refreshState = rememberPullRefreshState(
            refreshing = weatherState is WeatherState.Loading,
            onRefresh = viewModel::refresh
        )

        Box(
            modifier = modifier.pullRefresh(refreshState)
        ) {


            val isSuccess = weatherState is WeatherState.Success

            AnimatedVisibility(
                visible = isSuccess,
                enter = fadeIn(animationSpec = tween(durationMillis = 250)),
                exit = fadeOut(animationSpec = tween(durationMillis = 250))
            ) {

                if (isSuccess) {
                    val success = weatherState as WeatherState.Success

                    WeatherScreenContainer(
                        currentWeather = {
                            CurrentWeather(
                                address = success.address,
                                currentWeather = success.currentWeather
                            )
                        }
                    )
                }
            }
            AnimatedVisibility(visible = weatherState is WeatherState.Error) {
                TODO("Error screen")
            }


            PullRefreshIndicator(
                refreshing = weatherState is WeatherState.Loading,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun WeatherScreenContainer(
    currentWeather: @Composable ()-> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            currentWeather()
        }
    }
}

@Composable
fun CurrentWeather(
    address: Address,
    currentWeather: CurrentWeather,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .heightIn(min = 300.dp)
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.TwoTone.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "${address.locality}, ${address.countryCode}"
                )
            }

            Divider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        WeatherImages.getWeatherImage(
                            weatherCode = currentWeather.weatherCode,
                            isDay = currentWeather.isDay == 1
                        )
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = "Weather image",
                modifier = Modifier
                    .size(250.dp)
                    .padding(all = 24.dp)
            )

            Text(
                text = getTime(currentWeather.time),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = currentWeather.temperature.roundToInt().toString(),
                    fontSize = 64.sp
                )
                Text(
                    text = stringResource(R.string.degrees_c),
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
@Preview
fun CurrentWeatherPreview() {
    MaterialTheme {
        CurrentWeather(
            address = Address(Locale.getDefault()),
            currentWeather = CurrentWeather(
            0F,
            0F,
            0F,
            0,
            0,
            0L
        ))
    }
}