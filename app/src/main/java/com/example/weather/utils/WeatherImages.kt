package com.example.weather.utils

object WeatherImages {

    private val dayImages = listOf(
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/01d.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/02d.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/03d.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/04d.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/09d.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/10d.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/11d.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/13d.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/50d.png"
    )

    private val nightImages = listOf(
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/01n.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/02n.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/03n.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/04n.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/09n.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/10n.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/11n.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/13n.png",
        "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/50n.png"
    )

    fun getWeatherImage(weatherCode: Int, isDay: Boolean): String {

        val imagesArray = if(isDay) dayImages else nightImages

        return "https://raw.githubusercontent.com/hicodersofficial/weather-app/main/public/weather_icons/11d.png"
    }
}




