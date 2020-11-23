package com.lumi.employeeoftheweek.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.lumi.employeeoftheweek.R

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.title = ""

        Handler(Looper.getMainLooper()).postDelayed(
            {startActivity(Intent(this@Splash, MainActivity::class.java))
                finish()
            },2000)

    }
}