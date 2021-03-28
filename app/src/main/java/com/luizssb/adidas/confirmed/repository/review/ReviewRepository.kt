package com.luizssb.adidas.confirmed.repository.review

import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.Review
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun reviews(productId: String): Flow<PagingData<Review>>

    suspend fun addReview(productId: String, review: Review)
}
