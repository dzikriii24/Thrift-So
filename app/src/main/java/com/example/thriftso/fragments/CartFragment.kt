package com.example.thriftso.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thriftso.adapters.CartAdapter
import com.example.thriftso.databinding.FragmentCartBinding
import com.example.thriftso.repo.ThriftRepository
import com.example.thriftso.R

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null

    // PERBAIKAN: Gunakan get() untuk binding
    private val binding: FragmentCartBinding
        get() = _binding!!

    private lateinit var repository: ThriftRepository
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = ThriftRepository(requireContext())
        setupRecyclerView()
        setupCheckoutButton()
        loadCart()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            emptyList(),
            onQuantityChanged = { productId, size, quantity ->
                repository.updateCartItemQuantity(productId, size, quantity)
                loadCart()
                updateActivityCartBadge()
            },
            onRemoveItem = { productId, size ->
                repository.removeFromCart(productId, size)
                loadCart()
                updateActivityCartBadge()
                Toast.makeText(requireContext(), "Item removed from cart", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun setupCheckoutButton() {
        binding.btnCheckout.setOnClickListener {
            val cartItems = repository.getCart()
            if (cartItems.isEmpty()) {
                Toast.makeText(requireContext(), "Cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                navigateToCheckout()
            }
        }
    }


    private fun navigateToCheckout() {
        val checkoutFragment = CheckoutFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(com.example.thriftso.R.id.fragmentContainer, checkoutFragment)
            .addToBackStack("checkout")
            .commit()
    }


    private fun loadCart() {
        val cartItems = repository.getCart()
        cartAdapter.updateCartItems(cartItems)
        binding.tvTotal.text = "Total: Rp ${repository.getCartTotal()}"

        // Debug info
        println("DEBUG: Cart has ${cartItems.size} items")
        cartItems.forEach { item ->
            println("DEBUG: - ${item.product.name} (Size: ${item.selectedSize}) x ${item.quantity}")
        }

        // Show/hide empty state
        if (cartItems.isEmpty()) {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.rvCart.visibility = View.GONE
            binding.checkoutSection.visibility = View.GONE
        } else {
            binding.tvEmptyCart.visibility = View.GONE
            binding.rvCart.visibility = View.VISIBLE
            binding.checkoutSection.visibility = View.VISIBLE
        }
    }

    private fun updateActivityCartBadge() {
        // PERBAIKAN: Gunakan cara yang lebih aman untuk update badge
        activity?.let {
            if (it is com.example.thriftso.MainActivity) {
                it.updateCartBadge()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadCart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}