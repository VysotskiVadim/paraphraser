package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.mainscreen.mainScreenMiddleware
import dev.vadzimv.paraphrase.mainscreen.mainScreenReducer
import dev.vadzimv.paraphrase.mainscreen.MainScreenState
import dev.vadzimv.paraphrase.navigation.NavigationState
import dev.vadzimv.paraphrase.navigation.createNavigationInitialState
import dev.vadzimv.paraphrase.navigation.navigationReducer
import dev.vadzimv.paraphrase.redux.Action
import dev.vadzimv.paraphrase.redux.Store
import dev.vadzimv.paraphrase.redux.trunkMiddleware
import dev.vadzimv.paraphrase.settings.SettingsState
import dev.vadzimv.paraphrase.settings.createSettingsInitialState
import dev.vadzimv.paraphrase.settings.createSettingsMiddleware
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
    clipboard: Clipboard,
    keyValueStorage: KeyValueStorage
) = Store(
    AppState(
        createNavigationInitialState(),
        MainScreenState.Empty,
        createSettingsInitialState(keyValueStorage)
    ),
    ::appReducer,
    listOf(
        mainScreenMiddleware(chat, clipboard),
        createSettingsMiddleware(keyValueStorage),
        trunkMiddleware()
    )
)

