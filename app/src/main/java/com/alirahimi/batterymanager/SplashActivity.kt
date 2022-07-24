package com.alirahimi.batterymanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alirahimi.batterymanager.databinding.ActivitySplashBinding
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val quotes = arrayOf("What are you looking for?", "Almost Done!", "Let's Go")

        for (i in 1..3) {
            textGenerator((i * 1000).toLong(), quotes[i - 1])
        }

        Timer().schedule(timerTask {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 4000)

    }

    private fun textGenerator(delayTime: Long, textToShow: String) {
        Timer().schedule(timerTask {
            runOnUiThread(timerTask {
                binding.splashText.text = textToShow
            })
        }, delayTime)
    }
}