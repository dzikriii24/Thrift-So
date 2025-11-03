package com.example.thriftso.data

import Product
import CartItem
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalDatabaseManager(private val context: Context) {
    private val gson = Gson()
    private val productsFile = "products.json"
    private val cartFile = "cart.json"
    private val wishlistFile = "wishlist.json"

    // Products CRUD
    fun saveProducts(products: List<Product>) {
        val jsonString = gson.toJson(products)
        context.openFileOutput(productsFile, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    fun getProducts(): List<Product> {
        return try {
            context.openFileInput(productsFile).use {
                val jsonString = it.bufferedReader().use { reader -> reader.readText() }
                val type = object : TypeToken<List<Product>>() {}.type
                gson.fromJson(jsonString, type) ?: emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Cart CRUD
    fun saveCart(cartItems: List<CartItem>) {
        val jsonString = gson.toJson(cartItems)
        context.openFileOutput(cartFile, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    fun getCart(): List<CartItem> {
        return try {
            context.openFileInput(cartFile).use {
                val jsonString = it.bufferedReader().use { reader -> reader.readText() }
                val type = object : TypeToken<List<CartItem>>() {}.type
                gson.fromJson(jsonString, type) ?: emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Wishlist CRUD
    fun saveWishlist(products: List<Product>) {
        val jsonString = gson.toJson(products)
        context.openFileOutput(wishlistFile, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    fun getWishlist(): List<Product> {
        return try {
            context.openFileInput(wishlistFile).use {
                val jsonString = it.bufferedReader().use { reader -> reader.readText() }
                val type = object : TypeToken<List<Product>>() {}.type
                gson.fromJson(jsonString, type) ?: emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun clearCart() {
        saveCart(emptyList())
    }
}