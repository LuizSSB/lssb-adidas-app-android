package com.luizssb.adidas.confirmed.viewmodel.review

import com.luizssb.adidas.confirmed.dto.Rating
import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.viewmodel.MVIViewModel
import com.luizssb.adidas.confirmed.viewmodel.list.Listing

abstract class ReviewList private constructor() {
    data class State(
            val rating: Rating = Rating.MAX,
            val reviewText: String = ""
    )

    abstract class Effect private constructor() {
        data class ShowError(val error: Throwable) : Effect()
    }

    abstract class Intent private constructor() {
        object AddReview : Intent()
    }

    abstract class ViewModel : MVIViewModel<State, Effect, Intent>(State()) {
        abstract val listingController: Listing.Controller<Review>
    }
}
