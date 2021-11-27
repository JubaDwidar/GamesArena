package com.example.Arena.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import com.example.Arena.databinding.ActivitySplashScreenBinding
import com.example.Arena.ui.games.GamesActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SplashScreenActivity : AppCompatActivity() {
    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashImage.apply {
            alpha = 0f
            animate().setDuration(1500).alpha(1f).withEndAction {
                val i = Intent(this@SplashScreenActivity, GamesActivity::class.java)
                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()

            }
        }

    }
}