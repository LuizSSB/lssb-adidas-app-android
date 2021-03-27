package com.luizssb.adidas.confirmed.service.review

import com.luizssb.adidas.confirmed.dto.Review

interface ReviewService {
    suspend fun getReviews(productId: String): List<Review>
    suspend fun addReview(productId: String, review: Review)
}
