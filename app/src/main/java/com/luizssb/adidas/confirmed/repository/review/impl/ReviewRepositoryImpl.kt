package com.luizssb.adidas.confirmed.repository.review.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.repository.review.ReviewPagingSource
import com.luizssb.adidas.confirmed.repository.review.ReviewRepository
import com.luizssb.adidas.confirmed.service.review.ReviewService
import kotlinx.coroutines.flow.Flow

class ReviewRepositoryImpl(
        private val pagingSourceFactory: ReviewPagingSource.Factory,
        private val service: ReviewService
) : ReviewRepository {
    override fun reviews(productId: String): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { pagingSourceFactory(productId) }
        ).flow
    }

    override suspend fun addReview(review: Review) = service.addReview(review)
}
