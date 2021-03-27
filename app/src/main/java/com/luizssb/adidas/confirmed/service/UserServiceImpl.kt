package com.luizssb.adidas.confirmed.service

import com.luizssb.adidas.confirmed.dto.User
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitProductRESTAPI
import com.luizssb.adidas.confirmed.utils.PageRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserServiceImpl(private val restAPI: RetrofitProductRESTAPI) : UserService {
    override suspend fun getUsers(paging: PageRef): Pair<List<User>, Boolean> {
        return withContext(Dispatchers.IO) {
//            val response = restAPI.getPeople().await()
//            response to true
            emptyList<User>() to false
        }
    }
}
