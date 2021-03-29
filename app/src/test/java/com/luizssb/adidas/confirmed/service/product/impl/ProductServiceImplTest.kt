package com.luizssb.adidas.confirmed.service.product.impl

import com.luizssb.adidas.confirmed.service.retrofit.RetrofitProductRESTAPI
import com.luizssb.adidas.confirmed.utils.PageRef
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class ProductServiceImplTest {
    @Test
    fun getProducts_noQuery_returnsAll() = runBlocking {
        // arrange
        val data = listOf(FakeRemoteData.product("1"), FakeRemoteData.product("2"))
        val mockApi: RetrofitProductRESTAPI = mock {
            on { getProducts() } doReturn FakeCall(data)
        }
        val impl = ProductServiceImpl(mockApi)

        // act
        val paginationResult = impl.getProducts(null, PageRef(0, 20))

        // assert
        assertFalse(paginationResult.hasMore)
        assertEquals(paginationResult.data.size, data.size)
        repeat(data.size) {
            assertEquals(paginationResult.data[it], data[it].toAppType())
        }
    }
}
