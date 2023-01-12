package dev.vadzimv.paraphrase.navigation

import dev.vadzimv.paraphrase.redux.abstractions.Action
import dev.vadzimv.paraphrase.redux.abstractions.Effect
import dev.vadzimv.paraphrase.redux.abstractions.Middleware
import dev.vadzimv.paraphrase.redux.abstractions.Slice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

typealias NavigationSlice = Slice<NavigationState, NavigationAction, NavigationAction>

fun createNavigationSlice() = Slice(
    NavigationState(Screen.Main),
    NavigationMiddleware(),
    ::navigationReducer
)

data class NavigationState(
    val currentScreen: Screen
)

sealed interface Screen {
    object Settings : Screen
    object Main : Screen
}

sealed interface NavigationAction : Action, Effect {
    object OpenSettings : NavigationAction
    object Back : NavigationAction
}


fun navigationReducer(state: NavigationState, effect: NavigationAction): NavigationState =
    when (effect) {
        NavigationAction.Back -> when (state.currentScreen) {
            Screen.Main -> state
            Screen.Settings -> state.copy(currentScreen = Screen.Main)
        }
        NavigationAction.OpenSettings -> state.copy(currentScreen = Screen.Settings)
    }

class NavigationMiddleware : Middleware<NavigationState, NavigationAction, NavigationAction> {
    override fun processAction(
        state: NavigationState,
        action: NavigationAction
    ): Flow<NavigationAction> {
        return flowOf(action)
    }
}