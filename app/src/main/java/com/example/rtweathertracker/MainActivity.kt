package com.example.rtweathertracker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.rtweathertracker.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private var url: String=""
    private var latitude=17.3850
    private var longitude=78.4867
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        bottomAppBarSetup()
        getLastLocation()
        getWeatherDetails()

    }

    private fun bottomAppBarSetup(){
        val nav=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        nav.background=null
        nav.selectedItemId=R.id.miHome
        nav.setOnItemSelectedListener {
            when{
                it.itemId==R.id.miHome->{
                    return@setOnItemSelectedListener true
                }
                it.itemId==R.id.miSearch->{
                    val intent= Intent(applicationContext,SearchActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnItemSelectedListener true
                }
                it.itemId==R.id.miforecast->{
                    val intent= Intent(applicationContext,ForecastActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnItemSelectedListener true
                }
                else->{
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getWeatherDetails(){
        Log.i("MYTAG","SETTING")
        url="https://api.weatherbit.io/v2.0/current?lat=$latitude&lon=$longitude&key=b291b0ea6bd244c980a7ee0f0dcfbb94&units=M&include=minutely"
        val request = StringRequest(
            Request.Method.POST,
            url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val data = jsonObject.getJSONArray("data")
                    if (data.length() > 0) {
                        val infoObject = data.getJSONObject(0)
                        val cityname=infoObject.getString("city_name")
                        val windSpeed = infoObject.getString("wind_spd")
                        val kmph=(windSpeed.toDouble()*3.6).roundToInt()
                        val humidity=infoObject.getString("rh")
                        val precip=infoObject.getString("precip")
                        val temp=infoObject.getString("app_temp")
                        val weather=infoObject.getJSONObject("weather")
                        val des=weather.getString("description")
                        val imagecode=weather.getString("icon")
                        val imgres=resources.getIdentifier(imagecode,"drawable",packageName)

                        binding.apply {
                           tvwind.text="$kmph km/h"
                           tvhumid.text="$humidity%"
                           tvprecip.text="$precip%"
                           tvtemp.text="$temp°"
                           tvclouds.text= des
                            imvtype.setImageResource(imgres)
                            city.text=cityname
                        }


                        val lat = infoObject.getDouble("lat")
                        val lon = infoObject.getDouble("lon")

                        println("Wind speed: $windSpeed, Latitude: $lat, Longitude: $lon")
                    } else {
                        Log.i("MYTAG", "No data found in the response")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Log.i("MYTAG", "Failed")
                error.printStackTrace()
            }
        )

        val reqQueue=Volley.newRequestQueue(applicationContext)
        reqQueue.add(request)


    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Location", "Denied")
            return
        }
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            latitude = location.latitude
                            longitude = location.longitude
                            Log.i("MYTAG","lat=$latitude  long=$longitude")

                        } else {
                            Log.e("Location", "Location is null")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Location", "Failed to get location: ${e.message}")
                    }



    }

}