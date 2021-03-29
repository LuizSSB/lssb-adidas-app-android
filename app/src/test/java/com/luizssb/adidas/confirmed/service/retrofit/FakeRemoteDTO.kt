package com.luizssb.adidas.confirmed.service.retrofit

import com.luizssb.adidas.confirmed.service.retrofit.dto.RemoteProduct
import com.luizssb.adidas.confirmed.service.retrofit.dto.RemoteReview

abstract class FakeRemoteDTO private constructor() {
    companion object {
        fun product(id: String = "id") = RemoteProduct(
            id,
            "name",
            "description",
            "image",
            1.23f,
            "EUR",
            reviews = listOf(
                review(id),
                review(id)
            )
        )

        fun RemoteProduct.review() = review(id)

        fun review(productId: String = "id") = RemoteReview(
            productId,
            3,
            "text",
            "en-us"
        )
    }
}
