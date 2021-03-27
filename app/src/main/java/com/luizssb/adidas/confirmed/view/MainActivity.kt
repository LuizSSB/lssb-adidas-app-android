package com.luizssb.adidas.confirmed.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.luizssb.adidas.confirmed.databinding.ActivityMainBinding
import com.luizssb.adidas.confirmed.view.adapter.ProductsAdapter
import com.luizssb.adidas.confirmed.viewmodel.product.ProductListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val layout by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val itemAdapter by lazy { ProductsAdapter() }

    private val viewModel by inject<ProductListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.root)

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
                // TODO lbaglie: replace with snackbar
                Toast.makeText(this, effect.error.message, Toast.LENGTH_SHORT).show()
        }
    }
}
