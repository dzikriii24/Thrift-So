package com.example.thriftso.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.thriftso.databinding.FragmentCheckoutBinding
import com.example.thriftso.CheckoutData
import com.example.thriftso.Order
import com.example.thriftso.R
import com.example.thriftso.repo.ThriftRepository
import java.text.NumberFormat
import java.util.Locale

class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: ThriftRepository

    // Format currency
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = ThriftRepository(requireContext())
        setupUI()
        setupClickListeners()
        loadOrderSummary()
    }

    private fun setupUI() {
        // Setup shipping method spinner
        val shippingMethods = arrayOf("Regular (3-5 days) - Rp 15.000", "Express (1-2 days) - Rp 30.000", "Same Day - Rp 50.000")
        val shippingAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, shippingMethods)
        shippingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerShippingMethod.adapter = shippingAdapter

        // Setup payment method spinner
        val paymentMethods = arrayOf("Bank Transfer (BCA)", "Bank Transfer (Mandiri)", "Credit Card", "E-Wallet (Gopay)", "E-Wallet (OVO)")
        val paymentAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentMethods)
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPaymentMethod.adapter = paymentAdapter
    }

    private fun setupClickListeners() {
        binding.btnPlaceOrder.setOnClickListener {
            if (validateForm()) {
                placeOrder()
            }
        }

        binding.btnBackToCart.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun loadOrderSummary() {
        val cartItems = repository.getCart()
        val subtotal = repository.getCartTotal()
        val shippingCost = when (binding.spinnerShippingMethod.selectedItemPosition) {
            0 -> 15000.0
            1 -> 30000.0
            2 -> 50000.0
            else -> 15000.0
        }
        val total = subtotal + shippingCost

        // Update UI
        binding.tvSubtotal.text = currencyFormat.format(subtotal)
        binding.tvShippingCost.text = currencyFormat.format(shippingCost)
        binding.tvTotalAmount.text = currencyFormat.format(total)
        binding.tvItemCount.text = "${cartItems.size} items"

        // Show cart items summary
        val itemsSummary = cartItems.joinToString("\n") { item ->
            "• ${item.product.name} (${item.selectedSize}) x${item.quantity}"
        }
        binding.tvItemsSummary.text = itemsSummary
    }

    private fun validateForm(): Boolean {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val postalCode = binding.etPostalCode.text.toString().trim()

        if (fullName.isEmpty()) {
            binding.etFullName.error = "Full name is required"
            return false
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Valid email is required"
            return false
        }

        if (phone.isEmpty() || phone.length < 10) {
            binding.etPhone.error = "Valid phone number is required"
            return false
        }

        if (address.isEmpty()) {
            binding.etAddress.error = "Address is required"
            return false
        }

        if (city.isEmpty()) {
            binding.etCity.error = "City is required"
            return false
        }

        if (postalCode.isEmpty()) {
            binding.etPostalCode.error = "Postal code is required"
            return false
        }

        return true
    }

    private fun placeOrder() {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val postalCode = binding.etPostalCode.text.toString().trim()
        val shippingMethod = binding.spinnerShippingMethod.selectedItem as String
        val paymentMethod = binding.spinnerPaymentMethod.selectedItem as String

        val checkoutData = CheckoutData(
            fullName = fullName,
            email = email,
            phone = phone,
            address = address,
            city = city,
            postalCode = postalCode,
            shippingMethod = shippingMethod,
            paymentMethod = paymentMethod
        )

        // Create order
        val order = repository.createOrder(checkoutData)

        // Clear cart after successful order
        repository.clearCart()

        // Navigate to success page
        navigateToOrderSuccess(order)
    }

    private fun navigateToOrderSuccess(order: Order) {
        val successFragment = OrderSuccessFragment.newInstance(order)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, successFragment)
            .addToBackStack("order_success")
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}