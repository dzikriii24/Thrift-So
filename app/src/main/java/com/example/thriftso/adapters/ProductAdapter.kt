package com.example.thriftso.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.thriftso.R
import Product

class ProductAdapter(
    private var products: List<Product>,
    private val onAddToCart: (Product, String) -> Unit,
    private val onWishlistToggle: (Product) -> Unit,
    private val onProductClick: (Product) -> Unit,
    private val onProductDelete: (Product) -> Unit = {} // TAMBAH PARAMETER INI
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: androidx.cardview.widget.CardView = itemView.findViewById(R.id.cardProduct)
        val imageView: ImageView = itemView.findViewById(R.id.ivProduct)
        val nameTextView: TextView = itemView.findViewById(R.id.tvProductName)
        val priceTextView: TextView = itemView.findViewById(R.id.tvProductPrice)
        val originalPriceTextView: TextView = itemView.findViewById(R.id.tvOriginalPrice)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvProductDescription)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val reviewCountTextView: TextView = itemView.findViewById(R.id.tvReviewCount)
        val discountBadge: TextView = itemView.findViewById(R.id.tvDiscountBadge)
        val conditionBadge: TextView = itemView.findViewById(R.id.tvConditionBadge)
        val featuredBadge: TextView = itemView.findViewById(R.id.tvFeaturedBadge)
        val sizeSpinner: Spinner = itemView.findViewById(R.id.spinnerSize)
        val addToCartButton: Button = itemView.findViewById(R.id.btnAddToCart)
        val wishlistButton: ImageButton = itemView.findViewById(R.id.btnWishlist)
        val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete) // TAMBAH INI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_improved, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        // Load image using Glide
        if (product.imageUrl.isNotEmpty()) {
            try {
                Glide.with(holder.itemView.context)
                    .load(product.imageUrl)
                    .placeholder(R.drawable.placeholder_product)
                    .error(R.drawable.placeholder_product)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(holder.imageView)
            } catch (e: Exception) {
                holder.imageView.setImageResource(R.drawable.placeholder_product)
            }
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder_product)
        }

        // Set other data
        holder.nameTextView.text = product.name
        holder.priceTextView.text = "Rp ${product.price.toInt()}"
        holder.descriptionTextView.text = product.description
        holder.ratingBar.rating = product.rating
        holder.reviewCountTextView.text = "(${product.reviewCount})"

        // Handle discount
        if (product.originalPrice != null) {
            holder.originalPriceTextView.visibility = View.VISIBLE
            holder.originalPriceTextView.text = "Rp ${product.originalPrice.toInt()}"
            holder.discountBadge.visibility = View.VISIBLE
            val discount = (((product.originalPrice - product.price) / product.originalPrice) * 100).toInt()
            holder.discountBadge.text = "${discount}% OFF"
        } else {
            holder.originalPriceTextView.visibility = View.GONE
            holder.discountBadge.visibility = View.GONE
        }

        // Condition badge
        holder.conditionBadge.text = product.condition
        when (product.condition.toLowerCase()) {
            "excellent" -> holder.conditionBadge.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.retro_green)
            )
            "good" -> holder.conditionBadge.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.retro_orange)
            )
            "fair" -> holder.conditionBadge.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.retro_red)
            )
        }

        // Featured badge
        holder.featuredBadge.visibility = if (product.isFeatured) View.VISIBLE else View.GONE

        // Size spinner
        val sizes = if (product.size.isNotEmpty()) product.size else listOf("S", "M", "L", "XL")
        val sizeAdapter = ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_item, sizes)
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.sizeSpinner.adapter = sizeAdapter

        // Wishlist button
        holder.wishlistButton.setImageResource(R.drawable.ic_heart_outline)

        // TAMBAH: Delete button
        holder.deleteButton.setOnClickListener {
            onProductDelete(product)
        }

        // Click listeners
        holder.cardView.setOnClickListener {
            onProductClick(product)
        }

        holder.addToCartButton.setOnClickListener {
            val selectedSize = holder.sizeSpinner.selectedItem as String
            onAddToCart(product, selectedSize)
        }

        holder.wishlistButton.setOnClickListener {
            onWishlistToggle(product)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}