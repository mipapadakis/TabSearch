package com.minas.tabsearch.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewState<T>(val init: T) {

    private val _state = MutableStateFlow(init)
    private val state: StateFlow<T> = _state

    fun updateState(lambda: (T) -> T) {
        _state.update { lambda(_state.value) }
    }

    fun reInitialiseState() {
        _state.update {
            init
        }
    }

    fun getStateCurrentValue(): T {
        return _state.value
    }

    fun subscribeToState(viewLifecycleOwner: LifecycleOwner, actOnEvents: (T) -> Unit) {
        subscribeToStateG<T>(state, viewLifecycleOwner) { actOnEvents(it) }
    }
}

fun <T>subscribeToStateG(state: StateFlow<T>, viewLifecycleOwner: LifecycleOwner, actOnEvents: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            state.collect {
                actOnEvents(it)
            }
        }
    }
}