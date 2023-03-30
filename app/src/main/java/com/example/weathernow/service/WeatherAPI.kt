package com.example.weathernow.service

import com.example.weathernow.model.WeatherModel
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q=Dhanbad&appid=acee4dabaaeae51d875071c2c1847f07

interface WeatherAPI {

    @GET("weather")
    fun getData(
        @Query("q")
        cityName: String,
        @Query("appid")
        Appid:String
        ): Call<WeatherModel>
}