package com.example.rtweathertracker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.rtweathertracker.databinding.ActivitySearchBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONException
import org.json.JSONObject

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    var latitude=0.0
    var longitude=0.0
    private var url=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomAppBarSetup()
        getWeatherDetails()


    }

    private fun bottomAppBarSetup(){
        val nav=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        nav.background=null
        nav.selectedItemId=R.id.miSearch
        nav.setOnItemSelectedListener {
            when{
                it.itemId==R.id.miHome->{
                    val intent= Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnItemSelectedListener true
                }
                it.itemId==R.id.miSearch->{
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
        val t= listOf(listOf(28.7041,77.1025), listOf(22.5726,88.3639), listOf(12.9716,77.5946),
            listOf(13.0827,80.2707))
        t.forEachIndexed { index, doubles ->
            latitude=t[index][0]
            longitude=t[index][1]

            url =
                "https://api.weatherbit.io/v2.0/current?lat=$latitude&lon=$longitude&key=b291b0ea6bd244c980a7ee0f0dcfbb94&units=M&include=minutely"
            val request = StringRequest(
                Request.Method.POST,
                url,
                { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        val data = jsonObject.getJSONArray("data")
                        if (data.length() > 0) {
                            val infoObject = data.getJSONObject(0)
                            val temp = infoObject.getString("app_temp")
                            val weather = infoObject.getJSONObject("weather")
                            val des = weather.getString("description")
                            val imagecode = weather.getString("icon")
                            val imgres = resources.getIdentifier(imagecode, "drawable", packageName)

                        when{
                            index==0->{
                                binding.imv1.setImageResource(imgres)
                                binding.tvtemp1.text="$temp°"
                            }
                            index==1->{
                                binding.imv2.setImageResource(imgres)
                                binding.tvtemp2.text="$temp°"
                            }
                            index==2->{
                                binding.imv3.setImageResource(imgres)
                                binding.tvtemp3.text="$temp°"
                            }
                            index==3->{
                                binding.imv4.setImageResource(imgres)
                                binding.tvtemp4.text="$temp°"
                            }
                        }


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

            val reqQueue = Volley.newRequestQueue(applicationContext)
            reqQueue.add(request)
        }

    }
}