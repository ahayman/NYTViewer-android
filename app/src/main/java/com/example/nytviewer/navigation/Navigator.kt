package com.example.nytviewer.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * An interface that defines the navigation actions that can be performed in the app.
 * The Navigator should be the point of contact that the rest of the app uses to
 * perform any navigation action.
 * The navActions property is a [StateFlow] that emits the current navigation action.
 * The `handle` function takes a [NavAction] and pushes it into the state flow.
 * The AppHost should subscribe to the nav actions and use the NavGraph to route the
 * app to the proper destination when a new nav action is presented.
 */
interface Navigator {
    val navActions: SharedFlow<NavAction?>
    fun handle(navAction: NavAction)
}

/**
 * An app specific implementation of [Navigator]. This implementation receives an action and emits
 * the result to the [navActions] [StateFlow]. The feature specific ViewModels can use this
 * navigator to request specific actions by calling [handle].The applications [AppNavHost]
 * observes this flow and performs the corresponding navigation. In this way, views and viewmodels
 * do not need a reference to the [NavController] or the [NavHostFragment].
 */
class AppNavigator : Navigator {

    private val _navActions: MutableSharedFlow<NavAction> by lazy {
        MutableSharedFlow(extraBufferCapacity = 1)
    }

    override val navActions: SharedFlow<NavAction> = _navActions
    override fun handle(navAction: NavAction) {
        _navActions.tryEmit(navAction)
    }
}