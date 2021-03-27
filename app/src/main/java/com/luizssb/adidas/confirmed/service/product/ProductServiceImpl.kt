package com.luizssb.adidas.confirmed.service.product

import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitProductRESTAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await

class ProductServiceImpl(private val api: RetrofitProductRESTAPI) : ProductService {
    override suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        val products = api.getProducts().await()
        products
    }
}
