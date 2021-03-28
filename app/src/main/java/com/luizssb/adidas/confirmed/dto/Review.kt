package com.luizssb.adidas.confirmed.dto

import java.io.Serializable

data class Review(
        val productId: String,
        val locale: String, // TODO luizssb: change to Locale object
        val rating: Int, // TODO luizssb: change to enum?
        val text: String
) : Serializable
