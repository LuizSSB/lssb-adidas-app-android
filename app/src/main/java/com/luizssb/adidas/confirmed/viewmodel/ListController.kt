package com.luizssb.adidas.confirmed.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.utils.extensions.CombinedLoadStatesEx.Companion.error
import com.luizssb.adidas.confirmed.utils.extensions.LoadStateEx.Companion.loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ListController<T : Any> : DefaultBaseViewModel<ListController.State<T>, ListController.Effect, ListController.Intent>(State()) {
    data class State<T : Any>(
        val entries: PagingData<T> = PagingData.empty(),
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
    }

    fun updateEntries(flow: Flow<PagingData<T>>) {
        viewModelScope.launch {
            flow.collectLatest {
                setState { copy(entries = it) }
            }
        }
    }


    override fun handleIntent(intent: Intent) {
        when(intent) {
            is Intent.ChangeLoadState -> handleLoadStateChange(intent.state)

            Intent.Refresh -> runEffect(Effect.Refresh)
        }
    }

    private fun handleLoadStateChange(loadStates: CombinedLoadStates) {
        loadStates.error?.let {
            runEffect(Effect.ShowError(it))
        }

        setState(forceUpdate = false) {
            copy(
                loadingRefresh = loadStates.refresh.loading,
                loadingPrevious = loadStates.prepend.loading,
                loadingMore = loadStates.append.loading
            )
        }
    }

    override fun start() {
        TODO("Not yet implemented")
    }
}
