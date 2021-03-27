package com.luizssb.adidas.confirmed

import com.luizssb.adidas.confirmed.repository.product.ProductPagingSource
import com.luizssb.adidas.confirmed.repository.product.ProductPagingSourceImpl
import com.luizssb.adidas.confirmed.repository.product.ProductRepository
import com.luizssb.adidas.confirmed.repository.product.ProductRepositoryImpl
import com.luizssb.adidas.confirmed.repository.review.ReviewPagingSource
import com.luizssb.adidas.confirmed.repository.review.ReviewPagingSourceImpl
import com.luizssb.adidas.confirmed.repository.review.ReviewRepository
import com.luizssb.adidas.confirmed.repository.review.ReviewRepositoryImpl
import com.luizssb.adidas.confirmed.service.product.ProductService
import com.luizssb.adidas.confirmed.service.product.ProductServiceImpl
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitProductRESTAPI
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitReviewRESTAPI
import com.luizssb.adidas.confirmed.service.review.ReviewService
import com.luizssb.adidas.confirmed.service.review.ReviewServiceImpl
import com.luizssb.adidas.confirmed.viewmodel.product.ProductListViewModel
import com.luizssb.adidas.confirmed.viewmodel.product.ProductListViewModelImpl
import org.koin.dsl.module

val DIModule = module {
    // lbaglie: services
    single<ProductService> { ProductServiceImpl(RetrofitProductRESTAPI.default) }
    single<ReviewService> { ReviewServiceImpl(RetrofitReviewRESTAPI.default) }

    // lbaglie: repositories

    // lbaglie: repositores - product
    factory<ProductPagingSource> { ProductPagingSourceImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get()) }

    // lbaglie: repositories - review
    factory<(String) -> ReviewPagingSource> {
        ReviewPagingSourceImpl.Factory(get())::produce
    }
    factory<ReviewRepository> { params -> ReviewRepositoryImpl(get(), get(), params.get()) }

    // lbaglie: view models
    factory<ProductListViewModel> { ProductListViewModelImpl(get()) }
}
