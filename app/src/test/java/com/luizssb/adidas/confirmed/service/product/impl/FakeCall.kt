package com.luizssb.adidas.confirmed.service.product.impl

import com.luizssb.adidas.confirmed.BuildConfig
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class FakeCall<T>(private val data: T) : Call<T> {
    private var executed: Boolean = false

    private val response: Response<T>
        get() {
            executed = true
            return Response.success(data)
        }

    override fun clone(): Call<T> {
        return FakeCall(data)
    }

    override fun execute(): Response<T> {
        return response
    }

    override fun enqueue(callback: Callback<T>) {
        callback.onResponse(this, response)
    }

    override fun isExecuted(): Boolean {
        return executed
    }

    override fun cancel() {
    }

    override fun isCanceled(): Boolean {
        return false
    }

    override fun request(): Request {
        return Request.Builder().build()
    }

    override fun timeout(): Timeout {
        return Timeout().timeout(BuildConfig.SERVICE_SECONDS_TIMEOUT, TimeUnit.SECONDS)
    }
}
