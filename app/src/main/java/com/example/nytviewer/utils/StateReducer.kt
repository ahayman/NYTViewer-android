package com.example.nytviewer.utils

import kotlinx.coroutines.flow.StateFlow

/**
 * The StateReducer is the primary way state should be handled within the application.
 * Reducers are fairly simple, consisting of:
 *  - state: A StateFlow of the state being manipulated.
 *  - actions: All of the actions that can be applied to the state.
 *
 *  A State Reducer exposes state changes via the [state: StateFlow<State>] property.
 *  Action must be submitted either with the [submit] or [execute], after which the reducer
 *  should take the action and perform the necessary changes on the state.
 */
interface StateReducer<Action, State> {
    /**
     * The main state, exposed as a state flow. Action submitted to the reducer should
     * result in changes to the state that are emitted via this state flow property.
     * The only
     */
    val state: StateFlow<State>

    /**
     * If an error occurs, it should be emitted into the error StateFlow.
     * Errors should be _cleared_ when a new action is submitted.
     */
    val error: StateFlow<String?>

    /**
     * submit an action without waiting for it to complete.
     * Updates to state resulting from the action will still be emitted.
     */
    fun submit(action: Action)

    /**
     * execute an action and _wait_ for it to complete.
     * Requires a coroutine scope.
     */
    suspend fun execute(action: Action)
}