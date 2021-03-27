package com.luizssb.adidas.confirmed.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luizssb.adidas.confirmed.R
import com.luizssb.adidas.confirmed.databinding.ItemProductBinding
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.utils.EventHandler

class ProductViewHolder(
    private val layout: ItemProductBinding,
    onSelect: EventHandler<Product>?
    ) : RecyclerView.ViewHolder(layout.root) {
    init {
        if (onSelect != null) {
            layout.containerSelection.setOnClickListener {
                currentProduct?.let(onSelect)
            }
        }
    }

    private var currentProduct: Product? = null

    fun bind(item: Product?) {
        currentProduct = item

        val fixdItem = item ?: PLACEHOLDER_PRODUCT
        layout.textName.text = fixdItem.name
        layout.textDescription.text = fixdItem.description
        layout.textPrice.text = "R$"+fixdItem.price.toString()

        if (item == null) {
            layout.image.setImageDrawable(null)
        } else {
            Glide.with(layout.root.context)
                .load(item.imgUrl)
                .placeholder(R.mipmap.ic_launcher_desaturated)
                .into(layout.image)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onSelect: EventHandler<Product>?
        ): ProductViewHolder {
            val layout = ItemProductBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
            )
            return ProductViewHolder(layout, onSelect)
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
