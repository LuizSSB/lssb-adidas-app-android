package com.luizssb.adidas.confirmed.viewmodel.product

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.cachedIn
import com.luizssb.adidas.confirmed.repository.product.ProductRepository
import com.luizssb.adidas.confirmed.utils.CombinedLoadStatesEx.Companion.error
import com.luizssb.adidas.confirmed.utils.LoadStateEx.Companion.loading
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductListViewModelImpl(
    private val repository: ProductRepository
) : ProductListViewModel() {
    override fun start() {
        viewModelScope.launch {
            repository.products()
                .cachedIn(viewModelScope)
                .collectLatest {
                    state.value = State(products = it)
                }
        }
    }

    override fun handleIntent(intent: Intent) {
        when(intent) {
            is Intent.ChangeLoadState -> handleLoadStateChange(intent.state)
            Intent.Refresh -> { }
            is Intent.Select -> {
               // TODO lbaglie: navigate to product page
            }
        }
    }

    private fun handleLoadStateChange(loadStates: CombinedLoadStates) {
        loadStates.error?.let {
            effects.value = Effect.ShowError(it)
        }

        stateValue
            .copy(
                loadingRefresh = loadStates.refresh.loading,
                loadingPrevious = loadStates.prepend.loading,
                loadingMore = loadStates.append.loading
            )
            .takeIf { it != stateValue }
            ?.let { state.value = it }
    }
}
