package com.luizssb.adidas.confirmed.viewmodel.product

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.luizssb.adidas.confirmed.repository.product.ProductRepository
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
            Intent.StartedLoadingFirsts -> {}
            Intent.StartedLoadingNext -> {}
            Intent.StartedLoadingPrevious -> {}
            Intent.FinishedLoading -> {}
            is Intent.FailedToLoad -> {
                effects.value = Effect.ShowError(intent.error)
            }
            Intent.Refresh -> {

            }
            is Intent.Select -> {
               // TODO lbaglie: navigate to product page
            }
        }
    }
}
