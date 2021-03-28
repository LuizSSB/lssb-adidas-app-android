package com.luizssb.adidas.confirmed.viewmodel.product

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.viewmodel.DefaultBaseViewModel

abstract class ProductListViewModel
    : DefaultBaseViewModel<
        ProductListViewModel.State,
        ProductListViewModel.Effect,
        ProductListViewModel.Intent
        >(State()) {

    data class State(
        val products: PagingData<Product> = PagingData.empty(),
        val searchQuery: String? = null,
        val loadingPrevious: Boolean = false,
        val loadingMore: Boolean = false,
        val loadingRefresh: Boolean = false
    )

    abstract class Effect private constructor () {
        data class ShowError(val error: Throwable) : Effect()
        object Refresh : Effect()
        data class OpenProduct(val product: Product) : Effect()
    }

    abstract class Intent private constructor () {
        data class ChangeSearchQuery(val to: String?) : Intent()
        data class ChangeLoadState(val state: CombinedLoadStates) : Intent()
        object Refresh : Intent()
        data class Select(val product: Product) : Intent()
    }
}
