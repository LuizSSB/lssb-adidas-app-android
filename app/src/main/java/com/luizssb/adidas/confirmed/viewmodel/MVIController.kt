package com.luizssb.adidas.confirmed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

abstract class MVIController<TState, TEffects, TIntent> : ViewModel() {
    abstract val state: LiveData<TState>
    abstract val effects: Flow<TEffects>
    abstract fun handleIntent(intent: TIntent)
}
