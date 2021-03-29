package com.luizssb.adidas.confirmed

import com.luizssb.adidas.confirmed.repository.product.ProductPagingSource
import com.luizssb.adidas.confirmed.repository.product.ProductRepository
import com.luizssb.adidas.confirmed.repository.product.impl.ProductPagingSourceImpl
import com.luizssb.adidas.confirmed.repository.product.impl.ProductRepositoryImpl
import com.luizssb.adidas.confirmed.repository.review.ReviewPagingSource
import com.luizssb.adidas.confirmed.repository.review.ReviewRepository
import com.luizssb.adidas.confirmed.repository.review.impl.ReviewPagingSourceImpl
import com.luizssb.adidas.confirmed.repository.review.impl.ReviewRepositoryImpl
import com.luizssb.adidas.confirmed.service.product.ProductService
import com.luizssb.adidas.confirmed.service.product.impl.ProductServiceImpl
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitProductRESTAPI
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitReviewRESTAPI
import com.luizssb.adidas.confirmed.service.review.ReviewService
import com.luizssb.adidas.confirmed.service.review.impl.ReviewServiceImpl
import com.luizssb.adidas.confirmed.viewmodel.list.Listing
import com.luizssb.adidas.confirmed.viewmodel.list.impl.ListControllerImpl
import com.luizssb.adidas.confirmed.viewmodel.product.ProductDetail
import com.luizssb.adidas.confirmed.viewmodel.product.ProductList
import com.luizssb.adidas.confirmed.viewmodel.product.impl.ProductDetailViewModelImpl
import com.luizssb.adidas.confirmed.viewmodel.product.impl.ProductListViewModelImpl
import com.luizssb.adidas.confirmed.viewmodel.review.Reviews
import com.luizssb.adidas.confirmed.viewmodel.review.impl.ReviewsViewModelImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val DIModule = module {
    // luizssb: services - retrofit
    single { RetrofitProductRESTAPI.default }
    single { RetrofitReviewRESTAPI.default }

    // luizssb: services
    single<ProductService> { ProductServiceImpl(get()) }
    single<ReviewService> { ReviewServiceImpl(get(), get()) }

    // luizssb: repositores
    factory<ProductPagingSource.Factory>{ ProductPagingSourceImpl.Factory(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get()) }
    factory<ReviewPagingSource.Factory> { ReviewPagingSourceImpl.Factory(get()) }
    single<ReviewRepository> { ReviewRepositoryImpl(get(), get()) }

    // luizssb: controllers
    // luizssb: parameter type can be Any because of type erasure
    viewModel<Listing.Controller<Any>> { ListControllerImpl() }

    // luizssb: view models
    viewModel<ProductList.ViewModel> { ProductListViewModelImpl(get(), get()) }
    viewModel<ProductDetail.ViewModel> { params -> ProductDetailViewModelImpl(get(), params.get()) }
    viewModel<Reviews.ViewModel> { params -> ReviewsViewModelImpl(get(), get(), params.get()) }
}
