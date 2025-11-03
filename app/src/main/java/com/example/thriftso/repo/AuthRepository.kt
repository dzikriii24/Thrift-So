package com.example.thriftso.repo

import android.content.Context
import android.content.SharedPreferences
import com.example.thriftso.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AuthRepository(private val context: Context) {
    private val gson = Gson()
    private val sharedPref: SharedPreferences = context.getSharedPreferences("thriftso_auth", Context.MODE_PRIVATE)
    private val usersFile = "users.json"

    // SharedPreferences untuk session
    fun saveCurrentUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPref.edit().putString("current_user", userJson).apply()
    }

    fun getCurrentUser(): User? {
        val userJson = sharedPref.getString("current_user", null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun logout() {
        sharedPref.edit().remove("current_user").apply()
    }

    fun isLoggedIn(): Boolean {
        return getCurrentUser() != null
    }

    // User CRUD (simpan di file JSON)
    fun registerUser(user: User): Boolean {
        val users = getUsers().toMutableList()

        // Cek jika email sudah terdaftar
        if (users.any { it.email == user.email }) {
            return false
        }

        users.add(user.copy(id = System.currentTimeMillis().toString()))
        saveUsers(users)
        return true
    }

    fun loginUser(email: String, password: String): User? {
        val users = getUsers()
        return users.find { it.email == email && it.password == password }
    }

    private fun getUsers(): List<User> {
        return try {
            context.openFileInput(usersFile).use {
                val jsonString = it.bufferedReader().use { reader -> reader.readText() }
                val type = object : TypeToken<List<User>>() {}.type
                gson.fromJson(jsonString, type) ?: emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveUsers(users: List<User>) {
        val jsonString = gson.toJson(users)
        context.openFileOutput(usersFile, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    fun updateUserProfile(updatedUser: User): Boolean {
        val users = getUsers().toMutableList()
        val userIndex = users.indexOfFirst { it.id == updatedUser.id }

        if (userIndex != -1) {
            users[userIndex] = updatedUser
            saveUsers(users)

            // Update current user session jika sama
            val currentUser = getCurrentUser()
            if (currentUser?.id == updatedUser.id) {
                saveCurrentUser(updatedUser)
            }
            return true
        }
        return false
    }
}