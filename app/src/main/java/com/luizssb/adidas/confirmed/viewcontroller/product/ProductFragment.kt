package com.luizssb.adidas.confirmed.viewcontroller.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.luizssb.adidas.confirmed.R
import com.luizssb.adidas.confirmed.databinding.DialogReviewBinding
import com.luizssb.adidas.confirmed.databinding.FragmentProductBinding
import com.luizssb.adidas.confirmed.dto.Rating
import com.luizssb.adidas.confirmed.utils.extensions.FlowEx.Companion.observeOnLifecycle
import com.luizssb.adidas.confirmed.utils.extensions.FragmentEx.Companion.enableActionBarBackButton
import com.luizssb.adidas.confirmed.utils.extensions.FragmentEx.Companion.setSupportActionBar
import com.luizssb.adidas.confirmed.utils.extensions.ImageViewEx.Companion.setRemoteImage
import com.luizssb.adidas.confirmed.utils.extensions.ProductEx.Companion.getCompleteName
import com.luizssb.adidas.confirmed.viewcontroller.CustomAlertDialog
import com.luizssb.adidas.confirmed.viewcontroller.ListingViewControllerEx.Companion.justObserveListing
import com.luizssb.adidas.confirmed.viewcontroller.adapter.ReviewsAdapter
import com.luizssb.adidas.confirmed.viewmodel.product.ProductDetail
import com.luizssb.adidas.confirmed.viewmodel.review.Reviews
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.DefinitionParameters


class ProductFragment : Fragment() {
    private val args: ProductFragmentArgs by navArgs()

    private val detailViewModel: ProductDetail.ViewModel by viewModel {
        DefinitionParameters(listOf(args.productId))
    }

    private val reviewsViewModel: Reviews.ViewModel by viewModel {
        DefinitionParameters(listOf(args.productId))
    }

    private lateinit var layout: FragmentProductBinding

    private val reviewsAdapter by lazy { ReviewsAdapter() }

    private val composeDialog by lazy {
        CustomAlertDialog(requireContext()) {
            DialogReviewBinding.inflate(it, layout.root, false)
                    .apply {
                        buttonSend.setOnClickListener {
                            reviewsViewModel.handleIntent(Reviews.Intent.SendReview(
                                    Rating.tryFrom(rating.rating.toInt()),
                                    editText.text?.toString()
                            ))
                        }
                    }
        }
                .apply {
                    setOnDismissListener { reviewsViewModel.handleIntent(Reviews.Intent.CancelComposing) }
                }
    }

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
                    buttonAddReview.setOnClickListener {
                        reviewsViewModel.handleIntent(Reviews.Intent.ComposeReview)
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

        justObserveListing(
            reviewsViewModel.listingController,
            layout.refreshReviews,
            layout.listReviews,
            reviewsAdapter
        )

        return layout.root
    }

    private fun render(state: ProductDetail.State) {
        with(layout) {
            state.product?.getCompleteName(requireContext())
                    .let {
                        toolbar.title = it
                        composeDialog.content.textName.text = it
                    }
            textPrice.text = state.product?.priceString
            textDescription.text = state.product?.description
            imageHeader.setRemoteImage(state.product?.imageUrl)
        }
    }

    private fun render(effect: ProductDetail.Effect) {
        when(effect) {
            is ProductDetail.Effect.ShowError -> {
                Toast.makeText(requireContext(), effect.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun render(state: Reviews.State) {
        composeDialog.isShowing = state.composingReview
    }

    private fun render(effect: Reviews.Effect) {
        when(effect) {
            is Reviews.Effect.ResetCompose -> composeDialog.content.run {
                rating.rating = 0f
                editText.text = null
            }

            is Reviews.Effect.WarnLackingData ->
                Toast.makeText(requireContext(), R.string.error_review_compose_nil_fields, Toast.LENGTH_SHORT).show()

            is Reviews.Effect.ShowError ->
                // TODO luizssb: replace with snackbar
                Toast.makeText(requireContext(), effect.error.message, Toast.LENGTH_SHORT).show()
        }
    }
}
