package com.luizssb.adidas.confirmed.service.review.impl

import com.luizssb.adidas.confirmed.dto.Rating
import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.service.PaginationResult
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitReviewRESTAPI
import com.luizssb.adidas.confirmed.service.retrofit.dto.RemoteReview.Companion.toRemoteType
import com.luizssb.adidas.confirmed.service.review.ReviewService
import com.luizssb.adidas.confirmed.utils.PageRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.util.*

class ReviewServiceImpl(private val api: RetrofitReviewRESTAPI) : ReviewService {
    override suspend fun getReviews(productId: String, pageRef: PageRef): PaginationResult<Review> =
        withContext(Dispatchers.IO) {
//            val reviews = api.getReviews(productId).await()
//                .map { it.toAppType() }

            val reviews = mutableListOf<Review>()
            repeat(40) {
                reviews.add(Review(
                        "FI444",
                        Rating.NEUTRAL,
                        "MANO MTO FOOOODA",
                        Locale.getDefault(),
                ))
            }

            PaginationResult(reviews, false)
        }

    override suspend fun addReview(review: Review) = withContext(Dispatchers.IO) {
        api.addReview(review.productId, review.toRemoteType()).await()
    }
}
