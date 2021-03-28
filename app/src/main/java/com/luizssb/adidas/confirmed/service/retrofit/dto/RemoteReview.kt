package com.luizssb.adidas.confirmed.service.retrofit.dto

import com.luizssb.adidas.confirmed.dto.Rating
import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.utils.extensions.LocaleEx
import com.luizssb.adidas.confirmed.utils.extensions.LocaleEx.Companion.getLanguageTag
import java.io.Serializable

// luizssb: technically, I could provide a custom converter for retrofit that would take care of
// correctly deserializing objects such as this. However, Retrofit's documentation on the subject
// is severely lacking, and, in the end, what I do here is what I'd do in that case.
data class RemoteReview(
    val productId: String,
    val locale: String,
    val rating: Int,
    val text: String
) : Serializable {
    fun toAppType() = Review(
        productId,
        LocaleEx.fromLanguageTag(locale),
        Rating.from(rating),
        text
    )

    companion object {
        fun Review.toRemoteType() = RemoteReview(
            productId,
            locale?.getLanguageTag(LocaleEx.LanguageCountrySeparator.DASH) ?: "",
            rating.value,
            text
        )
    }
}
