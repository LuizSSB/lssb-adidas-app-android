package com.luizssb.adidas.confirmed.utils.extensions

import java.util.*

abstract class LocaleEx private constructor() {
    enum class LanguageCountrySeparator(val str: String) {
        DASH("-"),
        UNDERSCORE("_")
    }
    companion object {
        fun fromLanguageTag(tag: String) = tag
            .split(*LanguageCountrySeparator.values().map { it.str }.toTypedArray())
            .let {
                if (it.size == 2) Locale(it[0], it[1])
                else null
            }

        fun Locale.getLanguageTag(separator: LanguageCountrySeparator) = "$language${separator.str}$country"
    }
}
