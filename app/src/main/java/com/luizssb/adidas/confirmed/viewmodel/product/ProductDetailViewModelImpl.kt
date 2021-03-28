package com.luizssb.adidas.confirmed.viewmodel.product

import androidx.lifecycle.viewModelScope
import com.luizssb.adidas.confirmed.repository.product.ProductRepository
import kotlinx.coroutines.launch

class ProductDetailViewModelImpl(
        private val repository: ProductRepository,
        private val productId: String
): ProductDetail.ViewModel() {
    override fun start() {
        setState { copy(
                loading = true
        ) }
        viewModelScope.launch {
            try {
                val product = repository.getProduct(productId)
                if (product == null) {
                    setState { copy(productNotFound = true) }
                } else {
                    setState { copy(product = product) }
                }
            } catch (e: Throwable) {
                runEffect(ProductDetail.Effect.ShowError(e))
            } finally {
              setState { copy(loading = true) }
            }
        }
    }

    override fun handleIntent(intent: ProductDetail.Intent) {
        // no intent
    }
}
