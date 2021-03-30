package com.luizssb.adidas.confirmed.repository.product.impl

import com.luizssb.adidas.confirmed.dto.FakeDTO.Companion.product
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.service.product.ProductService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class ProductRepositoryImplTest {
    @Test
    fun getProduct_found_success() = runBlocking {
        // arrange
        val id = "id"
        val data = product(id)
        val service = mock<ProductService> {
            onBlocking { getProduct(id) } doReturn data
        }
        val repository = ProductRepositoryImpl(mock(), service)

        // act
        val result = repository.getProduct(id)

        // assert
        assertEquals(data, result)
    }

    @Test
    fun getProduct_notFound_success() = runBlocking {
        // arrange
        val id = "id"
        val data: Product? = null
        val service = mock<ProductService> {
            onBlocking { getProduct(id) } doReturn data
        }
        val repository = ProductRepositoryImpl(mock(), service)

        // act
        val result = repository.getProduct(id)

        // assert
        assertEquals(data, result)
    }

    @Test
    fun getProduct_error_throws() = runBlocking {
        // arrange
        val exception = RuntimeException()
        val service = mock<ProductService> {
            onBlocking { getProduct(any()) } doThrow exception
        }
        val repository = ProductRepositoryImpl(mock(), service)

        // act
        val result = try {
            repository.getProduct("")
            null
        } catch (ex: Throwable) {
            ex
        }

        // assert
        assertEquals(exception, result)
    }

    // luizssb: no way to test product(searchQuery: String?) because it's basically a proxy for
    // androidx's paging api, which has all its cool stuff internal/protected
}
