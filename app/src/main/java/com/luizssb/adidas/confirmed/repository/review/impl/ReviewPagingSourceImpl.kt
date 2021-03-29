package com.luizssb.adidas.confirmed.repository.review.impl

import androidx.paging.PagingState
import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.repository.paging.DefaultPagingSourceImpl
import com.luizssb.adidas.confirmed.repository.review.ReviewPagingSource
import com.luizssb.adidas.confirmed.service.review.ReviewService

class ReviewPagingSourceImpl(
        private val service: ReviewService,
        private val productId: String
) : ReviewPagingSource() {
    private val proxy by lazy {
        DefaultPagingSourceImpl { service.getReviews(productId, it) }
    }

    override fun getRefreshKey(state: PagingState<Int, Review>) = proxy.getRefreshKey(state)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> =
        proxy.load(params)

    class Factory(private val service: ReviewService) : ReviewPagingSource.Factory {
        override fun invoke(productId: String) = ReviewPagingSourceImpl(service, productId)
    }
}
