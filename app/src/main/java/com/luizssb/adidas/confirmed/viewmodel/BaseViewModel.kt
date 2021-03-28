package com.luizssb.adidas.confirmed.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface BaseViewModel<TStateModel, TEffects, TIntent> {
    val state: LiveData<TStateModel>
    val effects: Flow<TEffects>

    fun handleIntent(intent: TIntent)
    fun startOrResume()
}
