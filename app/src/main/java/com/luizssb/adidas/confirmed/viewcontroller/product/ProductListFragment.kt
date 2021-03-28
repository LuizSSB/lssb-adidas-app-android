package com.luizssb.adidas.confirmed.viewcontroller.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.luizssb.adidas.confirmed.R
import com.luizssb.adidas.confirmed.databinding.FragmentProductListBinding
import com.luizssb.adidas.confirmed.dto.Product
import com.luizssb.adidas.confirmed.utils.extensions.FlowEx.Companion.observeOnLifecycle
import com.luizssb.adidas.confirmed.viewcontroller.adapter.ProductsAdapter
import com.luizssb.adidas.confirmed.viewmodel.ListController
import com.luizssb.adidas.confirmed.viewmodel.product.ProductList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ProductListFragment : Fragment() {
    private lateinit var layout: FragmentProductListBinding

    private val itemAdapter by lazy {
        ProductsAdapter { viewModel.handleIntent(ProductList.Intent.Select(it)) }
    }

    private val viewModel by inject<ProductList.ViewModel>()

    private val navController by lazy {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_navhost)
        (navHostFragment  as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            itemAdapter.loadStateFlow.collectLatest {
                viewModel.listController.handleIntent(ListController.Intent.ChangeLoadState(it))
            }
        }

        viewModel.startOrResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = FragmentProductListBinding.inflate(inflater, container, false)
                .apply {
                    configureMenu(toolbar)

                    refresh.setOnRefreshListener { viewModel.listController.handleIntent(ListController.Intent.Refresh) }
                    list.adapter = itemAdapter
                }

        viewModel.let {
            it.state.observe(viewLifecycleOwner, Observer(::render))
            it.effects.observeOnLifecycle(viewLifecycleOwner, ::render)
        }

        viewModel.listController.let {
            it.state.observe(viewLifecycleOwner, Observer(::render))
            it.effects.observeOnLifecycle(viewLifecycleOwner, ::render)
        }

        return layout.root
    }

    // luizssb: the challenge's description described the app as being composed by two pages, so I am
    // choosing to not use Android's default approach to search, which requires extra activities and
    // a lot of other stuff, in favour of this simpler approach that is easier to implement.
    private fun configureMenu(toolbar: Toolbar) {
        toolbar.inflateMenu(R.menu.search_product)
        (toolbar.menu.findItem(R.id.action_search)?.actionView as? SearchView)?.run {
            queryHint = getString(R.string.hint_product_search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.handleIntent(ProductList.Intent.ChangeSearchQuery(newText))
                    return true
                }
            })
            setOnCloseListener {
                viewModel.handleIntent(ProductList.Intent.ChangeSearchQuery(null))
                false
            }
        }
    }

    private fun render(state: ProductList.State) {
    }

    private fun render(effect: ProductList.Effect) {
        when(effect) {
            is ProductList.Effect.OpenProduct -> {
                val action = ProductListFragmentDirections.actionProductListFragmentToProductFragment(
                    effect.product.id
                )
                navController.navigate(action)
            }
        }
    }

    private fun render(state: ListController.State<Product>) {
        layout.refresh.isRefreshing = state.loadingRefresh
        lifecycleScope.launch {
            itemAdapter.submitData(state.entries)
        }
    }

    private fun render(effect: ListController.Effect) {
        when(effect) {
            ListController.Effect.Refresh -> itemAdapter.refresh()

            is ListController.Effect.ShowError ->
                // TODO luizssb: replace with snackbar
                Toast.makeText(requireContext(), effect.error.message, Toast.LENGTH_SHORT).show()

//            is ProductList.Effect.OpenProduct -> {
//                val action = ProductListFragmentDirections.actionProductListFragmentToProductFragment(
//                    effect.product.id
//                )
//                navController.navigate(action)
//            }
        }
    }
}
