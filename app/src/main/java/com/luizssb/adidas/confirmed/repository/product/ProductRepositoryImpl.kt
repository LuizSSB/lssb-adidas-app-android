package com.luizssb.adidas.confirmed.repository.product

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val pagingSourceFactory: () -> ProductPagingSource
) : ProductRepository, CoroutineScope by MainScope() {
    override fun products(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}
