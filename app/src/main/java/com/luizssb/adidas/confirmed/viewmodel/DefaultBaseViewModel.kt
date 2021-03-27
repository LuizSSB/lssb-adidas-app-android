package com.luizssb.adidas.confirmed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class DefaultBaseViewModel<TStateModel, TEffects, TIntent>(defaultValue: TStateModel)
    : ViewModel(), BaseViewModel<TStateModel, TEffects, TIntent> {
    private val _state = MutableLiveData<TStateModel>()
    override val state: LiveData<TStateModel> get() = _state

    val stateValue get() = state.value!!

    final override val effects = MutableLiveData<TEffects>()

    init {
        // lbaglie: weird, a force unwrap should not be necessary here.
        _state.value = defaultValue!!
    }

    private var started = false

    final override fun startOrResume() {
        if (!started) {
            start()
            started = true
        } else {
            // luizssb: triggers update
            _state.value = state.value
        }
    }

    protected abstract fun start()

    protected fun setState(makeNewState: TStateModel.() -> TStateModel) {
        _state.value = stateValue.makeNewState()
    }
}
