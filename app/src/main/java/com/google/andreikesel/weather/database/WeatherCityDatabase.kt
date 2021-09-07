package com.google.andreikesel.weather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.andreikesel.weather.database.dao.WeatherDao
import com.google.andreikesel.weather.database.entity.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherCityDatabase : RoomDatabase() {
    abstract fun myWeatherCityDao(): WeatherDao
}

object DatabaseConstructor {

    fun create(context: Context): WeatherCityDatabase =
        Room.databaseBuilder(
            context,
            WeatherCityDatabase::class.java,
            "table_weather_info"
        ).build()
}