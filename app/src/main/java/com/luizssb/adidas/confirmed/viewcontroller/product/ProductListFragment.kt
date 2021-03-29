package com.luizssb.adidas.confirmed.viewcontroller.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.luizssb.adidas.confirmed.R
import com.luizssb.adidas.confirmed.databinding.FragmentProductListBinding
import com.luizssb.adidas.confirmed.utils.extensions.FlowEx.Companion.observeOnLifecycle
import com.luizssb.adidas.confirmed.viewcontroller.ListingViewControllerEx.Companion.justObserveListing
import com.luizssb.adidas.confirmed.viewcontroller.adapter.ProductsAdapter
import com.luizssb.adidas.confirmed.viewmodel.product.Products
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductListFragment : Fragment() {
    private lateinit var layout: FragmentProductListBinding

    private val itemAdapter by lazy {
        ProductsAdapter { viewModel.handleIntent(Products.Intent.Select(it)) }
    }

    private val viewModel by viewModel<Products.ViewModel>()

    private val navController by lazy {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_navhost)
        (navHostFragment  as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                }

        viewModel.let {
            it.state.observe(viewLifecycleOwner, Observer(::render))
            it.effects.observeOnLifecycle(viewLifecycleOwner, ::render)
        }

        justObserveListing(viewModel.listingController, layout.refresh, layout.list, itemAdapter)

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
                    viewModel.handleIntent(Products.Intent.ChangeSearchQuery(newText))
                    return true
                }
            })
            setOnCloseListener {
                viewModel.handleIntent(Products.Intent.ChangeSearchQuery(null))
                false
            }
        }
    }

    private fun render(state: Products.State) {
    }

    private fun render(effect: Products.Effect) {
        when(effect) {
            is Products.Effect.OpenProduct -> {
                val action = ProductListFragmentDirections.actionProductListFragmentToProductFragment(
                    effect.product.id
                )
                navController.navigate(action)
            }
        }
    }
}
