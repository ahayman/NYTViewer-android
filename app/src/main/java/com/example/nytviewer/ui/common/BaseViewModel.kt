package com.example.nytviewer.ui.common

import androidx.lifecycle.ViewModel
import com.example.nytviewer.utils.StateReducer

/**
 * All View Models should inherit from BaseViewModel. This is a simple way to ensure that
 * the view model is:
 *  - A StateReducer: The required way to handle state for all view models.
 *  - A ViewModel
 */
abstract class BaseViewModel<Action, State>() : StateReducer<Action, State>, ViewModel()