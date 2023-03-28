package com.example.weathernow.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weathernow.WeatherAPIService
import com.example.weathernow.model.Weather
import com.example.weathernow.model.WeatherModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val weatherApiService = WeatherAPIService()
    private val disposable = CompositeDisposable()

    val weather_data = MutableLiveData<WeatherModel>()
    val weather_desc=MutableLiveData<Weather>()
    val weather_error = MutableLiveData<Boolean>()
    val weather_loading = MutableLiveData<Boolean>()

    public fun refreshData(cityName: String) {
        getDataFromAPI(cityName)
    }

     fun getDataFromAPI(cityName: String) {

        weather_loading.value = true
        disposable.add(
            weatherApiService.getDataService(cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherModel>() {

                    override fun onSuccess(t: WeatherModel) {
                        weather_data.value = t
                        weather_error.value = false
                        weather_loading.value = false
                        Log.d(TAG, "onSuccess: Success")
                    }

                    override fun onError(e: Throwable) {
                        weather_error.value = true
                        weather_loading.value = false
                        Log.e(TAG, "onError: " + e)
                    }

                })
        )

    }

}