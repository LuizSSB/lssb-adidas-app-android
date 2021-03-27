package com.luizssb.adidas.confirmed.repository

import androidx.paging.PagingSource
import com.luizssb.adidas.confirmed.dto.User

abstract class UserPagingSource : PagingSource<Int, User>()
