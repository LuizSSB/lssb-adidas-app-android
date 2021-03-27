package com.luizssb.adidas.confirmed.service

data class PaginationResult<T>(val data: List<T>, val hasMore: Boolean)
