package com.luizssb.adidas.confirmed

import com.luizssb.adidas.confirmed.repository.UserPagingSource
import com.luizssb.adidas.confirmed.repository.UserPagingSourceImpl
import com.luizssb.adidas.confirmed.repository.UserRepository
import com.luizssb.adidas.confirmed.repository.UserRepositoryImpl
import com.luizssb.adidas.confirmed.service.UserService
import com.luizssb.adidas.confirmed.service.UserServiceImpl
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitRESTAPI
import org.koin.dsl.module

val DIModule = module {
    // services
    single<UserService> { UserServiceImpl(RetrofitRESTAPI.DEFAULT_INSTANCE) }

    // repositories
    factory<UserPagingSource> { UserPagingSourceImpl(get()) }
    single<UserRepository> { UserRepositoryImpl { get() } }
//
//    // view models
//    factory<UsersViewModel> { UsersViewModelImpl(get()) }
}
