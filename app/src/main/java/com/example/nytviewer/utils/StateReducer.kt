package com.example.nytviewer.utils

import kotlinx.coroutines.flow.StateFlow

interface StateReducer<Action, State> {
    val state: StateFlow<State>
    val error: StateFlow<String?>
    fun submit(action: Action)
    suspend fun execute(action: Action)
}