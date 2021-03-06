package com.luizssb.adidas.confirmed.viewcontroller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luizssb.adidas.confirmed.R
import com.luizssb.adidas.confirmed.databinding.ItemReviewBinding
import com.luizssb.adidas.confirmed.dto.Rating
import com.luizssb.adidas.confirmed.dto.Review

class ReviewViewHolder(private val layout: ItemReviewBinding) : RecyclerView.ViewHolder(layout.root) {
    fun bind(item: Review?) {
        val fixdItem = item ?: Review.NIL

        with(layout) {
            rating.rating = fixdItem.rating.value.toFloat()
            textLocation.text = fixdItem.locale?.displayCountry ?:
                layout.root.context.getString(R.string.label_locale_unknown)
            textText.text = fixdItem.text
        }
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
