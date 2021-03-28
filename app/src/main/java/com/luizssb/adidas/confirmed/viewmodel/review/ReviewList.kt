package com.luizssb.adidas.confirmed.viewmodel.review

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.viewmodel.DefaultBaseViewModel

abstract class ReviewList private constructor() {
    data class State(
            val reviews: PagingData<Review> = PagingData.empty(),
            val loadingPrevious: Boolean = false,
            val loadingMore: Boolean = false,
            val loadingRefresh: Boolean = false
    )

    abstract class Effect private constructor() {
        data class ShowError(val error: Throwable) : Effect()
        object Refresh : Effect()
    }

    abstract class Intent private constructor() {
        data class ChangeLoadState(val state: CombinedLoadStates) : Intent()
        object Refresh : Intent()
        object AddReview : Intent()
    }

    abstract class ViewModel : DefaultBaseViewModel<State, Effect, Intent>(State())
}
