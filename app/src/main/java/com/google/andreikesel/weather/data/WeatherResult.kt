package com.google.andreikesel.weather.data

data class WeatherResult(
    val lat: Double,
    val lon: Double,
    val description: String,
    val name: String,
    val temp: Int,
    val humidity: Int,
    val feelsLike: Double,
    val iconId: String,
    val windSpeed: Double
)