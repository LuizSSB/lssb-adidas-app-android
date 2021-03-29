package com.luizssb.adidas.confirmed.viewmodel.review.impl

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.repository.review.ReviewRepository
import com.luizssb.adidas.confirmed.viewmodel.list.Listing
import com.luizssb.adidas.confirmed.viewmodel.review.ReviewList

class ReviewListViewModelImpl(
        private val repository: ReviewRepository,
        override val listingController: Listing.Controller<Review>,
        private val productId: String
) : ReviewList.ViewModel() {
    override fun start() {
        listingController.updateEntries(
                repository.reviews(productId)
                        .cachedIn(viewModelScope)
        )
    }

    override fun handleIntent(intent: ReviewList.Intent) {
        when(intent) {
            ReviewList.Intent.AddReview -> {
                // TODO
            }
        }
    }
}
