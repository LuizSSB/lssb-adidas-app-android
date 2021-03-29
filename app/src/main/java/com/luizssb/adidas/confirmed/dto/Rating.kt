package com.luizssb.adidas.confirmed.dto

enum class Rating {
    SHIT,
    BAD,
    NEUTRAL,
    GOOD,
    EXCELLENT;

    val value get() = this.ordinal + 1

    companion object {
        fun from(number: Int) = values().let { it[number.coerceIn(MIN.value, MAX.value) - 1] }
        fun tryFrom(number: Int) = number
                .takeIf { it >= MIN.value && it <= MAX.value }
                ?.let { from(it) }

        val MAX = values().last()
        val MIN = values().first()
    }
}
