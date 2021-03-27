package com.luizssb.adidas.confirmed.utils

import androidx.paging.LoadState

class LoadStateEx private constructor() {
    companion object {
        val LoadState.loading get() = this is LoadState.Loading
        val LoadState.error: Throwable? get() = (this as? LoadState.Error)?.error
    }
}
