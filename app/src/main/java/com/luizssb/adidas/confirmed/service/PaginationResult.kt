package com.luizssb.adidas.confirmed.service

import com.luizssb.adidas.confirmed.utils.PageRef

data class PaginationResult<T>(val data: List<T>, val hasMore: Boolean) {
    constructor(data: List<T>, pageRef: PageRef) : this(data, data.size >= pageRef.limit)
}
