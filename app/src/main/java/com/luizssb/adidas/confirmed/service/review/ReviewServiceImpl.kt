package com.luizssb.adidas.confirmed.service.review

import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitReviewRESTAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await

class ReviewServiceImpl(private val api: RetrofitReviewRESTAPI) : ReviewService {
    override suspend fun getReviews(productId: String): List<Review> = withContext(Dispatchers.IO) {
        val reviews = api.getReviews(productId).await()
        reviews
    }

    override suspend fun addReview(productId: String, review: Review) = withContext(Dispatchers.IO) {
        api.addReview(productId, review).await()
    }
}
