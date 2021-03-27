package com.luizssb.adidas.confirmed.repository.product

import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun products(): Flow<PagingData<Product>>

    suspend fun getProduct(productId: String): Product?
}
