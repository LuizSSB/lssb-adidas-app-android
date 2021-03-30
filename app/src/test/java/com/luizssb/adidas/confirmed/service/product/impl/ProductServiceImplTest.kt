package com.luizssb.adidas.confirmed.service.product.impl

import com.luizssb.adidas.confirmed.service.retrofit.FakeCall
import com.luizssb.adidas.confirmed.service.retrofit.FakeRemoteDTO
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitProductRESTAPI
import com.luizssb.adidas.confirmed.utils.PageRef
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.HttpException

@ExperimentalCoroutinesApi
class ProductServiceImplTest {
    @Test
    fun getProducts_noQuery_returnsAll() = runBlocking {
        // arrange
        val data = listOf(FakeRemoteDTO.remoteProduct("1"), FakeRemoteDTO.remoteProduct("2"))
        val api = mock<RetrofitProductRESTAPI> {
            on { getProducts() } doReturn FakeCall.forData(data)
        }
        val impl = ProductServiceImpl(api)

        // act
        val page = impl.getProducts(null, PageRef(0, 20))

        // assert
        assertFalse(page.hasMore)
        assertEquals(data.size, page.data.size)
        repeat(data.size) {
            assertEquals(data[it].toAppType(), page.data[it])
        }
        verify(api, times(1)).getProducts()
        Unit
    }

    @Test
    fun getProducts_query_returnsSome() = runBlocking {
        // arrange
        val query = "a a"
        val wrong = FakeRemoteDTO.remoteProduct("aa")
        val data = listOf(
            FakeRemoteDTO.remoteProduct(query.toUpperCase()),
            FakeRemoteDTO.remoteProduct("$query $query"),
            FakeRemoteDTO.remoteProduct("$query b"),
            FakeRemoteDTO.remoteProduct("c $query"),
            wrong
        )
            .shuffled()
        val expectedData = data.filter { it != wrong }
        val api: RetrofitProductRESTAPI = mock {
            on { getProducts() } doReturn FakeCall.forData(data)
        }
        val impl = ProductServiceImpl(api)

        // act
        val page = impl.getProducts(query, PageRef(0, 20))

        // assert
        assertFalse(page.hasMore)
        assertEquals(expectedData.size, page.data.size)
        repeat(expectedData.size) {
            assertEquals(expectedData[it].toAppType(), page.data[it])
        }
        verify(api, times(1)).getProducts()
        Unit
    }

    @Test
    fun getProducts_pagination_hasMore() = runBlocking {
        // arrange
        val data = listOf(
            FakeRemoteDTO.remoteProduct("1"),
            FakeRemoteDTO.remoteProduct("2"),
            FakeRemoteDTO.remoteProduct("3"),
            FakeRemoteDTO.remoteProduct("4"),
        )
            .shuffled()
        val api: RetrofitProductRESTAPI = mock {
            on { getProducts() } doReturn FakeCall.forData(data)
        }
        val impl = ProductServiceImpl(api)

        // act
        val page1 = impl.getProducts(null, PageRef(0, 2))
        val page2 = impl.getProducts(null, PageRef(2, 2))
        val page3 = impl.getProducts(null, PageRef(4, 2))

        // assert
        assertTrue(page1.hasMore)
        repeat(page1.data.size) {
            assertEquals(data[it].toAppType(), page1.data[it])
        }

        assertTrue(page2.hasMore)
        repeat(page2.data.size) {
            assertEquals(data[it + page1.data.size].toAppType(), page2.data[it])
        }

        assertFalse(page3.hasMore)
        assertThat(page3.data, `is`(empty()))

        verify(api, times(3)).getProducts()
        Unit
    }

    @Test
    fun getProduct_found() = runBlocking {
        // arrange
        val id = "1"
        val data = FakeRemoteDTO.remoteProduct(id)
        val api: RetrofitProductRESTAPI = mock {
            on { getProduct(id) } doReturn FakeCall.forData(data)
        }
        val impl = ProductServiceImpl(api)

        // act
        val result = impl.getProduct(data.id)

        // assert
        assertEquals(data.toAppType(), result)
        verify(api, times(1)).getProduct(data.id)
        Unit
    }

    @Test
    fun getProduct_notFound() = runBlocking {
        // arrange
        val id = "1"
        val api: RetrofitProductRESTAPI = mock {
            on { getProduct(id) } doReturn FakeCall.forErrorCode(500)
        }
        val impl = ProductServiceImpl(api)

        // act
        val result = impl.getProduct(id)

        // assert
        assertNull(result)
        verify(api, times(1)).getProduct(id)
        Unit
    }

    @Test
    fun getProduct_statusProblem() = runBlocking {
        // arrange
        val id = "1"
        val errorCode = 502
        val api: RetrofitProductRESTAPI = mock {
            on { getProduct(id) } doReturn FakeCall.forErrorCode(errorCode)
        }
        val impl = ProductServiceImpl(api)

        // act
        val exception = try {
            impl.getProduct(id)
            null
        } catch (ex: HttpException) {
            ex
        }

        // assert
        assertNotNull(exception)
        assertThat(exception!!.code(), `is`(errorCode))
        verify(api, times(1)).getProduct(id)
        Unit
    }

    @Test
    fun getProduct_someMisteryousProblem() = runBlocking {
        // arrange
        val id = "1"
        val exception = object : RuntimeException() { }
        val api: RetrofitProductRESTAPI = mock {
            on { getProduct(id) } doThrow exception
        }
        val impl = ProductServiceImpl(api)

        // act
        val failed = try {
            impl.getProduct(id)
            null
        } catch (ex: Exception) {
            ex
        }

        // assert
        assertNotNull(failed)
        assertEquals(exception.javaClass, failed!!.javaClass)
        verify(api, times(1)).getProduct(id)
        Unit
    }
}
