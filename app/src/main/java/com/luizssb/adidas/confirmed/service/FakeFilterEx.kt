package com.luizssb.adidas.confirmed.service

import com.luizssb.adidas.confirmed.utils.PageRef
import kotlin.math.min

abstract class FakeFilterEx private constructor() {
    companion object {
        // luizssb: as the services do not provide paging, I thought it would be cool to fake it
        // to we can have some loading action
        fun <T> List<T>.fakePagination(pageRef: PageRef) = this
                .takeIf { it.size > pageRef.skip}
                ?.run { subList(pageRef.skip, min(pageRef.upperBound, size)) }
                ?: emptyList()
    }
}
