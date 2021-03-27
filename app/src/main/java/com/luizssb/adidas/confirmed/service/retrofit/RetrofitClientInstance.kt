package com.luizssb.adidas.confirmed.service.retrofit

import com.luizssb.adidas.confirmed.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClientInstance {
    companion object {
        val retrofitClientInstance: Retrofit by lazy {
            val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(
                        chain.request()
                            .newBuilder()
                            .addHeader("X-API-Key", BuildConfig.SERVICE_KEY_API)
                            .build()
                    )
                }
                .readTimeout(BuildConfig.SERVICE_SECONDS_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(BuildConfig.SERVICE_SECONDS_TIMEOUT, TimeUnit.SECONDS)
                .build()

            Retrofit.Builder()
                .baseUrl(BuildConfig.SERVICE_URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        }
    }
}
