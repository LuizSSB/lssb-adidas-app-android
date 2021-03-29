@file:Suppress("MemberVisibilityCanBePrivate")

package com.luizssb.adidas.confirmed.service.retrofit.dto

import com.luizssb.adidas.confirmed.dto.Product
import java.io.Serializable

class RemoteProduct (
        val id: String,
        val name: String,
        val description: String,
        val imgUrl: String,
        val price: Float,
        val currency: String,
        val reviews: List<RemoteReview>? = null
) : Serializable {
    fun toAppType() = Product(id,
            name,
            description,
            imgUrl,

            // luizssb: products in the sample database do not seem to have price nor currency,
            // so I'm faking these also.
            (id.hashCode() / 1e6).toFloat(),
            currency.takeIf { it.isNotBlank() } ?: "EUR"
    )

    // luizssb: there is an is an entry in the sample database that, for some
    // reason, has all null data. I'm assumming this is not on purpose, therefore
    // to avoid having to implement some extra structures to handle this single
    // problematic case, I'm just cutting it off like this.
    @Suppress("SENSELESS_COMPARISON")
    val isValid get() = id != null
}
