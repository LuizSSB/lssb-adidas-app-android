package com.luizssb.adidas.confirmed.service.retrofit

import com.luizssb.adidas.confirmed.BuildConfig
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import org.bouncycastle.crypto.tls.ContentType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class FakeCall<T>(private val _response: Response<T>) : Call<T> {
    companion object {
        fun <T> forData(result: T) = FakeCall(Response.success(result))

        fun <T> forErrorCode(code: Int) = FakeCall<T>(Response.error(
            code,
            ResponseBody.create(MediaType.get("application/json"), "{}")
        ))
    }

    private var executed: Boolean = false

    private val response: Response<T>
        get() {
            executed = true
            return _response
        }

    override fun clone(): Call<T> {
        return FakeCall(_response)
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
