package com.luizssb.adidas.confirmed.viewmodel.product

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.cachedIn
import com.luizssb.adidas.confirmed.repository.product.ProductRepository
import com.luizssb.adidas.confirmed.utils.extensions.CombinedLoadStatesEx.Companion.error
import com.luizssb.adidas.confirmed.utils.extensions.LoadStateEx.Companion.loading
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

class ProductListViewModelImpl(
    private val repository: ProductRepository
) : ProductListViewModel() {
    override fun start() {
        listenToProducts()
    }

    private fun listenToProducts(searchQuery: String? = null) {
        viewModelScope.launch {
            repository.products(searchQuery)
                    .cachedIn(viewModelScope)
                    .takeWhile {
                        searchQuery == this@ProductListViewModelImpl.stateValue.searchQuery
                    }
                    .collectLatest {
                        setState { copy(products = it) }
                    }
        }
    }

    override fun handleIntent(intent: Intent) {
        when(intent) {
            is Intent.ChangeSearchQuery -> intent.to.takeIf { it != stateValue.searchQuery }
                    .let {
                        setState { copy(searchQuery = it) }
                        listenToProducts(it)
                    }

            is Intent.ChangeLoadState -> handleLoadStateChange(intent.state)

            Intent.Refresh -> setEffect(Effect.Refresh)

            is Intent.Select -> setEffect(Effect.OpenProduct(intent.product))
        }
    }

    private fun handleLoadStateChange(loadStates: CombinedLoadStates) {
        loadStates.error?.let {
            setEffect(Effect.ShowError(it))
        }

        setState(forceUpdate = false) { copy(
                loadingRefresh = loadStates.refresh.loading,
                loadingPrevious = loadStates.prepend.loading,
                loadingMore = loadStates.append.loading
        ) }
    }
}
