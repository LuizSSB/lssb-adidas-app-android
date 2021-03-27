package com.luizssb.adidas.confirmed.repository.product

import androidx.paging.PagingSource
import com.luizssb.adidas.confirmed.dto.Product

abstract class ProductPagingSource : PagingSource<Int, Product>() {
    interface Factory {
        operator fun invoke(searchQuery: String?): ProductPagingSource
    }
}
