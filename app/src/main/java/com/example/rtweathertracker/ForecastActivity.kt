package com.example.rtweathertracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.rtweathertracker.databinding.ActivityForecastBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.roundToInt

class ForecastActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForecastBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomAppBarSetup()
    }

    private fun bottomAppBarSetup(){
        val nav=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        nav.background=null
        nav.selectedItemId=R.id.miforecast
        nav.setOnItemSelectedListener {
            when{
                it.itemId==R.id.miHome->{
                    val intent= Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnItemSelectedListener true
                }
                it.itemId==R.id.miSearch->{
                    val intent= Intent(applicationContext,SearchActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnItemSelectedListener true
                }
                it.itemId==R.id.miforecast->{
                    return@setOnItemSelectedListener true
                }
                else->{
                    return@setOnItemSelectedListener false
                }
            }
        }
    }


}