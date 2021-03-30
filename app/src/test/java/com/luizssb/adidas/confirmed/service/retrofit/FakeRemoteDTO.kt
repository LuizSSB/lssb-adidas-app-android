package com.luizssb.adidas.confirmed.service.retrofit

import com.luizssb.adidas.confirmed.service.retrofit.dto.RemoteProduct
import com.luizssb.adidas.confirmed.service.retrofit.dto.RemoteReview

abstract class FakeRemoteDTO private constructor() {
    companion object {
        fun remoteProduct(id: String = "id") = RemoteProduct(
            id,
            "name",
            "description",
            "image",
            1.23f,
            "EUR",
            reviews = listOf(
                remoteReview(id),
                remoteReview(id)
            )
        )

        fun RemoteProduct.remoteReview() = remoteReview(id)

        fun remoteReview(productId: String = "id") = RemoteReview(
            productId,
            3,
            "text",
            "en-us"
        )
    }
}
