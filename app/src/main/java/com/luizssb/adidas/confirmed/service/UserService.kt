package com.luizssb.adidas.confirmed.service

import com.luizssb.adidas.confirmed.dto.User
import com.luizssb.adidas.confirmed.utils.PageRef

interface UserService {
    suspend fun getUsers(paging: PageRef): Pair<List<User>, Boolean>
}
