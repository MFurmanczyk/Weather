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

    /**
     * Set of ([weatherCode], [imageIndex]) pairs.
     */
    private val imageMap = mapOf(
        0 to 0, //Clear sky
        1 to 1, //Mainly clear
        2 to 3, //Partly cloudy
        3 to 2, //Overcast
        45 to 8, //Fog
        48 to 8, //Depositing rime fog
        51 to 5, //Drizzle
        53 to 5, //Drizzle
        55 to 5, //Drizzle
        56 to 5, //Freezing Drizzle
        57 to 5, //Freezing Drizzle
        61 to 4, //Rain
        63 to 4, //Rain
        65 to 4, //Rain
        66 to 4, //Rain
        67 to 4, //Rain
        71 to 7, //Snowfall
        73 to 7, //Snowfall
        75 to 7, //Snowfall
        77 to 7, //Snowfall
        80 to 4, //Shower
        81 to 4, //Shower
        82 to 7, //Snowfall
        85 to 7, //Snowfall
        86 to 7, //Snowfall
        95 to 6, //Thunderstorm
        96 to 6, //Thunderstorm
        99 to 6  //Thunderstorm
    )

    fun getWeatherImage(weatherCode: Int, isDay: Boolean): String {

        val imagesArray = if(isDay) dayImages else nightImages

        return try {
            val imageIndex = imageMap.getValue(weatherCode)
            imagesArray[imageIndex]
        } catch(e: NoSuchElementException) {
            imagesArray[0]
        }
    }
}




