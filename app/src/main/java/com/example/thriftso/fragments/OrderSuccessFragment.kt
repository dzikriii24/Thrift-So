package com.example.thriftso.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.thriftso.databinding.FragmentOrderSuccessBinding
import com.example.thriftso.Order // IMPORT YANG BENAR
import java.text.NumberFormat
import java.util.Locale

class OrderSuccessFragment : Fragment() {
    private var _binding: FragmentOrderSuccessBinding? = null
    private val binding get() = _binding!!

    private lateinit var order: Order
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    companion object {
        private const val ARG_ORDER = "order"

        fun newInstance(order: Order): OrderSuccessFragment {
            val fragment = OrderSuccessFragment()
            val args = Bundle()
            args.putSerializable(ARG_ORDER, order)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get order from arguments
        order = arguments?.getSerializable(ARG_ORDER) as Order

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        // Set order details
        binding.tvOrderId.text = "Order #${order.id.takeLast(6).uppercase()}" // Fix: uppercase() bukan toUpperCase()
        binding.tvOrderDate.text = order.orderDate
        binding.tvCustomerName.text = order.checkoutData.fullName
        binding.tvCustomerEmail.text = order.checkoutData.email
        binding.tvCustomerPhone.text = order.checkoutData.phone
        binding.tvShippingAddress.text = "${order.checkoutData.address}, ${order.checkoutData.city} ${order.checkoutData.postalCode}"
        binding.tvShippingMethod.text = order.checkoutData.shippingMethod
        binding.tvPaymentMethod.text = order.checkoutData.paymentMethod
        binding.tvTotalAmount.text = currencyFormat.format(order.totalAmount)

        // Set order items
        val itemsText = order.items.joinToString("\n") { item ->
            "• ${item.product.name} (${item.selectedSize}) x${item.quantity} - ${currencyFormat.format(item.getTotalPrice())}"
        }
        binding.tvOrderItems.text = itemsText

        // Animate success
        animateSuccess()
    }

    private fun animateSuccess() {
        binding.ivSuccess.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(500)
            .withEndAction {
                binding.ivSuccess.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(500)
                    .start()
            }
            .start()
    }

    private fun setupClickListeners() {
        binding.btnContinueShopping.setOnClickListener {
            // Navigate back to home and clear back stack
            requireActivity().supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
            (requireActivity() as? com.example.thriftso.MainActivity)?.apply {
                binding.bottomNavigation.selectedItemId = com.example.thriftso.R.id.nav_home
            }
        }

        binding.btnViewOrders.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
            (requireActivity() as? com.example.thriftso.MainActivity)?.apply {
                binding.bottomNavigation.selectedItemId = com.example.thriftso.R.id.nav_profile
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}