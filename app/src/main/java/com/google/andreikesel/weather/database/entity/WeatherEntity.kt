package com.google.andreikesel.weather.database.entity

import androidx.room.Entity

@Entity(tableName = "table_weather_info", primaryKeys = ["name"])
data class WeatherEntity(
    val description: String,
    val name: String,
    val temp: Double,
    val humidity: Int,
    val feelsLike: Double,
    val iconId: String,
    val windSpeed: Double
)