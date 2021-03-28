package com.luizssb.adidas.confirmed

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.luizssb.adidas.confirmed.databinding.FragmentProductListBinding
import com.luizssb.adidas.confirmed.view.adapter.ProductsAdapter
import com.luizssb.adidas.confirmed.viewmodel.product.ProductListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ProductListFragment : Fragment() {
    private lateinit var layout: FragmentProductListBinding

    private val itemAdapter by lazy {
        ProductsAdapter { viewModel.handleIntent(ProductListViewModel.Intent.Select(it)) }
    }

    private val viewModel by inject<ProductListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            itemAdapter.loadStateFlow.collectLatest {
                viewModel.handleIntent(ProductListViewModel.Intent.ChangeLoadState(it))
            }
        }

        viewModel.startOrResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        layout = FragmentProductListBinding.inflate(inflater, container, false)

        with(layout) {
            refresh.setOnRefreshListener {
                viewModel.handleIntent(ProductListViewModel.Intent.Refresh)
            }
            list.adapter = itemAdapter
        }

        viewModel.let {
            it.state.observe(viewLifecycleOwner, Observer(this::render))
            it.effects.observe(viewLifecycleOwner, Observer(this::render))
        }

        return layout.root
    }

    // luizssb: the challenge's description described the app as being composed by two pages, so I am
    // choosing to not use Android's default approach to search, which requires extra activities and
    // a lot of other stuff, in favor of this simpler approach that is easier to implement.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.search_product, menu)

        (menu.findItem(R.id.action_search)?.actionView as? SearchView)?.run {
            queryHint = getString(R.string.hint_product_search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.handleIntent(ProductListViewModel.Intent.ChangeSearchQuery(newText))
                    return true
                }
            })
            setOnCloseListener {
                viewModel.handleIntent(ProductListViewModel.Intent.ChangeSearchQuery(null))
                false
            }
        }
    }

    private fun render(state: ProductListViewModel.State) {
        layout.refresh.isRefreshing = state.loadingRefresh
        lifecycleScope.launch {
            itemAdapter.submitData(state.products)
        }
    }

    private fun render(effect: ProductListViewModel.Effect) {
        when(effect) {
            ProductListViewModel.Effect.Refresh -> itemAdapter.refresh()
            is ProductListViewModel.Effect.ShowError ->
                // TODO luizssb: replace with snackbar
                Toast.makeText(requireContext(), effect.error.message, Toast.LENGTH_SHORT).show()
        }
    }
}
