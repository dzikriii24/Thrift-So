package com.example.thriftso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thriftso.adapters.BannerAdapter
import com.example.thriftso.adapters.CategoryAdapter
import com.example.thriftso.adapters.ProductAdapter
import com.example.thriftso.databinding.FragmentHomeBinding
import BannerItem
import com.example.thriftso.models.Category
import Product
import androidx.recyclerview.widget.RecyclerView
import com.example.thriftso.repo.ThriftRepository

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: ThriftRepository

    private lateinit var rvBanners: RecyclerView
    // Adapters
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var featuredProductsAdapter: ProductAdapter
    private lateinit var newArrivalsAdapter: ProductAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvBanners = view.findViewById(R.id.rvBanners)

        val banners = getBannerItems()

        rvBanners.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = BannerAdapter(banners)
        }

        binding.rvBanners.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvBanners.adapter = BannerAdapter(getBannerItems())

        repository = ThriftRepository(requireContext())
        setupUI()
        loadData()
        setupSwipeRefresh()
    }

    private fun setupUI() {
        // Setup Banner - skip dulu jika masih error
        try {
            bannerAdapter = BannerAdapter(getBannerItems())
            binding.rvBanners.adapter = bannerAdapter
            binding.rvBanners.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        } catch (e: Exception) {
            binding.rvBanners.visibility = View.GONE
            binding.tvBannerTitle.visibility = View.GONE
        }

        // Setup Categories - skip dulu jika masih error
        try {
            categoryAdapter = CategoryAdapter(getCategories()) { category ->
                (activity as? MainActivity)?.switchToProductsTab(category.name)
            }
            binding.rvCategories.adapter = categoryAdapter
            binding.rvCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        } catch (e: Exception) {
            binding.rvCategories.visibility = View.GONE
            binding.tvCategoriesTitle.visibility = View.GONE
        }

        // Setup Featured Products
        featuredProductsAdapter = ProductAdapter(
            emptyList(),
            onAddToCart = { product, size ->
                repository.addToCart(product, size)
                Toast.makeText(requireContext(), "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
                updateCartBadge()
            },
            onWishlistToggle = { product ->
                toggleWishlist(product)
            },
            onProductClick = { product ->
                showProductDetail(product)
            }
        )
        binding.rvFeaturedProducts.adapter = featuredProductsAdapter
        binding.rvFeaturedProducts.layoutManager = GridLayoutManager(requireContext(), 2)

        // Setup New Arrivals
        newArrivalsAdapter = ProductAdapter(
            emptyList(),
            onAddToCart = { product, size ->
                repository.addToCart(product, size)
                Toast.makeText(requireContext(), "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
                updateCartBadge()
            },
            onWishlistToggle = { product ->
                toggleWishlist(product)
            },
            onProductClick = { product ->
                showProductDetail(product)
            }
        )
        binding.rvNewArrivals.adapter = newArrivalsAdapter
        binding.rvNewArrivals.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun getBannerItems(): List<BannerItem> {
        return listOf(
            BannerItem(
                "Halloween Sale",
                "Up to 50% OFF",
                "https://i.pinimg.com/1200x/d8/be/6e/d8be6ecdb79a8fd6b8404fc5ac5f171b.jpg"
            ),
            BannerItem(
                "Retro Collection",
                "New Arrivals",
                "https://i.pinimg.com/736x/1b/49/8c/1b498cb7d7db3ef211df04d6c37b44af.jpg"
            ),
            BannerItem(
                "Jeans Collection",
                "Limited Time Offer",
                "https://i.pinimg.com/736x/b9/9c/0a/b99c0a5a3471cfb95799bcfd1aa9e045.jpg"
            )
        )
    }


    private fun getCategories(): List<Category> {
        return listOf(
            Category("T-Shirts", "https://i.pinimg.com/1200x/8e/e3/05/8ee305ced7c590d503056894cbd71785.jpg"),
            Category("Jackets", "https://i.pinimg.com/1200x/3f/06/7e/3f067e52eb78c9653a5eaab2f153c9dd.jpg"),
            Category("Pants", "https://i.pinimg.com/1200x/d0/3e/d1/d03ed143890357bd3e1bc2d8ece68dc7.jpg"),
            Category("Shoes", "https://i.pinimg.com/736x/93/67/f8/9367f8ff6181f322823cac9d7ef6eb06.jpg"),
            Category("Accessories", "https://i.pinimg.com/1200x/3b/1d/29/3b1d29ef6dacbd3664bcf3590f16936c.jpg"),
            Category("Dresses", "https://i.pinimg.com/1200x/f5/6e/4b/f56e4ba9832e60a4988478bd61fc8010.jpg")
        )
    }


    private fun loadData() {
        // Debug: Check what products we have
        val allProducts = repository.getAllProducts()
        println("DEBUG: Total products in database: ${allProducts.size}")
        allProducts.forEachIndexed { index, product ->
            println("DEBUG: Product $index: ${product.name}, Featured: ${product.isFeatured}")
        }

        // Load featured products
        val featuredProducts = repository.getFeaturedProducts()
        println("DEBUG: Featured products count: ${featuredProducts.size}")
        featuredProductsAdapter.updateProducts(featuredProducts)

        // Load new arrivals (take 4 latest)
        val newArrivals = allProducts.take(4)
        println("DEBUG: New arrivals count: ${newArrivals.size}")
        newArrivalsAdapter.updateProducts(newArrivals)

        // Update cart badge
        updateCartBadge()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.retro_orange),
            ContextCompat.getColor(requireContext(), R.color.retro_brown),
            ContextCompat.getColor(requireContext(), R.color.retro_dark_orange)
        )

        binding.swipeRefresh.setOnRefreshListener {
            loadData()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun updateCartBadge() {
        val cartCount = repository.getCartItemCount()
        if (cartCount > 0) {
            binding.tvCartBadge.visibility = View.VISIBLE
            binding.tvCartBadge.text = if (cartCount > 9) "9+" else cartCount.toString()
        } else {
            binding.tvCartBadge.visibility = View.GONE
        }

        // Update activity badge too
        (activity as? MainActivity)?.updateCartBadge()
    }

    private fun toggleWishlist(product: Product) {
        if (repository.isInWishlist(product.id)) {
            repository.removeFromWishlist(product.id)
            Toast.makeText(requireContext(), "Removed from wishlist", Toast.LENGTH_SHORT).show()
        } else {
            repository.addToWishlist(product)
            Toast.makeText(requireContext(), "Added to wishlist!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProductDetail(product: Product) {
        // Simple product detail using Toast for now
        Toast.makeText(
            requireContext(),
            "${product.name}\nPrice: Rp ${product.price}\n${product.description}",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}