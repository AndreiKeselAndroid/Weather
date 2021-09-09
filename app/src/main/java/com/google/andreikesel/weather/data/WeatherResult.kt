package com.google.andreikesel.weather.data

data class WeatherResult(
    val description: String,
    val name: String,
    val temp: Int,
    val humidity: Int,
    val feelsLike: Double,
    val iconId: String,
    val windSpeed: Double
)