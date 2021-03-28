package com.luizssb.adidas.confirmed.utils.extensions

import android.content.Context
import com.luizssb.adidas.confirmed.R
import com.luizssb.adidas.confirmed.dto.Product

abstract class ProductEx private constructor() {
    companion object {
        fun Product.getCompleteName(context: Context) = context.getString(
                R.string.template_product_complete_name,
                id,
                name
        )
    }
}
