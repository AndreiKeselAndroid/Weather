package com.google.andreikesel.weather.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_saved_city_weather")
data class SavedCityWeatherEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
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