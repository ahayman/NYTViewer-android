package com.example.nytviewer.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.nytviewer.navigation.DestinationInfo
import com.example.nytviewer.navigation.NavAction
import com.example.nytviewer.navigation.NavDestination
import com.example.nytviewer.navigation.NavGraphInterface
import com.example.nytviewer.navigation.Navigator
import com.example.nytviewer.navigation.PopBehavior
import com.example.nytviewer.ui.common.NavBar
import com.example.nytviewer.utils.asLifecycleAwareState
import com.kiwi.navigationcompose.typed.composable
import com.kiwi.navigationcompose.typed.navigate
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    navigator: Navigator,
    navGraph: NavGraphInterface,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val navigatorState by navigator.navActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = NavDestination.ArticleList
    )

    /**
     * Given destination info and the current route, calculate the NavOptions
     * and navigate to the destination.
     */
    fun handleDestination(info: DestinationInfo, currentRoute: String?) {
        if (info.destination == NavDestination.Back) {
            navController.popBackStack()
            return
        }
        /**
         * Calculate the NavOptions based on the PopBehavior.
         */
        val navOptions: NavOptions? = when (val pop = info.popBehavior) {
            PopBehavior.None -> null
            PopBehavior.PopSelf -> NavOptions.Builder().setPopUpTo(currentRoute, true).build()
            PopBehavior.PopToRoot -> NavOptions.Builder().setPopUpTo(navController.graph.id, true).build()
            PopBehavior.PopIfSelf -> currentRoute?.let {
                if (info.destination.baseRoute == NavDestination.baseRoute(it) && info.destination.route != currentRoute)
                    NavOptions.Builder().setPopUpTo(currentRoute, true).build()
                else
                    null
            }
            is PopBehavior.PopTo ->
                NavOptions.Builder().setPopUpTo(pop.destination.route, pop.inclusive).build()
        }
        navController.navigate(info.destination, navOptions)
    }

    /**
     * Sets up the Navigator state observer to trigger Navigation when NavActions
     * are submitted to the teh Navigator.
     */
    LaunchedEffect(navigatorState) {
        (navigatorState as? NavAction)?.let { action ->
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            navGraph.destinationInfoForAction(action)?.let {
                handleDestination(it, currentRoute)
            }
        }
    }

    /**
     * Adding a back handler to intercept the standard back behavior and
     * pass it into the navigator instead. This causes the action to be processed through the nav
     * graph, which can decide which destination (if any) to navigate to.
     */
    BackHandler {
        navigator.handle(NavAction.OnBack)
    }

    NavHost(navController = navController, startDestination = NavDestination.ArticleList.route) {
        composable<NavDestination.ArticleList> {
        }
        composable<NavDestination.ArticleDetail> {

        }
    }
}
