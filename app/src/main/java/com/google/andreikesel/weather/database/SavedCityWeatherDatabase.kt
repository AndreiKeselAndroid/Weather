package com.google.andreikesel.weather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.andreikesel.weather.database.dao.SavedCityWeatherDao
import com.google.andreikesel.weather.database.entity.SavedCityWeatherEntity

@Database(entities = [SavedCityWeatherEntity::class], version = 1)
abstract class SavedCityWeatherDatabase : RoomDatabase() {

    abstract fun savedWeatherCityDao(): SavedCityWeatherDao
}

object SavedDatabaseConstructor {

    fun create(context: Context): SavedCityWeatherDatabase =
        Room.databaseBuilder(
            context,
            SavedCityWeatherDatabase::class.java,
            "table_saved_city_weather"
        ).build()
}