package com.example.tamovie.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.tamovie.R

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
//        supportActionBar!!.hide()

        Handler().postDelayed({
//            startActivity(Intent(this, HomeActivity::class.java))
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}