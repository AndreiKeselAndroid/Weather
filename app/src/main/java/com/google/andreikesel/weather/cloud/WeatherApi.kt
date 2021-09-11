package com.google.andreikesel.weather.cloud

import com.google.andreikesel.BuildConfig
import com.google.andreikesel.weather.repository.ApiRepository.Companion.LANG
import com.google.andreikesel.weather.repository.ApiRepository.Companion.UNITS
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {
    @GET(BuildConfig.GET_API_KEY_WEATHER)
    suspend fun getWeatherCity(
        @Query("q") nameCity: String,
        @Query("lang") lang: String = LANG,
        @Query("units") units: String = UNITS,
    ): WeatherResponse

    @GET(BuildConfig.GET_API_KEY_WEATHER)
     suspend fun getWeatherCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String = LANG,
        @Query("units") units: String = UNITS,
    ): WeatherResponse

    companion object {
        fun get(): WeatherApi = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder().build()
            )
            .build().create(WeatherApi::class.java)
    }
}