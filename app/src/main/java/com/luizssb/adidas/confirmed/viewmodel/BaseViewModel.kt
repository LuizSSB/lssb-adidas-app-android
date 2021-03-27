package com.luizssb.adidas.confirmed.viewmodel

import androidx.lifecycle.LiveData

interface BaseViewModel<TStateModel, TEffects, TIntent> {
    val state: LiveData<TStateModel>
    val effects: LiveData<TEffects>

    fun handleIntent(intent: TIntent)
    fun startOrResume()
}
