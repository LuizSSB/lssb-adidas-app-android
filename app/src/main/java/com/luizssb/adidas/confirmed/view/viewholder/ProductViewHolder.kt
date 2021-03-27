package com.luizssb.adidas.confirmed.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luizssb.adidas.confirmed.databinding.ItemProductBinding
import com.luizssb.adidas.confirmed.dto.Product

class ProductViewHolder(private val layout: ItemProductBinding) : RecyclerView.ViewHolder(layout.root) {
    fun bind(item: Product?) {
        val fixdItem = item ?: PLACEHOLDER_PRODUCT
        layout.textName.text = fixdItem.name

//        if (item == null) {
//            layout.imageAvatar.setImageDrawable(null)
//        } else {
//            Glide.with(layout.root.context)
//                    .load(item.photo)
//                    .into(layout.imageAvatar)
//        }
    }

    companion object {
        fun create(parent: ViewGroup): ProductViewHolder {
            val layout = ItemProductBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
            )
            return ProductViewHolder(layout)
        }

        val PLACEHOLDER_PRODUCT = Product(
                id = "",
                name = "",
                description = "",
                imgUrl = "",
                price = 0f,
                currency = ""
        )
    }
}
