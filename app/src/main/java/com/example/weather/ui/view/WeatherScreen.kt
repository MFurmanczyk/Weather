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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.text.style.TextAlign
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
import com.example.weather.utils.getDayOfWeek
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
                        dailyWeather = {
                            WeatherSection(title = stringResource(R.string.coming_days)) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    val dailyList = success.dailyWeather.toObjectArray()
                                    for(dailyWeather in dailyList) {
                                        MediumWeatherCard(
                                            time = getDayOfWeek(dailyWeather.time),
                                            weatherCode = dailyWeather.weatherCode ?: 0,
                                            temperatureMax = dailyWeather.temperatureMax?.roundToInt() ?: 0,
                                            temperatureMin = dailyWeather.temperatureMin?.roundToInt() ?: 0,
                                            sunriseTime = getTime(dailyWeather.sunrise),
                                            sunsetTime = getTime(dailyWeather.sunset)
                                        )
                                    }
                                }
                            }
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
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 2.dp),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.data_source),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
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
            .size(width = 100.dp, height = 100.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(all = 4.dp),
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
                    fontSize = 8.sp
                )
            }
        }
    }
}

@Composable
fun MediumWeatherCard(
    time: String,
    weatherCode: Int,
    temperatureMax: Int,
    temperatureMin: Int,
    sunriseTime: String,
    sunsetTime: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(all = 4.dp)
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(
                                WeatherImages.getWeatherImage(
                                    weatherCode = weatherCode,
                                    isDay = true
                                )
                            )
                            .crossfade(true)
                            .build(),
                        contentDescription = "Weather image",
                        modifier = Modifier
                            .size(90.dp)
                            .padding(all = 4.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.widthIn(min = 90.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = temperatureMax.toString(),
                                fontSize = 14.sp
                            )
                            Text(
                                text = stringResource(R.string.degrees_c),
                                fontSize = 10.sp
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = temperatureMin.toString(),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = stringResource(R.string.degrees_c),
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {

                    WeatherParam(
                        paramImageSrc = WeatherImages.getParamImage(WeatherImages.WeatherParams.SUNRISE),
                        paramValue = sunriseTime,
                        contentDescription = stringResource(R.string.sunrise)
                    )

                    WeatherParam(
                        paramImageSrc = WeatherImages.getParamImage(WeatherImages.WeatherParams.SUNSET),
                        paramValue = sunsetTime,
                        contentDescription = stringResource(R.string.sunset)
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherParam(
    paramImageSrc: String,
    paramValue: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    paramImageSrc
                )
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(32.dp)
                .padding(all = 4.dp)
        )
        Text(
            text = paramValue,
            style = MaterialTheme.typography.titleSmall
        )
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
    WeatherTheme {
        SmallWeatherCard(
            time = "Now",
            weatherCode = 1,
            isDay = true,
            temperature = 10
        )
    }
}

@Composable
@Preview
fun MediumWeatherCardPreview() {
    WeatherTheme {
        MediumWeatherCard(
            time = "Monday",
            weatherCode = 1,
            temperatureMax = 10,
            temperatureMin = 1,
            sunriseTime = "4:20 AM",
            sunsetTime = "9:37 PM"
        )
    }
}