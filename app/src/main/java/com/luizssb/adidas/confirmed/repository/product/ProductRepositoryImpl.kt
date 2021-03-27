package com.luizssb.adidas.confirmed.repository.product

import androidx.paging.Pager
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.repository.paging.PagingConfigEx
import com.luizssb.adidas.confirmed.service.product.ProductService
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val pagingSourceFactory: ProductPagingSource.Factory,
    private val service: ProductService
) : ProductRepository {
    override fun products(searchQuery: String?): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfigEx.DEFAULT_PAGING_CONFIG,
            pagingSourceFactory = { pagingSourceFactory(searchQuery) }
        ).flow
    }

    override suspend fun getProduct(productId: String): Product? = service.getProduct(productId)
}
