package com.luizssb.adidas.confirmed.viewmodel.review

import com.luizssb.adidas.confirmed.repository.review.ReviewRepository

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
