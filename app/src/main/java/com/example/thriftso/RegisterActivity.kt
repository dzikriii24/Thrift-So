package com.example.thriftso

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thriftso.databinding.ActivityRegisterBinding
import com.example.thriftso.models.User
import com.example.thriftso.repo.AuthRepository

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepository(this)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            attemptRegister()
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun attemptRegister() {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        val phone = binding.etPhone.text.toString().trim()

        if (validateInput(fullName, email, password, confirmPassword, phone)) {
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.btnRegister.isEnabled = false

            // Simulate network delay
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                val newUser = User(
                    email = email,
                    password = password,
                    fullName = fullName,
                    phone = phone
                )

                val isSuccess = authRepository.registerUser(newUser)

                binding.progressBar.visibility = android.view.View.GONE
                binding.btnRegister.isEnabled = true

                if (isSuccess) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

                    // Auto login after registration
                    val user = authRepository.loginUser(email, password)
                    user?.let {
                        authRepository.saveCurrentUser(it)
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show()
                }
            }, 1000)
        }
    }

    private fun validateInput(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        phone: String
    ): Boolean {
        if (fullName.isEmpty()) {
            binding.etFullName.error = "Full name is required"
            return false
        }

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

        if (confirmPassword != password) {
            binding.etConfirmPassword.error = "Passwords do not match"
            return false
        }

        if (phone.isNotEmpty() && !android.util.Patterns.PHONE.matcher(phone).matches()) {
            binding.etPhone.error = "Please enter a valid phone number"
            return false
        }

        return true
    }
}