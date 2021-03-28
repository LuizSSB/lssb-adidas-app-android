package com.luizssb.adidas.confirmed.viewmodel.product

import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.viewmodel.DefaultBaseViewModel

abstract class ProductDetail private constructor() {
    data class State(
            val product: Product? = null,
            val loading: Boolean = false,
            val productNotFound: Boolean = false
    )

    abstract class Effect private constructor() {
        data class ShowError(val error: Throwable) : Effect()
    }

    abstract class Intent private constructor()

    abstract class ViewModel : DefaultBaseViewModel<State, Effect, Intent>(State())
}
