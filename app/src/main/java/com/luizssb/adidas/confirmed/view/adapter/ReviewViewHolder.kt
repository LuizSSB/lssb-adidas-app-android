package com.luizssb.adidas.confirmed.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luizssb.adidas.confirmed.databinding.ItemReviewBinding
import com.luizssb.adidas.confirmed.dto.Review

class ReviewViewHolder(private val layout: ItemReviewBinding) : RecyclerView.ViewHolder(layout.root) {
    private var currentReview: Review? = null

    fun bind(item: Review?) {
        val fixdItem = item ?: Review.NIL
        layout.textText.text = fixdItem.text
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
