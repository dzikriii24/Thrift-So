package com.example.thriftso

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.thriftso.databinding.ActivityMainBinding
import com.example.thriftso.fragments.CartFragment
import com.example.thriftso.fragments.ProductsFragment
import com.example.thriftso.fragments.ProfileFragment
import Product
import com.example.thriftso.repo.ThriftRepository
import com.example.thriftso.fragments.AddProductFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var repository: ThriftRepository

    // Keep track of current fragment to avoid unnecessary transactions
    private var currentFragmentTag: String? = null

    companion object {
        private const val TAG_HOME = "home"
        private const val TAG_PRODUCTS = "products"
        private const val TAG_CART = "cart"
        private const val TAG_PROFILE = "profile"
        private const val KEY_CURRENT_FRAGMENT = "current_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = ThriftRepository(this)
        setupInitialData()

        // Restore fragment state or load home
        if (savedInstanceState != null) {
            currentFragmentTag = savedInstanceState.getString(KEY_CURRENT_FRAGMENT)
        } else {
            loadFragment(HomeFragment(), TAG_HOME)
        }

        setupBottomNavigation()
        updateCartBadge()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_CURRENT_FRAGMENT, currentFragmentTag)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment(), TAG_HOME)
                    true
                }
                R.id.nav_products -> {
                    loadFragment(ProductsFragment(), TAG_PRODUCTS)
                    true
                }
                R.id.nav_cart -> {
                    loadFragment(CartFragment(), TAG_CART)
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment(), TAG_PROFILE)
                    true
                }
                else -> false
            }
        }

        // Set home as default if no saved state
        if (currentFragmentTag == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_home
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        // Avoid reloading the same fragment
        if (currentFragmentTag == tag) return

        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            replace(R.id.fragmentContainer, fragment, tag)
            commit()
        }

        currentFragmentTag = tag
    }

    private fun setupInitialData() {
        // Only add sample products if database is empty
        if (repository.getAllProducts().isEmpty()) {
            val sampleProducts = createSampleProducts()
            repository.saveProducts(sampleProducts)
        }
    }

    private fun createSampleProducts(): List<Product> {
        return listOf(
            Product(
                name = "Vintage Denim Jacket",
                price = 250000.0,
                originalPrice = 300000.0,
                description = "Classic 90s denim jacket in excellent condition. Perfect for casual outings and layering.",
                imageUrl = "https://i.pinimg.com/1200x/96/cf/88/96cf886a53d72ab1a5172741c51ccce8.jpg",
                category = "Jackets",
                isFeatured = true,
                rating = 4.5f,
                reviewCount = 128,
                condition = "Excellent",
                seller = "Retro Thrift Store",
                size = listOf("S", "M", "L", "XL")
            ),
            Product(
                name = "Flared Jeans",
                price = 180000.0,
                originalPrice = 250000.0,
                description = "70s style flared denim jeans. Authentic vintage piece.",
                imageUrl = "https://i.pinimg.com/1200x/25/fd/e5/25fde536cbb5d7f62e832eafbcbbb78f.jpg",
                category = "Pants",
                isFeatured = true,
                rating = 4.3f,
                reviewCount = 92,
                condition = "Excellent",
                seller = "Vintage Denim Co.",
                size = listOf("28", "30", "32", "34")
            ),
            Product(
                name = "Vintage Band T-Shirt",
                price = 120000.0,
                description = "Authentic 80s band t-shirt. Soft cotton material.",
                imageUrl = "https://i.pinimg.com/1200x/59/af/fb/59affb2a4d0f33ba7e1cab81213b8a0e.jpg",
                category = "T-Shirts",
                isFeatured = false,
                rating = 4.6f,
                reviewCount = 156,
                condition = "Good",
                seller = "Rock Vintage",
                size = listOf("S", "M", "L")
            ),
            Product(
                name = "Leather Messenger Bag",
                price = 280000.0,
                description = "Genuine leather messenger bag with vintage patina.",
                imageUrl = "https://i.pinimg.com/1200x/96/5a/5a/965a5a5b5918312cbd8077c528486d2a.jpg",
                category = "Accessories",
                isFeatured = true,
                rating = 4.7f,
                reviewCount = 64,
                condition = "Excellent",
                seller = "Leather & Co.",
                size = listOf("One Size")
            ),
            Product(
                name = "Oversized Blazer",
                price = 320000.0,
                description = "90s oversized blazer. Perfect for smart casual look.",
                imageUrl = "https://i.pinimg.com/1200x/18/7b/33/187b33d4d51b4a9a2750d82094673e24.jpg",
                category = "Jackets",
                isFeatured = false,
                rating = 4.4f,
                reviewCount = 73,
                condition = "Excellent",
                seller = "Retro Thrift Store",
                size = listOf("M", "L", "XL")
            ),
            Product(
                name = "Vintage Sunglasses",
                price = 95000.0,
                description = "Classic round sunglasses. UV protected lenses.",
                imageUrl = "https://i.pinimg.com/1200x/92/b6/8c/92b68cf6a6cebfc0c1c8575079a28a20.jpg",
                category = "Accessories",
                isFeatured = false,
                rating = 4.2f,
                reviewCount = 41,
                condition = "Good",
                seller = "Retro Accessories",
                size = listOf("One Size")
            ),
            Product(
                name = "Floral Midi Dress",
                price = 210000.0,
                description = "Beautiful 90s floral midi dress. Flowy and comfortable.",
                imageUrl = "https://i.pinimg.com/1200x/d8/bb/86/d8bb861ed254653854690221abcdcff5.jpg",
                category = "Dresses",
                isFeatured = true,
                rating = 4.9f,
                reviewCount = 112,
                condition = "Excellent",
                seller = "Vintage Boutique",
                size = listOf("S", "M", "L")
            ),
            Product(
                name = "Vintages Jacket",
                price = 250000.0,
                originalPrice = 300000.0,
                description = "Classic 90s denim jacket in excellent condition. Perfect for casual outings and layering.",
                imageUrl = "https://i.pinimg.com/736x/11/a1/2d/11a12d435f954835ae17d1a88032ece8.jpg",
                category = "Jackets",
                isFeatured = true,
                rating = 4.5f,
                reviewCount = 128,
                condition = "Excellent",
                seller = "Retro Thrift Store",
                size = listOf("S", "M", "L", "XL")
            ),

            Product(
                name = "Urban Tee",
                price = 150000.0,
                originalPrice = 180000.0,
                description = "Comfortable unisex t-shirt made from 100% organic cotton. Minimalist and modern design.",
                imageUrl = "https://i.pinimg.com/736x/3d/f6/5e/3df65e495989c7ef41468223e9ba4d26.jpg",
                category = "T-Shirts",
                isFeatured = false,
                rating = 4.2f,
                reviewCount = 89,
                condition = "New",
                seller = "EcoWear",
                size = listOf("M", "L", "XL")
            ),

            Product(
                name = "Floral Bliss Dress",
                price = 325000.0,
                originalPrice = 400000.0,
                description = "Lightweight summer dress with a vibrant floral pattern. Ideal for dates and garden parties.",
                imageUrl = "https://i.pinimg.com/1200x/43/7f/38/437f3846b8f53cebff23dbbd3fe7926b.jpg",
                category = "Dresses",
                isFeatured = true,
                rating = 4.8f,
                reviewCount = 204,
                condition = "New",
                seller = "Blossom Boutique",
                size = listOf("S", "M", "L")
            ),

            Product(
                name = "Retro Runner Sneakers",
                price = 450000.0,
                originalPrice = 500000.0,
                description = "Stylish and comfortable sneakers with retro vibes. Durable sole and breathable upper.",
                imageUrl = "https://i.pinimg.com/1200x/4f/4d/73/4f4d736004a1c7f836c679488a8bc8de.jpg",
                category = "Shoes",
                isFeatured = true,
                rating = 4.7f,
                reviewCount = 350,
                condition = "New",
                seller = "StepUp Store",
                size = listOf("38", "39", "40", "41", "42", "43")
            ),

            Product(
                name = "Classic Leather Belt",
                price = 175000.0,
                originalPrice = 200000.0,
                description = "Handcrafted leather belt with vintage buckle. Essential accessory for formal and casual wear.",
                imageUrl = "https://i.pinimg.com/1200x/11/64/e2/1164e28ac3f9aa00b055c6a9434f39b9.jpg",
                category = "Accessories",
                isFeatured = false,
                rating = 4.4f,
                reviewCount = 73,
                condition = "Like New",
                seller = "Genuine Craft",
                size = listOf("M", "L", "XL")
            ),

            Product(
                name = "Cozy Knit Sweater",
                price = 280000.0,
                originalPrice = 350000.0,
                description = "Warm and soft sweater made with premium wool. A perfect fit for cold seasons.",
                imageUrl = "https://i.pinimg.com/736x/5e/4b/1b/5e4b1b45ac877c008ade244ea3f3b651.jpg",
                category = "Knitwear",
                isFeatured = true,
                rating = 4.6f,
                reviewCount = 119,
                condition = "New",
                seller = "WinterWear Co.",
                size = listOf("M", "L")
            ),

            Product(
                name = "Slim Fit Chinos",
                price = 225000.0,
                originalPrice = 260000.0,
                description = "Stylish slim-fit chinos, comfortable and perfect for semi-formal outfits.",
                imageUrl = "https://i.pinimg.com/1200x/33/6f/50/336f50286d227edd2a3364c2e4322b09.jpg",
                category = "Pants",
                isFeatured = false,
                rating = 4.3f,
                reviewCount = 97,
                condition = "New",
                seller = "Urban Attire",
                size = listOf("30", "32", "34", "36")
            ),

            Product(
                name = "Vintage Sunglasses",
                price = 190000.0,
                originalPrice = 220000.0,
                description = "Retro-style sunglasses with UV protection. Lightweight and perfect for sunny days.",
                imageUrl = "https://i.pinimg.com/1200x/18/77/70/1877706d1a08a8d52f87a32758a01e22.jpg",
                category = "Accessories",
                isFeatured = true,
                rating = 4.9f,
                reviewCount = 287,
                condition = "Excellent",
                seller = "LensCraft",
                size = listOf()
            ),

            Product(
                name = "Classic Chelsea Boots",
                price = 550000.0,
                originalPrice = 600000.0,
                description = "Premium leather Chelsea boots with elastic side panels. Durable and elegant, suitable for any occasion.",
                imageUrl = "https://i.pinimg.com/1200x/95/c5/4c/95c54c61382b131c72b2a7c213baccec.jpg",
                category = "Shoes",
                isFeatured = true,
                rating = 4.8f,
                reviewCount = 412,
                condition = "New",
                seller = "FootStyle",
                size = listOf("40", "41", "42", "43", "44")
            ),

            Product(
                name = "Elegant Pearl Necklace",
                price = 320000.0,
                originalPrice = 400000.0,
                description = "Timeless pearl necklace with a classy touch. Perfect accessory to complement your evening outfit.",
                imageUrl = "https://i.pinimg.com/736x/e7/16/20/e716208fd92b9a54776d9a4429302c88.jpg",
                category = "Jewelry",
                isFeatured = false,
                rating = 4.6f,
                reviewCount = 154,
                condition = "Like New",
                seller = "PearlNest",
                size = listOf()
            )

        )
    }

    fun updateCartBadge() {
        val cartCount = repository.getCartItemCount()
        val badge = binding.bottomNavigation.getOrCreateBadge(R.id.nav_cart)

        if (cartCount > 0) {
            badge.isVisible = true
            badge.number = cartCount
            badge.backgroundColor = ContextCompat.getColor(this, R.color.retro_orange)
            badge.badgeTextColor = ContextCompat.getColor(this, android.R.color.white)
        } else {
            badge.isVisible = false
        }
    }

    fun switchToProductsTab(category: String? = null) {
        // Switch to products tab and optionally pass category filter
        val fragment = ProductsFragment()
        // Pass category via arguments if needed
        if (category != null) {
            val bundle = Bundle()
            bundle.putString("category", category)
            fragment.arguments = bundle
        }
        loadFragment(fragment, TAG_PRODUCTS)
        binding.bottomNavigation.selectedItemId = R.id.nav_products
    }

    // Tambah di MainActivity
    fun navigateToAddProduct() {
        val addProductFragment = AddProductFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, addProductFragment)
            .addToBackStack("add_product")
            .commit()
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
    }

}