package com.luizssb.adidas.confirmed.viewmodel.list.impl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.luizssb.adidas.confirmed.dto.FakeDTO.Companion.product
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.utils.extensions.LoadStateEx.Companion.loading
import com.luizssb.adidas.confirmed.viewmodel.LiveDataEx.Companion.getOrAwaitValue
import com.luizssb.adidas.confirmed.viewmodel.list.Listing
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListingControllerImplTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun handleIntent_refresh() = runBlocking {
        // arrange
        val controller = ListingControllerImpl<Any>()

        // act
        controller.handleIntent(Listing.Intent.Refresh)
        val state = controller.state.getOrAwaitValue()
        val effect = controller.effects.first()

        // assert
        assertEquals(Listing.State<Any>(), state)
        assertEquals(Listing.Effect.Refresh, effect)
    }

    @Test
    fun handleIntent_retry() = runBlocking {
        // arrange
        val controller = ListingControllerImpl<Any>()

        // act
        controller.handleIntent(Listing.Intent.Retry)
        val state = controller.state.getOrAwaitValue()
        val effect = controller.effects.first()

        // assert
        assertEquals(Listing.State<Any>(), state)
        assertEquals(Listing.Effect.Retry, effect)
    }

    @Test
    fun handleIntent_loadStates_refreshError() = runBlocking {
        // arrange
        val exception = RuntimeException()
        val states = LoadStates(
                LoadState.Error(exception),
                LoadState.NotLoading(false),
                LoadState.Error(exception)
        )
                .toCombinedLoadStates()
        val expectedState = states.toListingState<Any>().copy(
                contentType = Listing.ContentType.RefreshProblem
        )
        val controller = ListingControllerImpl<Any>()

        // act
        controller.handleIntent(Listing.Intent.ChangeLoadState(states, 10))
        val state = controller.state.getOrAwaitValue()

        // assert
        assertEquals(expectedState, state)
    }

    @Test
    fun handleIntent_loadStates_empty() = runBlocking {
        // arrange
        val exception = RuntimeException()
        val states = LoadStates(
                LoadState.NotLoading(false),
                LoadState.NotLoading(false),
                LoadState.Error(exception)
        )
                .toCombinedLoadStates()
        val expectedState = states.toListingState<Any>().copy(
                contentType = Listing.ContentType.Empty
        )
        val controller = ListingControllerImpl<Any>()

        // act
        controller.handleIntent(Listing.Intent.ChangeLoadState(states, 0))
        val state = controller.state.getOrAwaitValue()

        // assert
        assertEquals(expectedState, state)
    }

    @Test
    fun handleIntent_loadStates_content() = runBlocking {
        // arrange
        val exception = RuntimeException()
        val states = LoadStates(
                LoadState.NotLoading(false),
                LoadState.NotLoading(false),
                LoadState.Error(exception)
        )
                .toCombinedLoadStates()
        val expectedState = states.toListingState<Any>().copy(
                contentType = Listing.ContentType.Listing
        )
        val controller = ListingControllerImpl<Any>()

        // act
        controller.handleIntent(Listing.Intent.ChangeLoadState(states, 10))
        val state = controller.state.getOrAwaitValue()

        // assert
        assertEquals(expectedState, state)
    }

    @Test
    fun updateEntries() {
        // arrange
        val pagingData1 = PagingData.from(listOf(product()))
        val pagingData2 = PagingData.from(listOf(product()))
        val resultFlow = flowOf(pagingData1, pagingData2)
        val controller = ListingControllerImpl<Product>()

        // act
        controller.updateEntries(resultFlow)
        val state = controller.state.getOrAwaitValue()

        // assert
        assertEquals(Listing.State(pagingData2), state)
    }

    private fun LoadStates.toCombinedLoadStates() = CombinedLoadStates(
            refresh,
            prepend,
            append,
            this
    )

    private fun <T : Any> CombinedLoadStates.toListingState() = Listing.State<T>(
            loadingRefresh = refresh.loading,
            loadingPrevious = prepend.loading,
            loadingMore = append.loading,
    )
}


