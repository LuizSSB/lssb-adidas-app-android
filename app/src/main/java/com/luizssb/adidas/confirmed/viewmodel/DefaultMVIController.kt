package com.luizssb.adidas.confirmed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class DefaultMVIController<TState, TEffects, TIntent>(initialValue: TState)
    : MVIController<TState, TEffects, TIntent>() {
    private val _state = MutableLiveData<TState>()
    final override val state: LiveData<TState> get() = _state

    val stateValue get() = state.value!!

    private val _effects = Channel<TEffects>(Channel.BUFFERED)
    final override val effects = _effects.receiveAsFlow()

    init {
        // luizssb: weird, a force unwrap should not be necessary here.
        // Prolly a bug in the IDE, but putting it anyway just to be safe
        _state.value = initialValue!!
    }

    protected fun setState(forceUpdate: Boolean = true, makeNewState: TState.() -> TState) {
        val newState = stateValue.makeNewState()
        if (forceUpdate || newState != stateValue) {
            _state.value = newState!!
        }
    }

    protected fun runEffect(effect: TEffects) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
