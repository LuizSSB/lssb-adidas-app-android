package com.luizssb.adidas.confirmed.view.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.luizssb.adidas.confirmed.R
import com.luizssb.adidas.confirmed.databinding.FragmentProductBinding
import com.luizssb.adidas.confirmed.utils.extensions.FlowEx.Companion.observeOnLifecycle
import com.luizssb.adidas.confirmed.viewmodel.product.ProductDetail
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.DefinitionParameters

class ProductFragment : Fragment() {
    private val args: ProductFragmentArgs by navArgs()

    private val detailViewModel: ProductDetail.ViewModel by viewModel {
        DefinitionParameters(listOf("FI444"))
    }

    private lateinit var layout: FragmentProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewModel.startOrResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layout = FragmentProductBinding.inflate(inflater, container, false)

        with(detailViewModel) {
            state.observe(viewLifecycleOwner, Observer(::render))
            effects.observeOnLifecycle(viewLifecycleOwner, ::render)
        }

        return layout.root
    }

    private fun render(state: ProductDetail.State) {
        with(layout) {
            containerToolbar.title = state.product?.name
            Glide.with(requireContext())
                    .load(state.product?.imgUrl)
                    .placeholder(R.mipmap.ic_launcher_desaturated)
                    .into(layout.imageHeader)
        }
    }

    private fun render(effect: ProductDetail.Effect) {

    }
}
