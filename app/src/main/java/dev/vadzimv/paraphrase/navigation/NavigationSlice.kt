package dev.vadzimv.paraphrase.navigation

import dev.vadzimv.paraphrase.redux.Action

data class NavigationState(
    val currentScreen: Screen,
    val handleBackButton: Boolean
)

fun createNavigationInitialState() = NavigationState(Screen.Main, false)

sealed interface Screen {
    object Settings : Screen
    object Main : Screen
}

sealed interface NavigationAction : Action {
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
