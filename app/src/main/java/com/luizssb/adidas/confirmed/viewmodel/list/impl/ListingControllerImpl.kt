package com.luizssb.adidas.confirmed.viewmodel.list.impl

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.utils.extensions.CombinedLoadStatesEx.Companion.error
import com.luizssb.adidas.confirmed.utils.extensions.LoadStateEx.Companion.error
import com.luizssb.adidas.confirmed.utils.extensions.LoadStateEx.Companion.loading
import com.luizssb.adidas.confirmed.viewmodel.list.Listing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListingControllerImpl<T : Any> : Listing.Controller<T>() {
    override fun updateEntries(flow: Flow<PagingData<T>>) {
        viewModelScope.launch {
            flow.collectLatest {
                setState { copy(entries = it) }
            }
        }
    }

    override fun handleIntent(intent: Listing.Intent) {
        when(intent) {
            is Listing.Intent.ChangeLoadState -> handleLoadStateChange(intent.state, intent.itemCount)
            Listing.Intent.Refresh -> runEffect(Listing.Effect.Refresh)
            Listing.Intent.Retry -> runEffect(Listing.Effect.Retry)
        }
    }

    private fun handleLoadStateChange(loadStates: CombinedLoadStates, itemCount: Int) {
        with(loadStates) {
            setState(forceUpdate = false) { copy(
                contentType = when {
                    error?.let { it == refresh.error } == true -> Listing.ContentType.RefreshProblem
                    refresh is LoadState.NotLoading && itemCount == 0 -> Listing.ContentType.Empty
                    else -> Listing.ContentType.Listing
                },
                loadingRefresh = refresh.loading,
                loadingPrevious = prepend.loading,
                loadingMore = append.loading
            ) }
        }
    }
}
