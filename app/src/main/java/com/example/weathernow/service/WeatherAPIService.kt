package com.example.weathernow

import com.example.weathernow.model.WeatherModel
import com.example.weathernow.service.WeatherAPI
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIService {

    //http://api.openweathermap.org/data/2.5/weather?q=bingol&APPID=04a42b96398abc8e4183798ed22f9485

    private val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherAPI::class.java)

    fun getDataService(cityName: String): Call<WeatherModel> {
        return api.getData(cityName,"acee4dabaaeae51d875071c2c1847f07")
    }

}
