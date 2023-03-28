package com.example.weathernow.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.weathernow.viewmodel.MainViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weathernow.LocationpageActivity
import com.example.weathernow.databinding.ActivityMainPageBinding


class MainPageActivity : AppCompatActivity(){

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainPageBinding


    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

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




    }



    private fun getLiveData() {
       viewModel.weather_data.observe(this, Observer { data ->
           data?.let{
               binding.mainContainer.visibility = View.VISIBLE
               binding.loader.visibility = View.GONE
               binding.temp.text = data.main.temp.toString() + "⁰C"
               binding.address.text = data.name.toString()
               binding.sunrise.text = data.sys.sunrise.toString()
               binding.sunset.text = data.sys.sunset.toString()
               binding.wind.text = data.wind.toString()
               binding.pressure.text= data.main.pressure.toString()
               binding.humidity.text = data.main.humidity.toString()

               binding.tempMax.text = data.main.tempMax.toString()
               binding.tempMin.text = data.main.tempMin.toString()
               binding.feelLike.text = data.main.feelsLike.toString()


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


}