package com.luizssb.adidas.confirmed.viewmodel

abstract class MVIViewModel<TState, TEffects, TIntent>(initialValue: TState)
    : DefaultMVIController<TState, TEffects, TIntent>(initialValue) {
    private var started = false

    fun startOrResume() {
        if (!started) {
            start()
            started = true
        } else {
            // luizssb: triggers update
            setState { stateValue }
        }
    }

    protected abstract fun start()
}
