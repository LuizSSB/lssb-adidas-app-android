package com.luizssb.adidas.confirmed.service.retrofit

import com.luizssb.adidas.confirmed.BuildConfig
import com.luizssb.adidas.confirmed.service.retrofit.dto.RemoteProduct
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitProductRESTAPI {
    @GET("/product")
    fun getProducts(): Call<List<RemoteProduct>>

    @GET("/product/{id}")
    fun getProduct(@Path("id") id: String): Call<RemoteProduct>

    companion object {
        val default: RetrofitProductRESTAPI by lazy {
            RetrofitClientFactory(BuildConfig.SERVICE_URL_PRODUCT)
                    .produce()
                    .create(RetrofitProductRESTAPI::class.java)
        }
    }
}
