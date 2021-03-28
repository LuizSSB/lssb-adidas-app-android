package com.luizssb.adidas.confirmed.viewmodel.review.impl

import com.luizssb.adidas.confirmed.repository.review.ReviewRepository
import com.luizssb.adidas.confirmed.viewmodel.review.ReviewList

class ReviewListViewModelImpl(
        private val repository: ReviewRepository,
        private val productId: String
) : ReviewList.ViewModel() {
    override fun start() {
        TODO("Not yet implemented")
    }

    override fun handleIntent(intent: ReviewList.Intent) {
        TODO("Not yet implemented")
    }
}
