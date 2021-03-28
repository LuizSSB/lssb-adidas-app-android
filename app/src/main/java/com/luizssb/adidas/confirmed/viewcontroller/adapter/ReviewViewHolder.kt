package com.luizssb.adidas.confirmed.viewcontroller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luizssb.adidas.confirmed.databinding.ItemReviewBinding
import com.luizssb.adidas.confirmed.dto.Rating
import com.luizssb.adidas.confirmed.dto.Review

class ReviewViewHolder(private val layout: ItemReviewBinding) : RecyclerView.ViewHolder(layout.root) {
    fun bind(item: Review?) {
        val fixdItem = item ?: Review.NIL
        layout.rating.rating = fixdItem.rating.toFloat()
        // TODO get Locale object from review
//        layout.textLocation.text = Locale("en", "br").displayCountry
        layout.textText.text = fixdItem.text
    }

    companion object {
        fun create(parent: ViewGroup): ReviewViewHolder {
            val layout = ItemReviewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
            )
                    .apply {
                        rating.max = Rating.MAX.value
                        rating.numStars = Rating.MAX.value
                    }
            return ReviewViewHolder(layout)
        }
    }
}
