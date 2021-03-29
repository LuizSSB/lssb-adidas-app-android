package com.luizssb.adidas.confirmed.service.review.impl

import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.service.FakeFilterEx.Companion.fakePagination
import com.luizssb.adidas.confirmed.service.PaginationResult
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitProductRESTAPI
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitReviewRESTAPI
import com.luizssb.adidas.confirmed.service.retrofit.dto.RemoteReview.Companion.toRemoteType
import com.luizssb.adidas.confirmed.service.review.ReviewService
import com.luizssb.adidas.confirmed.utils.PageRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import kotlin.math.min

class ReviewServiceImpl(
        private val productAPI: RetrofitProductRESTAPI,
        private val reviewAPI: RetrofitReviewRESTAPI
) : ReviewService {
    override suspend fun getReviews(productId: String, pageRef: PageRef): PaginationResult<Review> =
        withContext(Dispatchers.IO) {
            // luizssb: gets the reviews from the product REST API, because the reviews from the
            // review API are all broken and insertion doest not seem to be working anyway.
//            val reviews = reviewAPI.getReviews(productId).await()
//                .map { it.toAppType() }

            val product = productAPI.getProduct(productId).await()
            val reviews = product.reviews
                    ?.fakePagination(pageRef)
                    ?.map { it.toAppType() }
                    ?: emptyList()
            PaginationResult(reviews, pageRef)
        }

    override suspend fun addReview(review: Review) = withContext(Dispatchers.IO) {
        reviewAPI.addReview(review.productId, review.toRemoteType()).await()
    }
}
