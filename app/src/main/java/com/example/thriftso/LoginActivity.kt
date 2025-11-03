package com.example.thriftso

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thriftso.databinding.ActivityLoginBinding
import com.example.thriftso.repo.AuthRepository

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepository(this)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            attemptLogin()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvSkipForNow.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun attemptLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (validateInput(email, password)) {
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.btnLogin.isEnabled = false

            // Simulate network delay
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                val user = authRepository.loginUser(email, password)

                binding.progressBar.visibility = android.view.View.GONE
                binding.btnLogin.isEnabled = true

                if (user != null) {
                    // Login successful
                    authRepository.saveCurrentUser(user)
                    Toast.makeText(this, "Welcome back, ${user.fullName}!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    // Login failed
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }, 1000)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Please enter a valid email"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters"
            return false
        }

        return true
    }
}