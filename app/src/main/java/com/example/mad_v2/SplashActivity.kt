package com.example.mad_v2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_v2.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimations()
        startLoadingDots()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2500)
    }

    private fun playAnimations() {
        // Fade animation
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 700
        }

        // Pulse animation for logo
        val pulse = ScaleAnimation(
            0.7f, 1f, 0.7f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 900
            fillAfter = true
        }

        binding.imgLogo.startAnimation(pulse)
        binding.tvTitle.startAnimation(fadeIn)
        binding.tvSub.startAnimation(fadeIn)
        binding.tvLoading.startAnimation(fadeIn)
    }

    private fun startLoadingDots() {
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            var dots = 0

            override fun run() {
                dots = (dots + 1) % 4
                binding.tvLoading.text = "Loading" + ".".repeat(dots)
                Handler(Looper.getMainLooper()).postDelayed(this, 500)
            }
        }, 500)
    }
}
