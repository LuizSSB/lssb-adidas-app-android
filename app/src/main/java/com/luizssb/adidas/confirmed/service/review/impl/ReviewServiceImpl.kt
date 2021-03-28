package com.luizssb.adidas.confirmed.service.review.impl

import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.service.PaginationResult
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitReviewRESTAPI
import com.luizssb.adidas.confirmed.service.review.ReviewService
import com.luizssb.adidas.confirmed.utils.PageRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await

class ReviewServiceImpl(private val api: RetrofitReviewRESTAPI) : ReviewService {
    override suspend fun getReviews(productId: String, pageRef: PageRef): PaginationResult<Review> =
        withContext(Dispatchers.IO) {
            val reviews = api.getReviews(productId).await()
            PaginationResult(reviews, false)
        }

    override suspend fun addReview(productId: String, review: Review) = withContext(Dispatchers.IO) {
        api.addReview(productId, review).await()
    }
}
