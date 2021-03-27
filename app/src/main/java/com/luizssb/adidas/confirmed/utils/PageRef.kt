package com.luizssb.adidas.confirmed.utils

data class PageRef(val skip: Int, val limit: Int) {
    val upperBound get() = skip + limit
}
