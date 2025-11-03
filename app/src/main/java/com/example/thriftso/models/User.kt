package com.example.thriftso.models

data class User(
    val id: String = "",
    val email: String,
    val password: String,
    val fullName: String,
    val phone: String = "",
    val address: String = "",
    val createdAt: Long = System.currentTimeMillis()
)