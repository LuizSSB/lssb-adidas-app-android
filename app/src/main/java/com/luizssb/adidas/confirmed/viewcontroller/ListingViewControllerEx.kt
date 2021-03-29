package com.luizssb.adidas.confirmed.viewcontroller

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.luizssb.adidas.confirmed.utils.extensions.FlowEx.Companion.observeOnLifecycle
import com.luizssb.adidas.confirmed.viewcontroller.adapter.GenericLoadStateAdapter
import com.luizssb.adidas.confirmed.viewmodel.list.Listing
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class ListingViewControllerEx private constructor() {
    companion object {
        fun <T : Any, TVH: RecyclerView.ViewHolder> Fragment.justObserveListing(
                controller: Listing.Controller<T>,
                refresh: SwipeRefreshLayout,
                list: RecyclerView,
                adapter: PagingDataAdapter<T, TVH>
        ) {
            refresh.setOnRefreshListener { controller.handleIntent(Listing.Intent.Refresh) }

            list.adapter = adapter
                .apply {
                    addLoadStateListener { loadState ->
                        controller.handleIntent(Listing.Intent.ChangeLoadState(loadState))
                    }
                }
                .withLoadStateHeaderAndFooter(
                    GenericLoadStateAdapter { adapter.retry() },
                    GenericLoadStateAdapter { adapter.retry() }
                )

            lifecycleScope.launch {
                adapter.loadStateFlow.collectLatest {
                    controller.handleIntent(Listing.Intent.ChangeLoadState(it))
                }
            }

            controller.state.observe(viewLifecycleOwner) {
                refresh.isRefreshing = it.loadingRefresh
                lifecycleScope.launch {
                    adapter.submitData(it.entries)
                }
            }

            controller.effects.observeOnLifecycle(viewLifecycleOwner) {
                when(it) {
                    Listing.Effect.Refresh -> adapter.refresh()

                    is Listing.Effect.ShowError ->
                        // TODO luizssb: replace with snackbar
                        Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
