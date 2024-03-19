package com.example.nytviewer.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> Flow<T>.asLifecycleAwareState(lifecycleOwner: LifecycleOwner, initialState: T) =
    lifecycleAwareState(lifecycleOwner, this, initialState)

/**
 * A convenience extension to convert a [Flow] to a Composable [State] that is lifecycle aware.
 */
@Composable
fun <T> lifecycleAwareState(
    lifecycleOwner: LifecycleOwner,
    flow: Flow<T>,
    initialState: T
): State<T> {
    val lifecycleAwareStateFlow = remember(flow, lifecycleOwner) {
        flow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    return lifecycleAwareStateFlow.collectAsState(initialState)
}