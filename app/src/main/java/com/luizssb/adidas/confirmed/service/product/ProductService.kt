package com.luizssb.adidas.confirmed.service.product

import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.service.PaginationResult
import com.luizssb.adidas.confirmed.utils.PageRef

interface ProductService {
    suspend fun getProducts(searchQuery: String?, pageRef: PageRef): PaginationResult<Product>
    suspend fun getProduct(productId: String): Product?
}
