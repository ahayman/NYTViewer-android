package com.example.nytviewer.navigation
import androidx.navigation.NamedNavArgument


sealed class PopBehavior {
    // [Default] Do not pop anything with the stack.
    data object None : PopBehavior()
    // Will always pop the current screen
    data object PopSelf : PopBehavior()
    // Will pop current screen if the base route is the same, but arguments are different
    data object PopIfSelf : PopBehavior()
    // Will pop everything off the stack. The Destination will become the new root screen.
    data object PopToRoot : PopBehavior()
    // Will pop to the destination provided, if it's in the stack. Otherwise, does nothing.
    data class PopTo(val destination: NavDestination, val inclusive: Boolean = false) : PopBehavior()
}

/**
 * All of the info necessary to route to a specific navigation destination.
 *
 * - [destination] The destination screen, as defined in the Destination sealed class.
 * - [popBehavior] (default: None) Defines whether and how to pop existing screens off the stack
 * before pushing the Destination.
 */
data class DestinationInfo(
    val destination: NavDestination,
    val popBehavior: PopBehavior = PopBehavior.None,
)
