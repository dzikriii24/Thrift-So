package com.example.thriftso
import CartItem

import java.io.Serializable
data class CheckoutData(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val city: String = "",
    val postalCode: String = "",
    val shippingMethod: String = "Regular",
    val paymentMethod: String = "Bank Transfer"
) : Serializable

data class Order(
    val id: String = System.currentTimeMillis().toString(),
    val items: List<CartItem>,
    val checkoutData: CheckoutData,
    val totalAmount: Double,
    val orderDate: String = "",
    val status: String = "Pending"
) : Serializable