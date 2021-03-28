package com.luizssb.adidas.confirmed.viewmodel.product.impl

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.repository.product.ProductRepository
import com.luizssb.adidas.confirmed.viewmodel.list.List
import com.luizssb.adidas.confirmed.viewmodel.product.ProductList
import kotlinx.coroutines.flow.takeWhile

class ProductListViewModelImpl(
    private val repository: ProductRepository,
    override val listController: List.Controller<Product>
) : ProductList.ViewModel() {
    override fun start() {
        listenToProducts()
    }

    private fun listenToProducts(searchQuery: String? = null) {
        listController.updateEntries(
            repository.products(searchQuery)
                .cachedIn(viewModelScope)
                .takeWhile {
                    searchQuery == stateValue.searchQuery
                }
        )
    }

    override fun handleIntent(intent: ProductList.Intent) {
        when(intent) {
            is ProductList.Intent.ChangeSearchQuery -> intent.to.takeIf { it != stateValue.searchQuery }
                    ?.let {
                        setState { copy(searchQuery = it) }
                        listenToProducts(it)
                    }

            is ProductList.Intent.Select -> runEffect(ProductList.Effect.OpenProduct(intent.product))
        }
    }
}
