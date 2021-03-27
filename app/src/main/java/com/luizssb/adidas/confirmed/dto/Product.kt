package com.luizssb.adidas.confirmed.dto

data class Product(
        val id: String,
        val name: String,
        val description: String,
        val imgUrl: String,
        val price: Float,
        val currency: String
        // lbaglie: although reviews are returned by the API, let us assume they do not,
        // so as to imagine a more performant data exchange.
)
