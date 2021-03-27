package com.luizssb.adidas.confirmed.service.retrofit

import com.luizssb.adidas.confirmed.BuildConfig
import com.luizssb.adidas.confirmed.dto.Review
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitReviewRESTAPI {
    @GET("/review/{productId}")
    fun getReviews(productId: String): Call<List<Review>>

    @POST("/review/{productId}")
    fun addReview(productId: String, @Body review: Review): Call<Unit>

    companion object {
        val default: RetrofitReviewRESTAPI by lazy {
            RetrofitClientFactory(BuildConfig.SERVICE_URL_REVIEW)
                .produce()
                .create(RetrofitReviewRESTAPI::class.java)
        }
    }
}
