package dev.vadzimv.paraphrase.navigation

import dev.vadzimv.paraphrase.redux.Action
import dev.vadzimv.paraphrase.redux.deprecated.Effect
import dev.vadzimv.paraphrase.redux.deprecated.ForwardingMiddleware
import dev.vadzimv.paraphrase.redux.deprecated.Slice

typealias NavigationSlice = Slice<NavigationState, NavigationAction, NavigationAction>

fun createNavigationSlice(): NavigationSlice = Slice(
    NavigationState(Screen.Main, false),
    ForwardingMiddleware(),
    { s, a -> navigationReducer(s, a) }
)

data class NavigationState(
    val currentScreen: Screen,
    val handleBackButton: Boolean
)

sealed interface Screen {
    object Settings : Screen
    object Main : Screen
}

sealed interface NavigationAction : dev.vadzimv.paraphrase.redux.Action, Effect {
    object OpenSettings : NavigationAction
    object Back : NavigationAction
}


fun navigationReducer(state: NavigationState, action: Action): NavigationState =
    when (action) {
        is NavigationAction ->when (action) {
            NavigationAction.Back -> when (state.currentScreen) {
                Screen.Main -> state
                Screen.Settings -> state.copy(currentScreen = Screen.Main, handleBackButton = false)
            }
            NavigationAction.OpenSettings -> state.copy(currentScreen = Screen.Settings, handleBackButton = true)
        }
        else -> state
    }
