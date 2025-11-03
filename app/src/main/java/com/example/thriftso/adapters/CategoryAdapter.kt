package com.example.thriftso.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thriftso.R
import com.example.thriftso.models.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
        val nameTextView: TextView = itemView.findViewById(R.id.tvCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        // ✅ Load image dari URL pakai Glide
        Glide.with(holder.itemView.context)
            .load(category.imageUrl)
            .placeholder(R.drawable.placeholder_category) // ganti sesuai drawable placeholder kamu
            .error(R.drawable.placeholder_category)
            .centerCrop()
            .into(holder.iconImageView)

        // ✅ Set nama kategori
        holder.nameTextView.text = category.name

        // ✅ Handle klik kategori
        holder.itemView.setOnClickListener {
            onCategoryClick(category)
        }
    }

    override fun getItemCount(): Int = categories.size
}
