package com.example.nytviewer.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class ActionResult<State> {
    data class Success<State>(val state: State) : ActionResult<State>()
    data class Failure<State>(val error: String) : ActionResult<State>()
}

abstract class BaseViewModel<Action, State>(state: State) : ViewModel() {
    val state = MutableStateFlow(state)
    val processing = MutableStateFlow<Action?>(null)
    val error = MutableStateFlow<String?>(null)

    internal abstract suspend fun perform(action: Action): ActionResult<State>

    fun execute(action: Action) {
        viewModelScope.launch {
            processing.value = action
            when (val result = perform(action)) {
                is ActionResult.Success -> state.value = result.state
                is ActionResult.Failure -> error.value = result.error
            }
            processing.value = null
        }
    }
}