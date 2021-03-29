package com.luizssb.adidas.confirmed.viewmodel.list

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.viewmodel.DefaultMVIController
import kotlinx.coroutines.flow.Flow

class Listing {
    enum class ContentType {
        Listing,
        RefreshProblem,
        Empty
    }

    data class State<T : Any>(
        val entries: PagingData<T> = PagingData.empty(),
        val loadingPrevious: Boolean = false,
        val loadingMore: Boolean = false,
        val loadingRefresh: Boolean = false,
        val contentType: ContentType = ContentType.Listing
    )

    abstract class Effect private constructor() {
        data class ShowError(val error: Throwable) : Effect()
        object Refresh : Effect()
        object Retry : Effect()
    }

    abstract class Intent private constructor() {
        data class ChangeLoadState(val state: CombinedLoadStates, val itemCount: Int) : Intent()
        object Refresh : Intent()
        object Retry : Intent()
    }

    abstract class Controller<T : Any> : DefaultMVIController<State<T>, Effect, Intent>(State()) {
        internal abstract fun updateEntries(flow: Flow<PagingData<T>>)
    }
}
