package com.luizssb.adidas.confirmed.dto

data class User(
    val photo: String,
    val username: String,
    val age: Int,
    val gender: String,
    val sexualOrientation: String,
    val city: String,
) {
    companion object {
        val PLACEHOLDER = User(
            "",
            "",
            0,
            "",
            "",
            ""
        )
    }
}
