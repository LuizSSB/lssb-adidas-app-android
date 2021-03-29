package com.luizssb.adidas.confirmed.dto

import com.luizssb.adidas.confirmed.service.retrofit.dto.RemoteProduct
import java.util.*

abstract class FakeDTO private constructor() {
    companion object {
        fun product(id: String = "id") = Product(
            id,
            "name",
            "description",
            "image",
            1.23f,
            "EUR"
        )

        fun RemoteProduct.review() = review(id)

        fun review(productId: String = "id") = Review(
            productId,
            Rating.NEUTRAL,
            "text",
            Locale.getDefault()
        )
    }
}
