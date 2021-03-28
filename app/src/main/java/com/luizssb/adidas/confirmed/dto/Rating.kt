package com.luizssb.adidas.confirmed.dto

enum class Rating {
    SHIT,
    BAD,
    NEUTRAL,
    GOOD,
    EXCELLENT;

    val value get() = this.ordinal + 1

    companion object {
        fun from(number: Int) = values().let { it[number.coerceIn(1, it.size)] }

        val MAX = values().last()
        val MIN = values().first()
    }
}
