package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.mainscreen.mainScreenMiddleware
import dev.vadzimv.paraphrase.mainscreen.mainScreenReducer
import dev.vadzimv.paraphrase.mainscreendeprecated.MainScreenState
import dev.vadzimv.paraphrase.navigation.NavigationState
import dev.vadzimv.paraphrase.navigation.createNavigationSlice
import dev.vadzimv.paraphrase.navigation.navigationReducer
import dev.vadzimv.paraphrase.redux.Action
import dev.vadzimv.paraphrase.redux.Store
import dev.vadzimv.paraphrase.settings.SettingsState
import dev.vadzimv.paraphrase.settings.createSettingsSlice
import dev.vadzimv.paraphrase.settings.settingsReducer

data class AppState(
    val navigationState: NavigationState,
    val mainScreenState: MainScreenState,
    val settingsState: SettingsState,
)

fun appReducer(state: AppState, action: Action): AppState {
    return state.copy(
        mainScreenState = mainScreenReducer(state.mainScreenState, action),
        settingsState = settingsReducer(state.settingsState, action),
        navigationState = navigationReducer(state.navigationState, action)
    )
}

fun createStore(
    chat: Chat,
    clipboard: Clipboard
) = Store(
    AppState(
        createNavigationSlice().initialState,
        MainScreenState.Empty,
        createSettingsSlice().initialState
    ),
    ::appReducer,
    listOf(
        mainScreenMiddleware(chat, clipboard)
    )
)

