package com.example.weather.ui.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weather.R
import com.example.weather.data.model.CurrentWeather
import com.example.weather.data.model.DailyWeatherUnit
import com.example.weather.data.model.HourlyWeatherUnit
import com.example.weather.data.model.toObjectArray
import com.example.weather.ui.viewmodel.WeatherState
import com.example.weather.ui.viewmodel.WeatherViewModel
import com.example.weather.utils.WeatherImages
import com.example.weather.utils.getDate
import com.example.weather.utils.getTime
import kotlin.math.roundToInt

/**
 *
 */
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    val weatherState: WeatherState by viewModel.weatherState.collectAsState()

    if(weatherState is WeatherState.Success) {

        val success = weatherState as WeatherState.Success

        WeatherScreenContainer(
            currentWeather = {
                LargeWeatherCard(
                    address = "${success.address.locality}, ${success.address.countryCode}",
                    currentWeatherState = success.currentWeather
                )
            },
            hourlyWeather = {
                LazyRow(
                    contentPadding = PaddingValues(all = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val hourlyWeather = success.hourlyWeather.toObjectArray()

                    items(hourlyWeather) { hourlyUnit ->
                        WeatherSmallCard(
                            weatherUnit = hourlyUnit
                        )
                    }

                }
            },
            dailyWeather = {

                val dailyWeather = success.dailyWeather.toObjectArray()

                for(dailyUnit in dailyWeather) {
                    WeatherMediumCard(
                        weatherUnit = dailyUnit,
                        modifier = Modifier
                            .padding(
                                horizontal = 4.dp,
                                vertical = 2.dp
                        )
                    )
                }

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Weather data from open-meteo.com",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            modifier = modifier
        )
    }
    else if(weatherState is WeatherState.Error) {
        /*TODO*/
        Column {
            Text(text = "Something went wrong.")
            Text(text = (weatherState as WeatherState.Error).getMessage())
        }

    } else {
        /*TODO*/
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

/**
 *
 */
@Composable
fun WeatherScreenContainer(
    currentWeather: @Composable () -> Unit,
    hourlyWeather: @Composable () -> Unit,
    dailyWeather: @Composable ()-> Unit,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        currentWeather()

        Text(
            modifier = Modifier.padding(start = 4.dp),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary,
            text = "Today"
        )
        hourlyWeather()

        Text(
            modifier = Modifier.padding(start = 4.dp),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary,
            text = "Coming days"
        )
        dailyWeather()
    }
}

/**
 *
 */
@Composable
fun LargeWeatherCard(
    /*TODO*/address: String,
    currentWeatherState: CurrentWeather,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    elevation: Dp = 0.dp
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .requiredHeight(450.dp)
            .clipToBounds(),
        shape = shape,
        tonalElevation = elevation,
        shadowElevation = elevation
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_location_on_24), contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = address,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text(
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Gray,
                text = "Now",
                modifier = Modifier.padding(top = 6.dp)
            )

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        WeatherImages.getWeatherImage(
                            weatherCode = currentWeatherState.weatherCode,
                            isDay = currentWeatherState.isDay == 1
                        )
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = "Weather image",
                modifier = Modifier.size(250.dp)
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ){
                WeatherElement(
                    id = R.drawable.baseline_thermostat_24,
                    elementValueString = currentWeatherState.temperature.roundToInt().toString() + "°C"
                )

                WeatherElement(
                    id = R.drawable.baseline_air_24,
                    elementValueString = currentWeatherState.windSpeed.roundToInt().toString() + "km/h"
                )
            }
        }
    }
}

@Composable
fun WeatherElement(
    @DrawableRes id: Int,
    elementValueString: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 24.sp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = id), contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = elementValueString,//currentWeatherState.temperature.roundToInt().toString() + "°C",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = fontSize
        )
    }
}

/**
 *
 */
@Composable
fun WeatherMediumCard(
    weatherUnit: DailyWeatherUnit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(200.dp)
            .clipToBounds(),
        shape = RoundedCornerShape(
            size = 8.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.LightGray
        )
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                text = getDate(weatherUnit.time),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(
                                WeatherImages.getWeatherImage(
                                    weatherCode = weatherUnit.weatherCode ?: 0,
                                    isDay = true
                                )
                            )
                            .crossfade(true)
                            .build(),
                        contentDescription = "Weather image",
                        modifier = Modifier
                            .size(120.dp)
                    )

                    Row(
                        modifier = Modifier.requiredWidth(120.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = (weatherUnit.temperatureMin ?: 0F).roundToInt().toString() + "°C",
                            fontSize = 14.sp
                        )
                        Text(
                            text = (weatherUnit.temperatureMax ?: 0F).roundToInt().toString() + "°C",
                            fontSize = 14.sp
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {

                    if((weatherUnit.precipitationHours ?: 0F) > 0F) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(
                                    id = R.drawable.baseline_umbrella_24
                                ),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Precipitation for\n${weatherUnit.precipitationHours?.roundToInt()} hours",
                                style = MaterialTheme.typography.bodySmall,
                                softWrap = true,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.baseline_wb_sunny_24
                            ),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        WeatherElement(
                            id = R.drawable.baseline_arrow_drop_up_24,
                            elementValueString = getTime(weatherUnit.sunrise)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.baseline_wb_sunny_24
                            ),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        WeatherElement(
                            id = R.drawable.baseline_arrow_drop_down_24,
                            elementValueString = getTime(weatherUnit.sunset)
                        )
                    }
                }
            }
        }
    }
}

/**
 *
 */
@Composable
fun WeatherSmallCard(
    weatherUnit: HourlyWeatherUnit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(
                width = 150.dp,
                height = 200.dp
            )
            .clipToBounds(),
        shape = RoundedCornerShape(
            size = 8.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.secondary
        ),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
                text = getTime(weatherUnit.time),
                modifier = Modifier.padding(top = 6.dp)
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        WeatherImages.getWeatherImage(
                            weatherCode = weatherUnit.weatherCode ?: 0,
                            isDay = weatherUnit.isDay == 1
                        )
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = "Weather image",
                modifier = Modifier
                    .size(120.dp)
                    .padding(8.dp)
            )
            WeatherElement(
                id = R.drawable.baseline_thermostat_24,
                elementValueString = (weatherUnit.temperature ?: 0F).roundToInt().toString() + "°C"
            )
        }
    }
}

@Preview
@Composable
fun WeatherLargeCardPreview() {
    MaterialTheme {
        LargeWeatherCard(
            "0.0, 0.0",
            CurrentWeather(
            10.0F,
            10.0F,
            10.0F,
            1,
            1,
            100020000
        )
        )
    }
}

@Preview
@Composable
fun WeatherMediumCardPreview() {
    MaterialTheme {
        WeatherMediumCard(
            DailyWeatherUnit()
        )
    }
}

@Preview
@Composable
fun WeatherSmallCardPreview() {
    MaterialTheme {
        WeatherSmallCard(HourlyWeatherUnit())
    }
}