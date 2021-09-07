package com.google.andreikesel.weather.cloud

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") val all: Int
)