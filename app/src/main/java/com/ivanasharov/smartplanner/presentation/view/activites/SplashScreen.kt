package com.ivanasharov.smartplanner.presentation.view.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ivanasharov.smartplanner.R

class SplashScreen : AppCompatActivity() {

    companion object{
        private const val SPLASH_DURATION = 5000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        },
            SPLASH_DURATION
        )
    }
}