package com.luizssb.adidas.confirmed.viewmodel.product.impl

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.cachedIn
import com.luizssb.adidas.confirmed.repository.product.ProductRepository
import com.luizssb.adidas.confirmed.utils.extensions.CombinedLoadStatesEx.Companion.error
import com.luizssb.adidas.confirmed.utils.extensions.LoadStateEx.Companion.loading
import com.luizssb.adidas.confirmed.viewmodel.product.ProductList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

class ProductListViewModelImpl(
    private val repository: ProductRepository
) : ProductList.ViewModel() {
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

    override fun handleIntent(intent: ProductList.Intent) {
        when(intent) {
            is ProductList.Intent.ChangeSearchQuery -> intent.to.takeIf { it != stateValue.searchQuery }
                    ?.let {
                        setState { copy(searchQuery = it) }
                        listenToProducts(it)
                    }

            is ProductList.Intent.ChangeLoadState -> handleLoadStateChange(intent.state)

            ProductList.Intent.Refresh -> runEffect(ProductList.Effect.Refresh)

            is ProductList.Intent.Select -> runEffect(ProductList.Effect.OpenProduct(intent.product))
        }
    }

    private fun handleLoadStateChange(loadStates: CombinedLoadStates) {
        loadStates.error?.let {
            runEffect(ProductList.Effect.ShowError(it))
        }

        setState(forceUpdate = false) { copy(
                loadingRefresh = loadStates.refresh.loading,
                loadingPrevious = loadStates.prepend.loading,
                loadingMore = loadStates.append.loading
        ) }
    }
}
