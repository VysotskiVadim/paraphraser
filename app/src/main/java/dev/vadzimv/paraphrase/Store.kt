package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.chat.ChatScreenState
import dev.vadzimv.paraphrase.chat.chatScreenReducer
import dev.vadzimv.paraphrase.chat.createChatScreenMiddleware
import dev.vadzimv.paraphrase.chat.createDefaultChatScreenState
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
    val settingsState: SettingsState,
    val chatScreenState: ChatScreenState,
)

fun appReducer(state: AppState, action: Action): AppState {
    return state.copy(
        settingsState = settingsReducer(state.settingsState, action),
        navigationState = navigationReducer(state.navigationState, action),
        chatScreenState = chatScreenReducer(state.chatScreenState, action)
    )
}

fun createStore(
    chat: Chat,
    clipboard: Clipboard,
    keyValueStorage: KeyValueStorage
) = Store(
    AppState(
        createNavigationInitialState(),
        createSettingsInitialState(keyValueStorage),
        createDefaultChatScreenState()
    ),
    ::appReducer,
    listOf(
        createChatScreenMiddleware(chat),
        createSettingsMiddleware(keyValueStorage),
        trunkMiddleware()
    )
)

