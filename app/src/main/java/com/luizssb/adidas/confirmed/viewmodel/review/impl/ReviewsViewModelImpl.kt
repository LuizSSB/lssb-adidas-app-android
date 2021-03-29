package com.luizssb.adidas.confirmed.viewmodel.review.impl

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.luizssb.adidas.confirmed.dto.Rating
import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.repository.review.ReviewRepository
import com.luizssb.adidas.confirmed.viewmodel.MVIControllerProperty
import com.luizssb.adidas.confirmed.viewmodel.list.Listing
import com.luizssb.adidas.confirmed.viewmodel.review.Reviews
import kotlinx.coroutines.launch

class ReviewsViewModelImpl(
    private val repository: ReviewRepository,
    @get:MVIControllerProperty override val listingController: Listing.Controller<Review>,
    private val productId: String
) : Reviews.ViewModel() {
    override fun start() {
        listingController.updateEntries(
                repository.reviews(productId)
                        .cachedIn(viewModelScope)
        )
    }

    private fun listenToReviews() {
        listingController.updateEntries(
                repository.reviews(productId)
                        .cachedIn(viewModelScope)
        )
    }

    override fun handleIntent(intent: Reviews.Intent) {
        when(intent) {
            Reviews.Intent.ComposeReview ->
                setState { copy(composingReview = true) }

            is Reviews.Intent.CancelComposing -> {
                setState { copy(composingReview = false) }
                runEffect(Reviews.Effect.ResetCompose)
            }

            is Reviews.Intent.SendReview -> {
                val (rating, text) = intent
                if (rating == null || text.isNullOrEmpty()) {
                    runEffect(Reviews.Effect.WarnLackingData)
                    return
                }

                sendReview(rating, text)
            }
        }
    }

    private fun sendReview(rating: Rating, text: String) {
        viewModelScope.launch {
            setState { copy(sendingReview = true) }
            try {
                val review = Review(productId, rating, text)
                repository.addReview(review)
                setState { copy(
                        sendingReview = false,
                        composingReview = false
                ) }
                runEffect(Reviews.Effect.ResetCompose)
                listenToReviews()
            } catch (ex: Throwable) {
                runEffect(Reviews.Effect.ShowError(ex))
                setState { copy(sendingReview = false) }
            }
        }
    }
}
