package com.example.weathernow.view

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log.e
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.weathernow.viewmodel.MainViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weathernow.LocationpageActivity
import com.example.weathernow.databinding.ActivityMainPageBinding
import java.text.SimpleDateFormat
import java.util.*


class MainPageActivity : AppCompatActivity(){

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainPageBinding


    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val cityname = intent.getStringExtra("cityName")

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()


        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        var cName = GET.getString("cityName","Dhanbad")
//        if (cName != null) {
//            (MainPageActivity() as  LocationpageActivity).setcityname(cName)
//        }

        viewModel.refreshData(cName!!)

        getLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.mainContainer.visibility = View.GONE
            binding.errortxt.visibility = View.GONE
            binding.loader.visibility = View.GONE

//            var cityName = GET.getString("cityName",cName)
//            if (cityName != null) {
//                (MainPageActivity() as  LocationpageActivity).setcityname(cityName)
//            }
            viewModel.refreshData(cName!!)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        cityname?.apply {  viewModel.getDataFromAPI(this)
                            binding.cityName.text = this
                            e("city",this)}
        viewModel.observe().observe(this){ e("data","$it")}




    }



    private fun getLiveData() {
       viewModel.weather_data.observe(this, Observer { data ->
           data?.let{
               binding.mainContainer.visibility = View.VISIBLE
               binding.loader.visibility = View.GONE
               val temp  = ((data.main.temp - 273.5)*100).toInt()/100
               binding.temp.text = temp.toString() + "⁰C"
               binding.cityName.text = data.name
               binding.sunrise.text = time(data.sys.sunrise.toLong()) + "AM"
               binding.sunset.text = time(data.sys.sunset.toLong()) + "PM"
               binding.wind.text = data.wind.speed.toString() + "m/s"
               binding.pressure.text= data.main.pressure.toString() + "hPa"
               binding.humidity.text = data.main.humidity.toString() + "%"
               val tempMax  = ((data.main.tempMax - 273.5)*100).toInt()/100
               binding.tempMax.text = "Max Temp:" +tempMax.toString() + "⁰C"
               val tempMin  = ((data.main.tempMin - 273.5)*100).toInt()/100
               binding.tempMin.text =  "Min Temp:" +tempMin.toString() + "⁰C"
               val feelsLike  = ((data.main.feelsLike - 273.5)*100).toInt()/100
               binding.feelLike.text = feelsLike.toString() + "⁰C"


           }
       })
        viewModel.weather_desc.observe(this, Observer { data->
            data ?.let {
                binding.status.text=data.description.toString()
            }
        })

        viewModel.weather_loading.observe(this, Observer { load ->
            load?.let {
                if(it){
                    binding.loader.visibility = View.VISIBLE
                    binding.errortxt.visibility = View.GONE
                    binding.mainContainer.visibility = View.GONE
                }else{
                    binding.loader.visibility = View.GONE
                }
            }
        })

        viewModel.weather_error.observe(this, Observer { error ->
            error?.let {
                if(it){
                    binding.errortxt.visibility = View.VISIBLE
                    binding.mainContainer.visibility = View.GONE
                    binding.loader.visibility = View.GONE
                }else{
                    binding.errortxt.visibility = View.GONE
                }
            }
        })
    }

    private fun time(t : Long):String{
        val date = Date(t)
        val f = SimpleDateFormat("HH :mm")
        return  f.format(date)
    }


}