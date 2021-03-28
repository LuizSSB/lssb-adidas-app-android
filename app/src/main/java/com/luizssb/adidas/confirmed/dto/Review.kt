package com.luizssb.adidas.confirmed.dto

import java.io.Serializable
import java.util.*

data class Review(
    val productId: String,
    val locale: Locale?,
    val rating: Rating,
    val text: String
) : Serializable {
    companion object {
        val NIL = Review("", null, Rating.MIN, "text")
    }
}
