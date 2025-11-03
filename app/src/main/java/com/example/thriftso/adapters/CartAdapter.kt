package com.example.thriftso.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thriftso.R
import CartItem

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onQuantityChanged: (String, String, Int) -> Unit,
    private val onRemoveItem: (String, String) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivCartProduct)
        val nameTextView: TextView = itemView.findViewById(R.id.tvCartProductName)
        val priceTextView: TextView = itemView.findViewById(R.id.tvCartProductPrice)
        val quantityTextView: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnDecrease: Button = itemView.findViewById(R.id.btnDecrease)
        val btnIncrease: Button = itemView.findViewById(R.id.btnIncrease)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
        val sizeTextView: TextView = itemView.findViewById(R.id.tvSize)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val product = cartItem.product
        val context = holder.itemView.context

        // ✅ Load image dengan Glide
        Glide.with(context)
            .load(product.imageUrl)
            .placeholder(R.drawable.placeholder_product)
            .error(R.drawable.placeholder_product)
            .into(holder.imageView)

        // ✅ Gunakan string resource biar bisa diterjemahin
        holder.nameTextView.text = product.name
        holder.priceTextView.text = context.getString(
            R.string.price_format, product.price, cartItem.quantity
        )
        holder.quantityTextView.text = cartItem.quantity.toString()
        holder.sizeTextView.text = context.getString(
            R.string.size_format, cartItem.selectedSize
        )

        // Quantity logic
        holder.btnIncrease.setOnClickListener {
            onQuantityChanged(product.id, cartItem.selectedSize, cartItem.quantity + 1)
        }

        holder.btnDecrease.setOnClickListener {
            if (cartItem.quantity > 1) {
                onQuantityChanged(product.id, cartItem.selectedSize, cartItem.quantity - 1)
            }
        }

        holder.btnRemove.setOnClickListener {
            onRemoveItem(product.id, cartItem.selectedSize)
        }
    }

    override fun getItemCount(): Int = cartItems.size

    // ✅ DiffUtil untuk update efisien
    fun updateCartItems(newCartItems: List<CartItem>) {
        val diffResult = DiffUtil.calculateDiff(CartDiffCallback(cartItems, newCartItems))
        cartItems = newCartItems
        diffResult.dispatchUpdatesTo(this)
    }

    class CartDiffCallback(
        private val oldList: List<CartItem>,
        private val newList: List<CartItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldList[oldPos].product.id == newList[newPos].product.id &&
                    oldList[oldPos].selectedSize == newList[newPos].selectedSize
        }

        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldList[oldPos] == newList[newPos]
        }
    }
}
