package com.luizssb.adidas.confirmed.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.luizssb.adidas.confirmed.service.PaginationResult
import com.luizssb.adidas.confirmed.utils.PageRef

open class DefaultPagingSourceImpl<T : Any>(
    val acquirer: suspend (PageRef) -> PaginationResult<T>
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let {
                val prevKey = it.prevKey?.plus(it.data.size)

                if (prevKey != null) {
                    return prevKey
                }

                val nextKey = it.nextKey?.minus(it.data.size)
                if (nextKey == 0) null else nextKey
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val skip = params.key ?: 0
            val (users, hasMore) = acquirer(PageRef(skip, params.loadSize))
            LoadResult.Page(users, params.key, if (hasMore) skip + users.size else null)
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}
