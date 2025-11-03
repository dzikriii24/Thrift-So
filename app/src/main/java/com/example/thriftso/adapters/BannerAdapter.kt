package com.example.thriftso.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thriftso.R
import BannerItem

class BannerAdapter(private val banners: List<BannerItem>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivBanner: ImageView = itemView.findViewById(R.id.ivBanner)
        val tvBannerTitle: TextView = itemView.findViewById(R.id.tvBannerTitle)
        val tvBannerSubtitle: TextView = itemView.findViewById(R.id.tvBannerSubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val banner = banners[position]

        holder.tvBannerTitle.text = banner.title
        holder.tvBannerSubtitle.text = banner.subtitle

        // ✅ Load gambar pakai Glide dari URL
        Glide.with(holder.itemView.context)
            .load(banner.imageUrl)
            .placeholder(R.drawable.placeholder_product)
            .into(holder.ivBanner)
    }

    override fun getItemCount(): Int = banners.size
}
