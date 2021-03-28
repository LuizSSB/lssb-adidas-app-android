package com.luizssb.adidas.confirmed.viewmodel.product.impl

import androidx.lifecycle.viewModelScope
import com.luizssb.adidas.confirmed.repository.product.ProductRepository
import com.luizssb.adidas.confirmed.viewmodel.product.ProductDetail
import kotlinx.coroutines.launch

class ProductDetailViewModelImpl(
        private val repository: ProductRepository,
        private val productId: String
): ProductDetail.ViewModel() {
    override fun start() {
        setState { copy( loading = true ) }
        viewModelScope.launch {
            try {
                val product = repository.getProduct(productId)
                setState { copy(
                        product = product,
                        productNotFound = product == null
                ) }
            } catch (e: Throwable) {
                runEffect(ProductDetail.Effect.ShowError(e))
            } finally {
              setState { copy(loading = false) }
            }
        }
    }

    override fun handleIntent(intent: ProductDetail.Intent) {
        // no intent
    }
}
