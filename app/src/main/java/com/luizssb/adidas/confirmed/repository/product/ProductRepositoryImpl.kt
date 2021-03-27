package com.luizssb.adidas.confirmed.repository.product

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.service.product.ProductService
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val pagingSourceFactory: () -> ProductPagingSource,
    private val service: ProductService
) : ProductRepository {
    override fun products(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun getProduct(productId: String): Product? = service.getProduct(productId)
}
