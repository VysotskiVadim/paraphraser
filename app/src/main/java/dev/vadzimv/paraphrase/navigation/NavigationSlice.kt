package dev.vadzimv.paraphrase.navigation

import dev.vadzimv.paraphrase.redux.Action

data class NavigationState(
    val currentScreen: Screen,
    val handleBackButton: Boolean
)

fun createNavigationInitialState() = NavigationState(Screen.Chat, false)

sealed interface Screen {
    object Settings : Screen
    object Chat : Screen
}

sealed interface NavigationAction : Action {
    object OpenSettings : NavigationAction
    object OpenChat : NavigationAction
    object Back : NavigationAction
}


fun navigationReducer(state: NavigationState, action: Action): NavigationState =
    when (action) {
        is NavigationAction ->when (action) {
            NavigationAction.Back -> when (state.currentScreen) {
                Screen.Settings -> state.copy(currentScreen = Screen.Chat, handleBackButton = false)
                Screen.Chat -> state
            }
            NavigationAction.OpenSettings -> state.copy(currentScreen = Screen.Settings, handleBackButton = true)
            NavigationAction.OpenChat -> state.copy(currentScreen = Screen.Chat, handleBackButton = true)
        }
        else -> state
    }
