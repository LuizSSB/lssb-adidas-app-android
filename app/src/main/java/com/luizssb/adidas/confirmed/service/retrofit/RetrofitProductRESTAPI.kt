package com.luizssb.adidas.confirmed.service.retrofit

import com.luizssb.adidas.confirmed.BuildConfig
import com.luizssb.adidas.confirmed.dto.Product
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitProductRESTAPI {
    @GET("/product")
    fun getProducts(): Call<List<Product>>

    companion object {
        val default: RetrofitProductRESTAPI by lazy {
            RetrofitClientFactory(BuildConfig.SERVICE_URL_PRODUCT)
                    .produce()
                    .create(RetrofitProductRESTAPI::class.java)
        }
    }
}
