package com.luizssb.adidas.confirmed.viewmodel.product

import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.viewmodel.MVIViewModel
import com.luizssb.adidas.confirmed.viewmodel.list.List

abstract class ProductList private constructor() {
    data class State(
            val searchQuery: String? = null
    )

    abstract class Effect private constructor() {
        data class OpenProduct(val product: Product) : Effect()
    }

    abstract class Intent private constructor() {
        data class ChangeSearchQuery(val to: String?) : Intent()
        data class Select(val product: Product) : Intent()
    }

    abstract class ViewModel : MVIViewModel<State, Effect, Intent>(State()) {
        abstract val listController: List.Controller<Product>
    }
}
