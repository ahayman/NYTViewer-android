package com.example.nytviewer.ui.common

import androidx.lifecycle.ViewModel
import com.example.nytviewer.utils.StateReducer

sealed class ActionResult<State> {
    data class Success<State>(val state: State) : ActionResult<State>()
    data class Failure<State>(val error: String) : ActionResult<State>()
}

abstract class BaseViewModel<Action, State>(state: State) : StateReducer<Action, State>, ViewModel()