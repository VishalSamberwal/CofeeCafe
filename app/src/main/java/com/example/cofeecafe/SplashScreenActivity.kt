package com.example.cofeecafe

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import java.util.TimerTask

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        Timer().schedule(object : TimerTask() {
            override fun run() {

                val intent = Intent(this@SplashScreenActivity, LogInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)
    }
}
