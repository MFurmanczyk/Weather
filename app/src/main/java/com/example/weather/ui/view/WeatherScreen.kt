package com.example.weather.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.weather.data.model.toObjectArray
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.ui.viewmodel.WeatherState
import com.example.weather.ui.viewmodel.WeatherViewModel
import com.example.weather.utils.WeatherImages
import com.example.weather.utils.getTime
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
                            CurrentWeatherPanel(
                                address = "${success.address.locality}, ${success.address.countryCode}",
                                weatherCode = success.currentWeather.weatherCode,
                                isDay = success.currentWeather.isDay == 1,
                                time = getTime(success.currentWeather.time),
                                temperature = success.currentWeather.temperature.roundToInt()
                            )
                        },
                        hourlyWeather = {
                            val hourlyList = success.hourlyWeather.toObjectArray()

                            WeatherSection(title = stringResource(R.string.today)) {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
                                ) {
                                    items(hourlyList) {hourlyUnit ->
                                        SmallWeatherCard(
                                            time =
                                            if (success.currentWeather.time == hourlyUnit.time) stringResource(
                                                id = R.string.now
                                            )
                                            else getTime(hourlyUnit.time),
                                            weatherCode = hourlyUnit.weatherCode ?: 0,
                                            isDay = hourlyUnit.isDay == 1,
                                            temperature = hourlyUnit.temperature?.roundToInt() ?: 0
                                        )
                                    }
                                }
                            }
                        },
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
    modifier: Modifier = Modifier,
    currentWeather: @Composable () -> Unit = {},
    currentHighlights: @Composable () -> Unit = {},
    hourlyWeather: @Composable () -> Unit = {},
    dailyWeather: @Composable () -> Unit = {}
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
            currentHighlights()
            hourlyWeather()
            dailyWeather()
        }
    }
}

@Composable
fun CurrentWeatherPanel(
    address: String,
    weatherCode: Int,
    isDay: Boolean,
    time: String,
    temperature: Int,
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
                    text = address
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
                            weatherCode = weatherCode,
                            isDay = isDay
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
                text = time,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = temperature.toString(),
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
fun WeatherSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
        content()
    }
}

@Composable
fun SmallWeatherCard(
    time: String,
    weatherCode: Int,
    isDay: Boolean,
    temperature: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .sizeIn(minWidth = 70.dp, minHeight = 100.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(all = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        WeatherImages.getWeatherImage(
                            weatherCode = weatherCode,
                            isDay = isDay
                        )
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = "Weather image",
                modifier = Modifier
                    .size(50.dp)
                    .padding(all = 4.dp)
            )
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = temperature.toString(),
                    fontSize = 12.sp
                )
                Text(
                    text = stringResource(R.string.degrees_c),
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
@Preview
fun CurrentWeatherPreview() {
    WeatherTheme {
        CurrentWeatherPanel(
            address = "Gliwice, PL",
            weatherCode = 1,
            isDay = true,
            time = "9:37 PM",
            temperature = 10
        )
    }
}

@Composable
@Preview
fun WeatherSectionPreview() {
    WeatherTheme {
        WeatherSection(
            title = "Title"
        ) {

        }
    }
}

@Composable
@Preview
fun SmallWeatherCardPreview() {
    WeatherTheme{
        SmallWeatherCard(
            time = "Now",
            weatherCode = 1,
            isDay = true,
            temperature = 10
        )
    }
}