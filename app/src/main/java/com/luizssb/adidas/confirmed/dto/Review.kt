package com.luizssb.adidas.confirmed.dto

import java.io.Serializable
import java.util.*

data class Review(
        val productId: String,
        val rating: Rating,
        val text: String,
        val locale: Locale? = Locale.getDefault(),
) : Serializable {
    companion object {
        val NIL = Review("", Rating.MIN, "text", null)
    }
}
