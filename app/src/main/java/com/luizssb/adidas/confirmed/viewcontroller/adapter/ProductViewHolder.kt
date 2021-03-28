package com.luizssb.adidas.confirmed.viewcontroller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luizssb.adidas.confirmed.databinding.ItemProductBinding
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.utils.EventHandler
import com.luizssb.adidas.confirmed.utils.extensions.ProductEx.Companion.getCompleteName
import java.text.NumberFormat
import java.util.*

class ProductViewHolder(
    private val layout: ItemProductBinding,
    onSelect: EventHandler<Product>?
    ) : RecyclerView.ViewHolder(layout.root) {
    private val numberFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())

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
            textPrice.text = numberFormatter
                    .apply { currency = Currency.getInstance(fixdItem.currency) }
                    .format(fixdItem.price)

            if (item == null) {
                image.setImageDrawable(null)
            } else {
                image.setRemoteImage(fixdItem.imgUrl)
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
