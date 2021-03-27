package com.luizssb.adidas.confirmed.service

import com.luizssb.adidas.confirmed.dto.User
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitRESTAPI
import com.luizssb.adidas.confirmed.utils.PageRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserServiceImpl(private val restAPI: RetrofitRESTAPI) : UserService {
    override suspend fun getUsers(paging: PageRef): Pair<List<User>, Boolean> {
        return withContext(Dispatchers.IO) {
//            val response = restAPI.getPeople().await()
//            response to true
            emptyList<User>() to false
        }
    }
}
