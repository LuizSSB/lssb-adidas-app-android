package com.luizssb.adidas.confirmed.repository

import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun users(): Flow<PagingData<User>>
}
