package com.luizssb.adidas.confirmed.service.product.impl

import com.luizssb.adidas.confirmed.service.retrofit.FakeCall
import com.luizssb.adidas.confirmed.service.retrofit.FakeRemoteDTO
import com.luizssb.adidas.confirmed.utils.PageRef
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.empty
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import retrofit2.HttpException

@ExperimentalCoroutinesApi
class ProductServiceImplTest {
    @Test
    fun getProducts_noQuery_returnsAll() = runBlocking {
        // arrange
        val data = listOf(FakeRemoteDTO.product("1"), FakeRemoteDTO.product("2"))
        val impl = ProductServiceImpl(mock {
            on { getProducts() } doReturn FakeCall.forData(data)
        })

        // act
        val page = impl.getProducts(null, PageRef(0, 20))

        // assert
        assertFalse(page.hasMore)
        assertEquals(data.size, page.data.size)
        repeat(data.size) {
            assertEquals(data[it].toAppType(), page.data[it])
        }
    }

    @Test
    fun getProducts_query_returnsSome() = runBlocking {
        // arrange
        val query = "a a"
        val wrong = FakeRemoteDTO.product("aa")
        val data = listOf(
            FakeRemoteDTO.product(query.toUpperCase()),
            FakeRemoteDTO.product("$query $query"),
            FakeRemoteDTO.product("$query b"),
            FakeRemoteDTO.product("c $query"),
            wrong
        )
            .shuffled()
        val expectedData = data.filter { it != wrong }
        val impl = ProductServiceImpl(mock {
            on { getProducts() } doReturn FakeCall.forData(data)
        })

        // act
        val page = impl.getProducts(query, PageRef(0, 20))

        // assert
        assertFalse(page.hasMore)
        assertEquals(expectedData.size, page.data.size)
        repeat(expectedData.size) {
            assertEquals(expectedData[it].toAppType(), page.data[it])
        }
    }

    @Test
    fun getProducts_pagination_hasMore() = runBlocking {
        // arrange
        val data = listOf(
            FakeRemoteDTO.product("1"),
            FakeRemoteDTO.product("2"),
            FakeRemoteDTO.product("3"),
            FakeRemoteDTO.product("4"),
        )
            .shuffled()
        val impl = ProductServiceImpl(mock {
            on { getProducts() } doReturn FakeCall.forData(data)
        })

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
    }

    @Test
    fun getProduct_found() = runBlocking {
        // arrange
        val id = "1"
        val data = FakeRemoteDTO.product(id)
        val impl = ProductServiceImpl(mock {
            on { getProduct(id) } doReturn FakeCall.forData(data)
        })

        // act
        val result = impl.getProduct(data.id)

        // assert
        assertEquals(data.toAppType(), result)
    }

    @Test
    fun getProduct_notFound() = runBlocking {
        // arrange
        val id = "1"
        val impl = ProductServiceImpl(mock {
            on { getProduct(id) } doReturn FakeCall.forErrorCode(500)
        })

        // act
        val result = impl.getProduct(id)

        // assert
        assertNull(result)
    }

    @Test
    fun getProduct_statusProblem() = runBlocking {
        // arrange
        val id = "1"
        val errorCode = 502
        val impl = ProductServiceImpl(mock {
            on { getProduct(id) } doReturn FakeCall.forErrorCode(errorCode)
        })

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
    }

    @Test
    fun getProduct_someMisteryousProblem() = runBlocking {
        // arrange
        val id = "1"
        val exception = object : RuntimeException() { }
        val impl = ProductServiceImpl(mock {
            on { getProduct(id) } doThrow exception
        })

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
    }
}
