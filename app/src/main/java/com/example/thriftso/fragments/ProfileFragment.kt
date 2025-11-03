package com.example.thriftso.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.thriftso.LoginActivity
import com.example.thriftso.databinding.FragmentProfileBinding
import com.example.thriftso.repo.AuthRepository
import com.example.thriftso.repo.ThriftRepository

class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding? = null
    private lateinit var authRepository: AuthRepository
    private lateinit var thriftRepository: ThriftRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authRepository = AuthRepository(requireContext())
        thriftRepository = ThriftRepository(requireContext())
        setupProfile()
        setupClickListeners()
    }

    private fun setupProfile() {
        val currentUser = authRepository.getCurrentUser()

        currentUser?.let { user ->
            binding?.tvUserName?.text = user.fullName
            binding?.tvUserEmail?.text = user.email
            binding?.tvUserPhone?.text = if (user.phone.isNotEmpty()) user.phone else "Not set"
            binding?.tvUserAddress?.text = if (user.address.isNotEmpty()) user.address else "Not set"

            // Set stats
            val wishlistCount = thriftRepository.getWishlist().size
            binding?.tvWishlistCount?.text = wishlistCount.toString()
            binding?.tvOrdersCount?.text = "5" // Placeholder
        }
    }

    private fun setupClickListeners() {
        binding?.btnEditProfile?.setOnClickListener {
            showEditProfileDialog()
        }

        binding?.btnLogout?.setOnClickListener {
            authRepository.logout()
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        binding?.wishlistItem?.setOnClickListener {
            Toast.makeText(requireContext(), "Wishlist clicked", Toast.LENGTH_SHORT).show()
        }

        binding?.ordersItem?.setOnClickListener {
            Toast.makeText(requireContext(), "Orders clicked", Toast.LENGTH_SHORT).show()
        }

        binding?.settingsItem?.setOnClickListener {
            Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEditProfileDialog() {
        val currentUser = authRepository.getCurrentUser() ?: return

        // Simple edit dialog (bisa dikembangkan lebih lengkap)
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Edit Profile")
            .setMessage("Edit profile feature coming soon!")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        setupProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}