package com.jordiphonedeveloper.digitalportfolio.core.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<Intent, State, Effect>(initialState: State) : ViewModel() {

    private val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<State> = mutableState.asStateFlow()

    private val effectsChannel = Channel<Effect>(capacity = Channel.BUFFERED)
    val effects = effectsChannel.receiveAsFlow()

    abstract fun onIntent(intent: Intent)

    protected fun updateState(reducer: State.() -> State) {
        mutableState.update(reducer)
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch { effectsChannel.send(effect) }
    }
}
