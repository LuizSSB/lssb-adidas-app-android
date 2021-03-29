package com.luizssb.adidas.confirmed.viewcontroller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luizssb.adidas.confirmed.databinding.ItemProductBinding
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.utils.EventHandler
import com.luizssb.adidas.confirmed.utils.extensions.ImageViewEx.Companion.setRemoteImage
import com.luizssb.adidas.confirmed.utils.extensions.ProductEx.Companion.getCompleteName

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

        val fixdItem = item ?: Product.NIL

        with (layout) {
            textName.text = fixdItem.getCompleteName(root.context)
            textDescription.text = fixdItem.description
            textPrice.text = fixdItem.priceString

            if (item == null) {
                image.setImageDrawable(null)
            } else {
                image.setRemoteImage(fixdItem.imageUrl)
            }
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
    }
}
