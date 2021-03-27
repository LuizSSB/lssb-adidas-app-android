package com.luizssb.adidas.confirmed.repository.review

import androidx.paging.PagingSource
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.dto.Review

abstract class ReviewPagingSource : PagingSource<Int, Review>() {
    interface Factory {
        operator fun invoke(productId: String): ReviewPagingSource
    }
}
