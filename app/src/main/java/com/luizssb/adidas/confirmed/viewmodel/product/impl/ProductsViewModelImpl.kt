package com.luizssb.adidas.confirmed.viewmodel.product.impl

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.repository.product.ProductRepository
import com.luizssb.adidas.confirmed.viewmodel.list.Listing
import com.luizssb.adidas.confirmed.viewmodel.product.Products
import kotlinx.coroutines.flow.takeWhile

class ProductsViewModelImpl(
    private val repository: ProductRepository,
    override val listingController: Listing.Controller<Product>
) : Products.ViewModel() {
    override fun start() {
        listenToProducts()
    }

    private fun listenToProducts(searchQuery: String? = null) {
        listingController.updateEntries(
            repository.products(searchQuery)
                .cachedIn(viewModelScope)
                .takeWhile {
                    searchQuery == stateValue.searchQuery
                }
        )
    }

    override fun handleIntent(intent: Products.Intent) {
        when(intent) {
            is Products.Intent.ChangeSearchQuery -> intent.to.takeIf { it != stateValue.searchQuery }
                    ?.let {
                        setState { copy(searchQuery = it) }
                        listenToProducts(it)
                    }

            is Products.Intent.Select -> runEffect(Products.Effect.OpenProduct(intent.product))
        }
    }
}
