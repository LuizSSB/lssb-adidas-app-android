package com.luizssb.adidas.confirmed.service.review

import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.service.PaginationResult
import com.luizssb.adidas.confirmed.utils.PageRef

interface ReviewService {
    suspend fun getReviews(productId: String, pageRef: PageRef): PaginationResult<Review>
    suspend fun addReview(review: Review)
}
