package com.luizssb.adidas.confirmed.repository.paging

import androidx.paging.PagingConfig

class PagingConfigEx {
    companion object {
        val DEFAULT_PAGING_CONFIG = PagingConfig(pageSize = 20, initialLoadSize = 20)
    }
}
