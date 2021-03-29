package com.luizssb.adidas.confirmed.viewcontroller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.luizssb.adidas.confirmed.databinding.ItemLoadstateGenericBinding
import com.luizssb.adidas.confirmed.utils.EventHandler

class GenericLoadStateViewHolder(
    private val binding: ItemLoadstateGenericBinding,
    retry: EventHandler<Unit>
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.buttonRetry.setOnClickListener { retry.invoke(Unit) }
    }

    fun bind(loadState: LoadState) {
        with(binding) {
            if (loadState is LoadState.Error) {
                errorMsg.text = loadState.error.localizedMessage
            }
            progress.isVisible = loadState is LoadState.Loading
            buttonRetry.isVisible = loadState is LoadState.Error
            errorMsg.isVisible = loadState is LoadState.Error
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: EventHandler<Unit>): GenericLoadStateViewHolder {
            val binding = ItemLoadstateGenericBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return GenericLoadStateViewHolder(binding, retry)
        }
    }
}
