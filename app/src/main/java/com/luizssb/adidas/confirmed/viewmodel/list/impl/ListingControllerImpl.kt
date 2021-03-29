package com.luizssb.adidas.confirmed.viewmodel.list.impl

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
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
            Listing.Intent.Refresh -> runEffect(Listing.Effect.Refresh)

            Listing.Intent.Retry -> runEffect(Listing.Effect.Retry)

            is Listing.Intent.ChangeLoadState -> handleLoadStateChange(intent.state)
        }
    }

    private fun handleLoadStateChange(loadStates: CombinedLoadStates) {
        println("REFRESHING ${loadStates.refresh.loading}")
        if (loadStates.error == null) {
            setState { copy(refreshProblem = false) }
        } else {
            if (loadStates.error == loadStates.refresh.error) {
                setState { copy(refreshProblem = true) }
            }
        }


        setState(forceUpdate = false) {
            copy(
                    loadingRefresh = loadStates.refresh.loading,
                    loadingPrevious = loadStates.prepend.loading,
                    loadingMore = loadStates.append.loading
            )
        }
    }
}
