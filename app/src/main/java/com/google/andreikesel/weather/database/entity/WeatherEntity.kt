package com.google.andreikesel.weather.database.entity

import androidx.room.Entity

@Entity(tableName = "table_weather_info", primaryKeys = ["name"])
data class WeatherEntity(
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