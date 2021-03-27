package com.luizssb.adidas.confirmed.repository.review

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.service.review.ReviewService
import kotlinx.coroutines.flow.Flow

class ReviewRepositoryImpl(
    private val pagingSourceFactory: (String) -> ReviewPagingSource,
    private val service: ReviewService,
    override val productId: String
) : ReviewRepository {
    override fun reviews(): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { pagingSourceFactory(productId) }
        ).flow
    }

    override suspend fun addReview(review: Review) = service.addReview(productId, review)
}
