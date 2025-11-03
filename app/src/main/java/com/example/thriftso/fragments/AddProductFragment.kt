package com.example.thriftso.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.thriftso.databinding.FragmentAddProductBinding
import Product
import com.example.thriftso.repo.ThriftRepository

class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: ThriftRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = ThriftRepository(requireContext())
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnAddProduct.setOnClickListener {
            addSampleProduct()
        }

        binding.btnCancel.setOnClickListener {
            // Kembali ke previous fragment
            parentFragmentManager.popBackStack()
        }
    }

    private fun addSampleProduct() {
        // Buat produk sample untuk testing
        val newProduct = Product(
            name = "New Sample Product",
            price = 150000.0,
            description = "This is a sample product added via form",
            imageUrl = "https://picsum.photos/300/300?${System.currentTimeMillis()}",
            category = "T-Shirts",
            isFeatured = true,
            rating = 4.5f,
            reviewCount = 10,
            condition = "Excellent",
            seller = "Thrift So Store"
        )

        repository.addProduct(newProduct)
        Toast.makeText(requireContext(), "Product added successfully!", Toast.LENGTH_SHORT).show()

        // Kembali ke ProductsFragment
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}