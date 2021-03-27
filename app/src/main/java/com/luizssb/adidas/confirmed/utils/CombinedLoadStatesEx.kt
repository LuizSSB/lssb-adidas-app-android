package com.luizssb.adidas.confirmed.utils

import androidx.paging.CombinedLoadStates
import com.luizssb.adidas.confirmed.utils.LoadStateEx.Companion.error

class CombinedLoadStatesEx private constructor() {
    companion object {
        val CombinedLoadStates.error get() = append.error ?: prepend.error ?: refresh.error
    }
}
