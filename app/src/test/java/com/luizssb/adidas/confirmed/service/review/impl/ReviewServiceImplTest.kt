package com.luizssb.adidas.confirmed.service.review.impl

import com.luizssb.adidas.confirmed.service.retrofit.FakeCall
import com.luizssb.adidas.confirmed.service.retrofit.FakeRemoteDTO
import com.luizssb.adidas.confirmed.service.retrofit.FakeRemoteDTO.Companion.remoteReview
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitProductRESTAPI
import com.luizssb.adidas.confirmed.service.retrofit.RetrofitReviewRESTAPI
import com.luizssb.adidas.confirmed.service.retrofit.dto.RemoteReview
import com.luizssb.adidas.confirmed.service.retrofit.dto.RemoteReview.Companion.toRemoteType
import com.luizssb.adidas.confirmed.utils.PageRef
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.empty
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.*
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
class ReviewServiceImplTest {
    @Test
    fun getReviews_returnsAll() = runBlocking {
        // arrange
        val id = "1"
        val data = FakeRemoteDTO.remoteProduct(id)
        val api: RetrofitProductRESTAPI = mock {
            on { getProduct(id) } doReturn FakeCall.forData(data)
        }
        val impl = ReviewServiceImpl(api, mock())

        // act
        val page = impl.getReviews(id, PageRef(0, 20))

        // assert
        assertFalse(page.hasMore)
        assertEquals(data.reviews!!.size, page.data.size)
        repeat(data.reviews!!.size) {
            assertEquals(data.reviews!![it].toAppType(), page.data[it])
        }
        verify(api, times(1)).getProduct(id)
        Unit
    }

    @Test
    fun getReviews_returnsNone() = runBlocking {
        // arrange
        val id = "1"
        val data = FakeRemoteDTO.remoteProduct(id).copy(reviews = null)
        val api: RetrofitProductRESTAPI = mock {
                    on { getProduct(id) } doReturn FakeCall.forData(data)
                }
        val impl = ReviewServiceImpl(api, mock())

        // act
        val page = impl.getReviews(id, PageRef(0, 20))

        // assert
        assertFalse(page.hasMore)
        assertThat(page.data, `is`(empty()))
        verify(api, times(1)).getProduct(id)
        Unit
    }

    @Test
    fun getReviews_pagination_hasMore() = runBlocking {
        // arrange
        val id = "1"
        val data = FakeRemoteDTO.remoteProduct(id).run {
            copy(reviews = listOf(
                remoteReview(),
                remoteReview(),
                remoteReview(),
                remoteReview(),
            ).shuffled())
        }
        val api: RetrofitProductRESTAPI = mock {
            on { getProduct(id) } doReturn FakeCall.forData(data)
        }
        val impl = ReviewServiceImpl(api, mock())

        // act
        val page1 = impl.getReviews(id, PageRef(0, 2))
        val page2 = impl.getReviews(id, PageRef(2, 2))
        val page3 = impl.getReviews(id, PageRef(4, 2))

        // assert
        assertTrue(page1.hasMore)
        repeat(page1.data.size) {
            assertEquals(data.reviews!![it].toAppType(), page1.data[it])
        }

        assertTrue(page2.hasMore)
        repeat(page2.data.size) {
            assertEquals(data.reviews!![it + page1.data.size].toAppType(), page2.data[it])
        }

        assertFalse(page3.hasMore)
        assertThat(page3.data, `is`(empty()))
        verify(api, times(3)).getProduct(id)
        Unit
    }

    @Test
    fun addReview_add() = runBlocking {
        // arrange
        val id = "id"
        val data = remoteReview(id)
        val dataConverted = data.toAppType().toRemoteType()
        lateinit var added: RemoteReview
        val api: RetrofitReviewRESTAPI = mock {
            on { addReview(eq(id), eq(dataConverted)) } doAnswer {
                added = it.arguments[1] as RemoteReview
                FakeCall.forData(Unit)
            }
        }
        val impl = ReviewServiceImpl(mock(), api)

        // act
        impl.addReview(data.toAppType())

        // assert
        // luizssb: makes the conversion, because of the Locale object
        assertEquals(dataConverted, added)
        verify(api, times(1)).addReview(eq(data.productId), eq(dataConverted))
        Unit
    }

    @Test
    fun addReview_error() = runBlocking {
        // arrange
        val id = "id"
        val data = remoteReview(id)
        val dataConverted = data.toAppType().toRemoteType()
        val exception = object : RuntimeException() {}
        val api: RetrofitReviewRESTAPI = mock {
            on { addReview(eq(id), eq(dataConverted)) } doThrow exception
        }
        val impl = ReviewServiceImpl(mock(), api)

        // act
        val result = try {
            impl.addReview(data.toAppType())
            null
        } catch (ex: Throwable) {
            ex
        }

        // assert
        assertNotNull(result)
        assertEquals(exception.javaClass, result!!.javaClass)
        verify(api, times(1)).addReview(eq(data.productId), eq(dataConverted))
        Unit
    }
}
