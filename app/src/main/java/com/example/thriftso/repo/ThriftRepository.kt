package com.example.thriftso.repo
import android.content.Context
import com.example.thriftso.data.LocalDatabaseManager
import CartItem
import Product
import com.example.thriftso.CheckoutData
import com.example.thriftso.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ThriftRepository(private val context: Context) {
    private val databaseManager = LocalDatabaseManager(context)

    private val ordersFile = "orders.json"

    private val gson = Gson()

    fun saveOrders(orders: List<Order>) {
        val jsonString = gson.toJson(orders)
        context.openFileOutput(ordersFile, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    fun getOrders(): List<Order> {
        return try {
            context.openFileInput(ordersFile).use {
                val jsonString = it.bufferedReader().use { reader -> reader.readText() }
                val type = object : TypeToken<List<Order>>() {}.type
                gson.fromJson(jsonString, type) ?: emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun createOrder(checkoutData: CheckoutData): Order {
        val cartItems = getCart()
        val totalAmount = getCartTotal()
        val order = Order(
            items = cartItems,
            checkoutData = checkoutData,
            totalAmount = totalAmount,
            orderDate = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date()),
            status = "Processing"
        )

        val orders = getOrders().toMutableList()
        orders.add(order)
        saveOrders(orders)

        return order
    }

    // Products - TAMBAH FUNGSI DELETE
    fun getAllProducts(): List<Product> = databaseManager.getProducts()
    fun getFeaturedProducts(): List<Product> = databaseManager.getProducts().filter { it.isFeatured }
    fun getProductsByCategory(category: String): List<Product> =
        databaseManager.getProducts().filter { it.category.equals(category, ignoreCase = true) }

    fun searchProducts(query: String): List<Product> {
        return databaseManager.getProducts().filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
    }

    fun saveProducts(products: List<Product>) = databaseManager.saveProducts(products)

    // FUNGSI BARU: Hapus produk by ID
    fun deleteProduct(productId: String) {
        val products = databaseManager.getProducts().toMutableList()
        products.removeAll { it.id == productId }
        databaseManager.saveProducts(products)
        println("🗑️ DEBUG: Deleted product with ID: $productId")
    }

    // FUNGSI BARU: Tambah produk single
    fun addProduct(product: Product) {
        val products = databaseManager.getProducts().toMutableList()
        products.add(product)
        databaseManager.saveProducts(products)
        println("➕ DEBUG: Added new product: ${product.name}")
    }

    // FUNGSI BARU: Update produk
    fun updateProduct(updatedProduct: Product) {
        val products = databaseManager.getProducts().toMutableList()
        val index = products.indexOfFirst { it.id == updatedProduct.id }
        if (index != -1) {
            products[index] = updatedProduct
            databaseManager.saveProducts(products)
            println("✏️ DEBUG: Updated product: ${updatedProduct.name}")
        }
    }

    // Cart - tetap sama
    fun getCart(): List<CartItem> = databaseManager.getCart()
    fun addToCart(product: Product, size: String = "M") {
        val cart = databaseManager.getCart().toMutableList()
        val existingItem = cart.find { it.product.id == product.id && it.selectedSize == size }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cart.add(CartItem(product, selectedSize = size))
        }
        databaseManager.saveCart(cart)
    }

    fun removeFromCart(productId: String, size: String? = null) {
        val cart = databaseManager.getCart().toMutableList()
        if (size != null) {
            cart.removeAll { it.product.id == productId && it.selectedSize == size }
        } else {
            cart.removeAll { it.product.id == productId }
        }
        databaseManager.saveCart(cart)
    }

    fun updateCartItemQuantity(productId: String, size: String, quantity: Int) {
        val cart = databaseManager.getCart().toMutableList()
        val item = cart.find { it.product.id == productId && it.selectedSize == size }
        item?.quantity = quantity
        databaseManager.saveCart(cart)
    }

    fun clearCart() = databaseManager.clearCart()
    fun getCartTotal(): Double = databaseManager.getCart().sumOf { it.getTotalPrice() }
    fun getCartItemCount(): Int = databaseManager.getCart().sumOf { it.quantity }

    // Wishlist - tetap sama
    fun getWishlist(): List<Product> = databaseManager.getWishlist()
    fun addToWishlist(product: Product) {
        val wishlist = databaseManager.getWishlist().toMutableList()
        if (wishlist.none { it.id == product.id }) {
            wishlist.add(product)
            databaseManager.saveWishlist(wishlist)
        }
    }
    fun removeFromWishlist(productId: String) {
        val wishlist = databaseManager.getWishlist().toMutableList()
        wishlist.removeAll { it.id == productId }
        databaseManager.saveWishlist(wishlist)
    }
    fun isInWishlist(productId: String): Boolean = databaseManager.getWishlist().any { it.id == productId }
}