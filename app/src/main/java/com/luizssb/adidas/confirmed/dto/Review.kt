package com.luizssb.adidas.confirmed.dto

data class Review(
        val productId: String,
        val locale: String, // TODO lbaglie: change to Locale object
        val rating: Int, // TODO lbaglie: change to enum?
        val text: String
)
