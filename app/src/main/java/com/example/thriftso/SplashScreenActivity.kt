package com.example.thriftso

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.thriftso.databinding.ActivitySplashBinding
import com.example.thriftso.repo.AuthRepository

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepository(this)

        // Splash screen delay
        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthStatus()
        }, 2000) // 2 detik
    }

    private fun checkAuthStatus() {
        if (authRepository.isLoggedIn()) {
            // Jika sudah login, langsung ke MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Jika belum login, ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}