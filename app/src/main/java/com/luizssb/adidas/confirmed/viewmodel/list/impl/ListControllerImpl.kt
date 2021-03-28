package com.luizssb.adidas.confirmed.viewmodel.list.impl

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.utils.extensions.CombinedLoadStatesEx.Companion.error
import com.luizssb.adidas.confirmed.utils.extensions.LoadStateEx.Companion.loading
import com.luizssb.adidas.confirmed.viewmodel.list.List
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListControllerImpl<T : Any> : List.Controller<T>() {
    override fun updateEntries(flow: Flow<PagingData<T>>) {
        viewModelScope.launch {
            flow.collectLatest {
                setState { copy(entries = it) }
            }
        }
    }


    override fun handleIntent(intent: List.Intent) {
        when(intent) {
            is List.Intent.ChangeLoadState -> handleLoadStateChange(intent.state)

            List.Intent.Refresh -> runEffect(List.Effect.Refresh)
        }
    }

    private fun handleLoadStateChange(loadStates: CombinedLoadStates) {
        loadStates.error?.let {
            runEffect(List.Effect.ShowError(it))
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
