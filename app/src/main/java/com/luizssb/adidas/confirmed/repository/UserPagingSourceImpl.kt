package com.luizssb.adidas.confirmed.repository

import androidx.paging.PagingState
import com.luizssb.adidas.confirmed.service.UserService
import com.luizssb.adidas.confirmed.dto.User
import com.luizssb.adidas.confirmed.utils.PageRef

class UserPagingSourceImpl(private val service: UserService) : UserPagingSource() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.let {
                val prevKey = it.prevKey?.plus(it.data.size)
                if (prevKey != null) {
                    return@let prevKey
                }

                val nextKey= it.nextKey?.minus(it.data.size)
                if (nextKey == 0) null else nextKey
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val skip = params.key ?: 0
            val (users, hasMore) = service.getUsers(PageRef(skip, params.loadSize))
            LoadResult.Page(users, params.key, if (hasMore) skip + users.size else null)
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}
