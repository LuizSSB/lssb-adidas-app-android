package com.luizssb.adidas.confirmed.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luizssb.adidas.confirmed.R
import com.luizssb.adidas.confirmed.databinding.ItemProductBinding
import com.luizssb.adidas.confirmed.databinding.ItemReviewBinding
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.utils.EventHandler
import java.util.*

class ReviewViewHolder(private val layout: ItemReviewBinding) : RecyclerView.ViewHolder(layout.root) {
    private var currentReview: Review? = null

    fun bind(item: Review?) {
    }

    companion object {
        fun create(parent: ViewGroup): ReviewViewHolder {
            val layout = ItemReviewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
            )
            return ReviewViewHolder(layout)
        }
    }
}
