package com.luizssb.adidas.confirmed.viewmodel.review

import com.luizssb.adidas.confirmed.dto.Rating
import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.viewmodel.MVIViewModel
import com.luizssb.adidas.confirmed.viewmodel.list.Listing

abstract class Reviews private constructor() {
    data class State(
            val composingReview: Boolean = false,
            val sendingReview: Boolean = false,
    )

    abstract class Effect private constructor() {
        data class ShowError(val error: Throwable) : Effect()
        object ResetCompose : Effect()
        object WarnLackingData : Effect()
    }

    abstract class Intent private constructor() {
        object ComposeReview : Intent()
        object CancelComposing : Intent()
        data class SendReview(val rating: Rating?, val text: String?) : Intent()
    }

    abstract class ViewModel : MVIViewModel<State, Effect, Intent>(State()) {
        abstract val listingController: Listing.Controller<Review>
    }
}
