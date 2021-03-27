package com.luizssb.adidas.confirmed.view

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.luizssb.adidas.confirmed.R
import com.luizssb.adidas.confirmed.databinding.ActivityMainBinding
import com.luizssb.adidas.confirmed.view.adapter.ProductsAdapter
import com.luizssb.adidas.confirmed.viewmodel.product.ProductListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val layout by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val itemAdapter by lazy {
        ProductsAdapter { viewModel.handleIntent(ProductListViewModel.Intent.Select(it)) }
    }

    private val viewModel by inject<ProductListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.root)
        setSupportActionBar(layout.toolbar)

        with(layout) {
            refresh.setOnRefreshListener {
                viewModel.handleIntent(ProductListViewModel.Intent.Refresh)
            }
            list.adapter = itemAdapter
        }

        lifecycleScope.launch {
            itemAdapter.loadStateFlow.collectLatest {
                viewModel.handleIntent(ProductListViewModel.Intent.ChangeLoadState(it))
            }
        }

        viewModel.let {
            it.state.observe(this, Observer(this::render))
            it.effects.observe(this, Observer(this::render))
            it.startOrResume()
        }
    }

    // luizssb: the challenge's description described the app as being composed by two pages, so I am
    // choosing to not use Android's default approach to search, which requires extra activities and
    // a lot of other stuff, in favor of this simpler approach that is easier to implement.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_product, menu)

        (menu?.findItem(R.id.action_search)?.actionView as? SearchView)?.run {
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

        return true
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
                Toast.makeText(this, effect.error.message, Toast.LENGTH_SHORT).show()
        }
    }
}
