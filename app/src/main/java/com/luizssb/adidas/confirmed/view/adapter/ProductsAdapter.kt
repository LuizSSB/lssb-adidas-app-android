package com.luizssb.adidas.confirmed.view.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.utils.EventHandler
import com.luizssb.adidas.confirmed.view.viewholder.ProductViewHolder

class ProductsAdapter(
    private var onSelectItem: EventHandler<Product>? = null
) : PagingDataAdapter<Product, ProductViewHolder>(DIFF_UTIL) {
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.create(parent, onSelectItem)
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}
