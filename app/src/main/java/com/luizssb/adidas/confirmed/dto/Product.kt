package com.luizssb.adidas.confirmed.dto

import java.io.Serializable
import java.text.NumberFormat
import java.util.*

data class Product(
        val id: String,
        val name: String,
        val description: String,
        val imgUrl: String,
        val price: Float,
        val currency: String
        // luizssb: although reviews are returned by the API, let us assume they do not,
        // so as to imagine a more performant data exchange.
) : Serializable {
    companion object {
        val NIL = Product(
                id = "",
                name = "",
                description = "",
                imgUrl = "",
                price = 0f,
                currency = ""
        )
    }

    val priceString get() = NumberFormat.getCurrencyInstance(Locale.getDefault())
            .also { it.currency = Currency.getInstance(currency) }
            .format(price)
}
