package com.luizssb.adidas.confirmed.viewcontroller.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.luizssb.adidas.confirmed.databinding.FragmentProductBinding
import com.luizssb.adidas.confirmed.utils.extensions.FlowEx.Companion.observeOnLifecycle
import com.luizssb.adidas.confirmed.utils.extensions.FragmentEx.Companion.enableActionBarBackButton
import com.luizssb.adidas.confirmed.utils.extensions.FragmentEx.Companion.setSupportActionBar
import com.luizssb.adidas.confirmed.utils.extensions.ImageViewEx.Companion.setRemoteImage
import com.luizssb.adidas.confirmed.utils.extensions.ProductEx.Companion.getCompleteName
import com.luizssb.adidas.confirmed.viewcontroller.ListingViewControllerEx.Companion.observeListing
import com.luizssb.adidas.confirmed.viewcontroller.adapter.ReviewsAdapter
import com.luizssb.adidas.confirmed.viewmodel.product.ProductDetail
import com.luizssb.adidas.confirmed.viewmodel.review.ReviewList
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.DefinitionParameters


class ProductFragment : Fragment() {
    private val args: ProductFragmentArgs by navArgs()

    private val detailViewModel: ProductDetail.ViewModel by viewModel {
        DefinitionParameters(listOf(args.productId))
    }

    private val reviewsViewModel: ReviewList.ViewModel by viewModel {
        DefinitionParameters(listOf(args.productId))
    }

    private lateinit var layout: FragmentProductBinding

    private val reviewsAdapter by lazy { ReviewsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailViewModel.startOrResume()
        reviewsViewModel.startOrResume()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        layout = FragmentProductBinding.inflate(inflater, container, false)
                .apply {
                    setSupportActionBar(toolbar)
                    enableActionBarBackButton()
                    listReviews.adapter = reviewsAdapter
                    buttonAddReview.setOnClickListener {

                    }
                }

        with(detailViewModel) {
            state.observe(viewLifecycleOwner, Observer(::render))
            effects.observeOnLifecycle(viewLifecycleOwner, ::render)
        }

        with(reviewsViewModel) {
            state.observe(viewLifecycleOwner, Observer(::render))
            effects.observeOnLifecycle(viewLifecycleOwner, ::render)
        }

        observeListing(reviewsViewModel.listingController, layout.refreshReviews, reviewsAdapter)

        return layout.root
    }

    private fun render(state: ProductDetail.State) {
        with(layout) {
            toolbar.title = state.product?.getCompleteName(requireContext())
            textPrice.text = state.product?.priceString
            textDescription.text = state.product?.description
            imageHeader.setRemoteImage(state.product?.imgUrl)
        }
    }

    private fun render(effect: ProductDetail.Effect) {
        when(effect) {
            is ProductDetail.Effect.ShowError -> {
                Toast.makeText(requireContext(), effect.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun render(state: ReviewList.State) {
    }

    private fun render(effect: ReviewList.Effect) {
        when(effect) {
            is ReviewList.Effect.ShowError ->
                // TODO luizssb: replace with snackbar
                Toast.makeText(requireContext(), effect.error.message, Toast.LENGTH_SHORT).show()
        }
    }
}