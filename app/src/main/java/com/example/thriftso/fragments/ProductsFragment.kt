package com.example.thriftso.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.thriftso.adapters.ProductAdapter
import com.example.thriftso.databinding.FragmentProductsBinding
import Product
import com.example.thriftso.MainActivity
import com.example.thriftso.repo.ThriftRepository
// HAPUS: import android.R <- INI SALAH!
import com.example.thriftso.R // IMPORT YANG BENAR

class ProductsFragment : Fragment() {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: ThriftRepository
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = ThriftRepository(requireContext())
        setupRecyclerView()
        loadProducts()
        setupAddProductButton()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            emptyList(),
            onAddToCart = { product, size ->
                repository.addToCart(product, size)
                Toast.makeText(requireContext(), "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
                (activity as? MainActivity)?.updateCartBadge()
            },
            onWishlistToggle = { product ->
                if (repository.isInWishlist(product.id)) {
                    repository.removeFromWishlist(product.id)
                    Toast.makeText(requireContext(), "Removed from wishlist", Toast.LENGTH_SHORT).show()
                } else {
                    repository.addToWishlist(product)
                    Toast.makeText(requireContext(), "Added to wishlist!", Toast.LENGTH_SHORT).show()
                }
            },
            onProductClick = { product ->
                showProductDetail(product)
            },
            onProductDelete = { product ->
                showDeleteConfirmation(product)
            }
        )

        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }
    }

    private fun setupAddProductButton() {
        binding.fabAddProduct.setOnClickListener {
            navigateToAddProduct()
        }
    }

    private fun navigateToAddProduct() {
        val addProductFragment = AddProductFragment()
        requireActivity().supportFragmentManager.beginTransaction() // GUNAKAN requireActivity()
            .replace(R.id.fragmentContainer, addProductFragment) // SEKARANG HARUSNYA TIDAK ERROR
            .addToBackStack("add_product")
            .commit()
    }

    private fun showDeleteConfirmation(product: Product) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete \"${product.name}\"?")
            .setPositiveButton("Delete") { dialog, which ->
                repository.deleteProduct(product.id)
                loadProducts()
                Toast.makeText(requireContext(), "Product deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showProductDetail(product: Product) {
        Toast.makeText(
            requireContext(),
            "${product.name}\nPrice: Rp ${product.price}\n${product.description}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun loadProducts() {
        val products = repository.getAllProducts()
        productAdapter.updateProducts(products)

        if (products.isEmpty()) {
            binding.tvEmptyProducts.visibility = View.VISIBLE
            binding.rvProducts.visibility = View.GONE
        } else {
            binding.tvEmptyProducts.visibility = View.GONE
            binding.rvProducts.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}