package com.luizssb.adidas.confirmed.service.retrofit

import com.luizssb.adidas.confirmed.dto.User
import retrofit2.http.GET
import retrofit2.Call

interface RetrofitRESTAPI {
    @GET("/mobiletest.json/")
    fun getPeople(): Call<List<User>>

    companion object {
        val DEFAULT_INSTANCE by lazy {
            RetrofitClientInstance.retrofitClientInstance.create(RetrofitRESTAPI::class.java)
        }
    }
}
