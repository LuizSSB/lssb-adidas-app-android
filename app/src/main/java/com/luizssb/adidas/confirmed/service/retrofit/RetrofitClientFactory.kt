package com.luizssb.adidas.confirmed.service.retrofit

import com.luizssb.adidas.confirmed.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClientFactory(private val baseUrl: String) {
    fun produce(): Retrofit {
        val httpClient = OkHttpClient.Builder()
                .readTimeout(BuildConfig.SERVICE_SECONDS_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(BuildConfig.SERVICE_SECONDS_TIMEOUT, TimeUnit.SECONDS)
                .build()

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
    }
}
