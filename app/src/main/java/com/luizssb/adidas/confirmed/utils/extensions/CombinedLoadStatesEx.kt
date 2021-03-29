package com.luizssb.adidas.confirmed.utils.extensions

import androidx.paging.CombinedLoadStates
import com.luizssb.adidas.confirmed.utils.extensions.LoadStateEx.Companion.error

class CombinedLoadStatesEx private constructor() {
    companion object {
        val CombinedLoadStates.error get() = refresh.error ?: append.error ?: prepend.error
    }
}
