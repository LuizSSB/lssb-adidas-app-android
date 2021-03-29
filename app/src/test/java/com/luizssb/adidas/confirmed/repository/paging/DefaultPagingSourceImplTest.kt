package com.luizssb.adidas.confirmed.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.luizssb.adidas.confirmed.dto.FakeDTO.Companion.product
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.service.PaginationResult
import com.luizssb.adidas.confirmed.utils.PageRef
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultPagingSourceImplTest {
    @Test
    fun getRefreshKey_nullAnchor_returnsNullKey() {
        // arrange
        val elementsPerPage = 5
        val qtyPages = 6
        val pages = (0 until qtyPages).map {
            PagingSource.LoadResult.Page(
                (0 until elementsPerPage).map { product() },
                if (it == 0) null else it * elementsPerPage,
                (it * elementsPerPage) + elementsPerPage
            )
        }
        val state = PagingState(
            pages,
            null,
            PagingConfigEx.DEFAULT_PAGING_CONFIG,
            0
        )
        val pagingSource = DefaultPagingSourceImpl<Product> { TODO() }

        // act
        val key = pagingSource.getRefreshKey(state)

        // assert
        assertNull(key)
    }

    @Test
    fun getRefreshKey_firstPageAnchor_returnsNullKey() {
        // arrange
        val elementsPerPage = 5
        val qtyPages = 6
        val pages = (0 until qtyPages).map {
            PagingSource.LoadResult.Page(
                (0 until elementsPerPage).map { product() },
                if (it == 0) null else it * elementsPerPage,
                (it * elementsPerPage) + elementsPerPage
            )
        }
        val state = PagingState(
            pages,
            0,
            PagingConfigEx.DEFAULT_PAGING_CONFIG,
            0
        )
        val pagingSource = DefaultPagingSourceImpl<Product> { TODO() }

        // act
        val key = pagingSource.getRefreshKey(state)

        // assert
        assertEquals(null, key)
    }

    @Test
    fun getRefreshKey_secondPageAnchor_returnsThirdKey() {
        // arrange
        val elementsPerPage = 5
        val qtyPages = 6
        val pages = (0 until qtyPages).map {
            PagingSource.LoadResult.Page(
                (0 until elementsPerPage).map { product() },
                if (it == 0) null else it * elementsPerPage,
                (it * elementsPerPage) + elementsPerPage
            )
        }
        @Suppress("UnnecessaryVariable") val thisPage = elementsPerPage
        val nextPage = elementsPerPage + elementsPerPage
        val state = PagingState(
            pages,
            thisPage,
            PagingConfigEx.DEFAULT_PAGING_CONFIG,
            0
        )
        val pagingSource = DefaultPagingSourceImpl<Product> { TODO() }

        // act
        val key = pagingSource.getRefreshKey(state)

        // assert
        assertEquals(nextPage, key)
    }

    @Test
    fun load_refreshSuccess_noMore() = runBlocking {
        // arrange
        val limit = 5
        val data = (0 until limit).map { product() }
        val loadParams = PagingSource.LoadParams.Refresh<Int>(null, limit, false)
        lateinit var pageRef: PageRef
        val pagingSource = DefaultPagingSourceImpl {
            pageRef = it
            PaginationResult(data, false)
        }

        // act
        val result = pagingSource.load(loadParams)

        // assert
        assertEquals(PageRef(0, limit), pageRef)
        assertThat(result, `is`(instanceOf(PagingSource.LoadResult.Page::class.java)))

        val castResult = result as PagingSource.LoadResult.Page
        assertNull(castResult.nextKey)
        assertNull(castResult.nextKey)
        repeat(castResult.data.size) {
            assertEquals(data[it], castResult.data[it])
        }
    }

    @Test
    fun load_appendSuccess_hasMore() = runBlocking {
        // arrange
        val expectedPageRef = PageRef(10, 5)
        val data = (0 until expectedPageRef.limit).map { product() }
        val loadParams = PagingSource.LoadParams.Refresh(expectedPageRef.skip, expectedPageRef.limit, false)
        lateinit var pageRef: PageRef
        val pagingSource = DefaultPagingSourceImpl {
            pageRef = it
            PaginationResult(data, true)
        }

        // act
        val result = pagingSource.load(loadParams)

        // assert
        assertEquals(expectedPageRef, pageRef)
        assertThat(result, `is`(instanceOf(PagingSource.LoadResult.Page::class.java)))

        val castResult = result as PagingSource.LoadResult.Page
        assertEquals(expectedPageRef.skip, castResult.prevKey)
        assertEquals(expectedPageRef.upperBound, castResult.nextKey)
        repeat(castResult.data.size) {
            assertEquals(data[it], castResult.data[it])
        }
    }

    @Test
    fun load_appendError() = runBlocking {
        // arrange
        val exception = RuntimeException()
        val loadParams = PagingSource.LoadParams.Refresh(0, 0, false)
        val pagingSource = DefaultPagingSourceImpl<Product> {
            throw exception
        }

        // act
        val result = pagingSource.load(loadParams)

        // assert
        assertThat(result, `is`(instanceOf(PagingSource.LoadResult.Error::class.java)))

        val castResult = result as PagingSource.LoadResult.Error
        assertEquals(exception, castResult.throwable)
    }
}
