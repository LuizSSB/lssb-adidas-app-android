package com.luizssb.adidas.confirmed.viewcontroller.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.luizssb.adidas.confirmed.utils.EventHandler

class GenericLoadStateAdapter(private val retry: EventHandler<Unit>)
    : LoadStateAdapter<GenericLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: GenericLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): GenericLoadStateViewHolder {
        return GenericLoadStateViewHolder.create(parent, retry)
    }
}
