package com.luizssb.adidas.confirmed.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.User
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userPagingSourceFactory: () -> UserPagingSource
) : UserRepository {
    override fun users(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = userPagingSourceFactory
        ).flow
    }
}
