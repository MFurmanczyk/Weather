package com.example.weather.ui.view

import android.content.Intent.ShortcutIconResource
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weather.R
import com.example.weather.data.model.CurrentWeather
import com.example.weather.ui.theme.WeatherTheme
import com.example.weather.ui.viewmodel.WeatherState
import com.example.weather.ui.viewmodel.WeatherViewModel
import com.example.weather.utils.WeatherImages
import kotlin.math.roundToInt

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

    if(weatherState is WeatherState.Success) {

        val success = weatherState as WeatherState.Success

        WeatherScreenContainer(
            currentWeather = {
                LargeWeatherCard(
                    currentWeatherState = success.currentWeather
                )
                             },
            hourlyWeather = { },
            dailyWeather = { },
            modifier = modifier
        )
    }
    else if(weatherState is WeatherState.Error) {
        /*TODO*/
        Column {
            Text(text = "Something went wrong.")
            Text(text = (weatherState as WeatherState.Error).exception.message.toString())
        }

    } else {
        /*TODO*/
        //Text(text = "Loading")
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        currentWeather()
        hourlyWeather()
        dailyWeather()
    }
}

/**
 *
 */
@Composable
fun LargeWeatherCard(
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
            Text(
                text = "Berlin, DE",
                style = MaterialTheme.typography.titleLarge
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
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(
                width = 100.dp,
                height = 150.dp
            )
            .clipToBounds(),
        shape = RoundedCornerShape(
            size = 8.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.LightGray
        )
    ) {
    }
}

/**
 *
 */
@Composable
fun WeatherSmallCard(
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Card(
            modifier = Modifier
                .padding(
                    bottom = 8.dp,
                    start = 8.dp,
                    end = 8.dp
                )
                .widthIn(min = 50.dp, max = 50.dp)
                .heightIn(min = 70.dp, max = 70.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

        }
    }
}

@Preview
@Composable
fun WeatherLargeCardPreview() {
    MaterialTheme {
        LargeWeatherCard(
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
        WeatherMediumCard()
    }
}

@Preview
@Composable
fun WeatherSmallCardPreview() {
    MaterialTheme {
        WeatherSmallCard()
    }
}