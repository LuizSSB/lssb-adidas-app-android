package com.luizssb.adidas.confirmed.repository.product.impl

import androidx.paging.PagingState
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.repository.paging.DefaultPagingSourceImpl
import com.luizssb.adidas.confirmed.repository.product.ProductPagingSource
import com.luizssb.adidas.confirmed.service.product.ProductService

class ProductPagingSourceImpl(
        private val service: ProductService,
        private val searchQuery: String?
) : ProductPagingSource() {
    private val inner by lazy {
        DefaultPagingSourceImpl { service.getProducts(searchQuery, it) }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>) = inner.getRefreshKey(state)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> =
        inner.load(params)

    class Factory(private val service: ProductService) : ProductPagingSource.Factory {
        override fun invoke(searchQuery: String?) = ProductPagingSourceImpl(service, searchQuery)
    }
}
