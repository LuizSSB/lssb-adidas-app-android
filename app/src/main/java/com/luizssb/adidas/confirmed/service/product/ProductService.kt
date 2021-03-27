package com.luizssb.adidas.confirmed.service.product

import com.luizssb.adidas.confirmed.dto.Product

interface ProductService {
    suspend fun getProducts(): List<Product>
}
