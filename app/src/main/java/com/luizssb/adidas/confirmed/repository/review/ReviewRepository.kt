package com.luizssb.adidas.confirmed.repository.review

import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.dto.Review
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    // luizssb: not used; here to indicate to implementations that they must be unique to a product
    val productId: String

    fun reviews(): Flow<PagingData<Review>>

    suspend fun addReview(review: Review)
}
