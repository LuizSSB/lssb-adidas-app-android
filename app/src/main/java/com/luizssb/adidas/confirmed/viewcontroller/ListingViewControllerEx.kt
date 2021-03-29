package com.luizssb.adidas.confirmed.viewcontroller

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luizssb.adidas.confirmed.databinding.ViewListGenericBinding
import com.luizssb.adidas.confirmed.utils.extensions.FlowEx.Companion.observeOnLifecycle
import com.luizssb.adidas.confirmed.viewcontroller.adapter.GenericLoadStateAdapter
import com.luizssb.adidas.confirmed.viewmodel.list.Listing
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class ListingViewControllerEx private constructor() {
    companion object {
        fun <T : Any, TVH: RecyclerView.ViewHolder> Fragment.justObserveListing(
                controller: Listing.Controller<T>,
                listView: ViewListGenericBinding,
                adapter: PagingDataAdapter<T, TVH>
        ) {
            val doRetry = { _: Any -> controller.handleIntent(Listing.Intent.Retry) }
            val doRefresh = { _: Any -> controller.handleIntent(Listing.Intent.Refresh) }

            with(listView) {
                buttonRetry.setOnClickListener(doRetry)
                buttonRefresh.setOnClickListener(doRefresh)
                refresh.setOnRefreshListener { doRefresh(Unit) }
                listView.list.adapter = adapter.withLoadStateHeaderAndFooter(
                    GenericLoadStateAdapter(doRetry),
                    GenericLoadStateAdapter(doRetry)
                )
            }

            lifecycleScope.launch {
                adapter.loadStateFlow.collectLatest {
                    controller.handleIntent(Listing.Intent.ChangeLoadState(it, adapter.itemCount))
                }
            }

            controller.state.observe(viewLifecycleOwner) {
                with(listView) {
                    refresh.isRefreshing = it.loadingRefresh
                    containerList.isVisible = it.contentType == Listing.ContentType.Listing
                    containerProblem.isVisible = it.contentType == Listing.ContentType.RefreshProblem
                    containerEmpty.isVisible = it.contentType == Listing.ContentType.Empty
                }

                lifecycleScope.launch {
                    adapter.submitData(it.entries)
                }
            }

            controller.effects.observeOnLifecycle(viewLifecycleOwner) {
                when(it) {
                    Listing.Effect.Refresh -> adapter.refresh()

                    Listing.Effect.Retry -> adapter.retry()

                    is Listing.Effect.ShowError ->
                        // TODO luizssb: replace with snackbar
                        Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
