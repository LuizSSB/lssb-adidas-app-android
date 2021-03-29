package com.luizssb.adidas.confirmed.repository.review.impl

import com.luizssb.adidas.confirmed.dto.FakeDTO.Companion.review
import com.luizssb.adidas.confirmed.dto.Review
import com.luizssb.adidas.confirmed.service.review.ReviewService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class ReviewRepositoryImplTest {
    @Test
    fun addReview_success() = runBlocking {
        // arrange
        val data = review()
        lateinit var expected: Review
        val service = mock<ReviewService> {
            onBlocking { addReview(any()) } doAnswer {
                expected = it.getArgument(0)
            }
        }
        val repository = ReviewRepositoryImpl(mock(), service)

        // act
        repository.addReview(data)

        // assert
        assertEquals(data, expected)
        verify(service, times(1)).addReview(data)
    }

    @Test
    fun addReview_error() = runBlocking {
        // arrange
        val data = review()
        val exception = RuntimeException()
        val service = mock<ReviewService> {
            onBlocking { addReview(any()) } doThrow exception
        }
        val repository = ReviewRepositoryImpl(mock(), service)

        // act
        val result = try {
            repository.addReview(data)
            null
        } catch (ex: Throwable) {
            ex
        }

        // assert
        assertEquals(exception, result)
        verify(service, times(1)).addReview(data)
    }
}
