package com.luizssb.adidas.confirmed.repository.product

import androidx.paging.PagingState
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.repository.paging.DefaultPagingSourceImpl
import com.luizssb.adidas.confirmed.service.product.ProductService

class ProductPagingSourceImpl(
        private val service: ProductService,
        searchQuery: String?
) : ProductPagingSource(searchQuery) {
    private val inner by lazy {
        DefaultPagingSourceImpl { service.getProducts(searchQuery, it) }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>) = inner.getRefreshKey(state)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> =
        inner.load(params)

    class Factory(private val service: ProductService) {
        fun produce(searchQuery: String?): ProductPagingSourceImpl {
            return ProductPagingSourceImpl(service, searchQuery)
        }
    }
}
