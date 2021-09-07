package com.google.andreikesel.weather.data

data class WeatherResult(
    val description: String,
    val name: String,
    val temp: Double,
    val humidity: Int,
    val feelsLike: Double,
    val iconId: String
)