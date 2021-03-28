package com.luizssb.adidas.confirmed.viewmodel.review.impl

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.cachedIn
import com.luizssb.adidas.confirmed.repository.review.ReviewRepository
import com.luizssb.adidas.confirmed.utils.extensions.CombinedLoadStatesEx.Companion.error
import com.luizssb.adidas.confirmed.utils.extensions.LoadStateEx.Companion.loading
import com.luizssb.adidas.confirmed.viewmodel.review.ReviewList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReviewListViewModelImpl(
        private val repository: ReviewRepository,
        private val productId: String
) : ReviewList.ViewModel() {
    override fun start() {
        viewModelScope.launch {
            repository.reviews(productId)
                    .cachedIn(viewModelScope)
                    .collectLatest {
                        setState { copy(reviews = it) }
                    }
        }
    }

    override fun handleIntent(intent: ReviewList.Intent) {
        when(intent) {
            is ReviewList.Intent.ChangeLoadState -> handleLoadStateChange(intent.state)

            ReviewList.Intent.Refresh -> runEffect(ReviewList.Effect.Refresh)

            ReviewList.Intent.AddReview -> {
                // TODO
            }
        }
    }

    private fun handleLoadStateChange(loadStates: CombinedLoadStates) {
        loadStates.error?.let {
            runEffect(ReviewList.Effect.ShowError(it))
        }

        setState(forceUpdate = false) { copy(
                loadingRefresh = loadStates.refresh.loading,
                loadingPrevious = loadStates.prepend.loading,
                loadingMore = loadStates.append.loading
        ) }
    }
}
