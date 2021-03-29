package com.luizssb.adidas.confirmed.service.review.impl

import com.luizssb.adidas.confirmed.service.retrofit.FakeCall
import com.luizssb.adidas.confirmed.service.retrofit.FakeRemoteData
import com.luizssb.adidas.confirmed.service.retrofit.FakeRemoteData.Companion.review
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

@ExperimentalCoroutinesApi
class ReviewServiceImplTest {
    @Test
    fun getReviews_returnsAll() = runBlocking {
        // arrange
        val id = "1"
        val data = FakeRemoteData.product(id)
        val impl = ReviewServiceImpl(
            mock {
                on { getProduct(id) } doReturn FakeCall.forData(data)
            },
            mock()
        )

        // act
        val page = impl.getReviews(id, PageRef(0, 20))

        // assert
        assertFalse(page.hasMore)
        assertEquals(data.reviews!!.size, page.data.size)
        repeat(data.reviews!!.size) {
            assertEquals(data.reviews!![it].toAppType(), page.data[it])
        }
    }

    @Test
    fun getReviews_returnsNone() = runBlocking {
        // arrange
        val id = "1"
        val data = FakeRemoteData.product(id).copy(reviews = null)
        val impl = ReviewServiceImpl(
            mock {
                on { getProduct(id) } doReturn FakeCall.forData(data)
            },
            mock()
        )

        // act
        val page = impl.getReviews(id, PageRef(0, 20))

        // assert
        assertFalse(page.hasMore)
        assertThat(page.data, `is`(empty()))
    }

    @Test
    fun getReviews_pagination_hasMore() = runBlocking {
        // arrange
        val id = "1"
        val data = FakeRemoteData.product(id).run {
            copy(reviews = listOf(
                review(),
                review(),
                review(),
                review(),
            ).shuffled())
        }
        val impl = ReviewServiceImpl(
            mock {
                on { getProduct(id) } doReturn FakeCall.forData(data)
            },
            mock()
        )

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
    }

    @Test
    fun addReview_add() = runBlocking {
        // arrange
        val id = "id"
        val data = review(id)
        lateinit var added: RemoteReview
        val impl = ReviewServiceImpl(
            mock {},
            mock {
                on { addReview(eq(id), any()) } doAnswer  {
                    added = it.arguments[1] as RemoteReview
                    FakeCall.forData(Unit)
                }
            }
        )

        // act
        impl.addReview(data.toAppType())

        // assert
        // luizssb: makes the conversion, because of the Locale object
        assertEquals(data.toAppType().toRemoteType(), added)
    }
}
