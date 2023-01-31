package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.doubles.FakePlainTextClipboard
import dev.vadzimv.paraphrase.doubles.StubChat
import dev.vadzimv.paraphrase.mainscreen.mainScreenMiddleware
import dev.vadzimv.paraphrase.mainscreen.mainScreenReducer
import dev.vadzimv.paraphrase.mainscreendeprecated.MainScreenState
import dev.vadzimv.paraphrase.navigation.createNavigationSlice
import dev.vadzimv.paraphrase.navigation.navigationReducer
import dev.vadzimv.paraphrase.settings.createSettingsSlice
import dev.vadzimv.paraphrase.settings.settingsReducer

fun createTestStore(
    chat: Chat = StubChat(),
    clipboard: Clipboard = FakePlainTextClipboard()
) = dev.vadzimv.paraphrase.redux.Store<AppState>(
    AppState(
        createNavigationSlice().initialState,
        MainScreenState.Empty,
        createSettingsSlice().initialState
    ),
    { state, action ->
        state.copy(
            mainScreenState = mainScreenReducer(state.mainScreenState, action),
            settingsState = settingsReducer(state.settingsState, action),
            navigationState = navigationReducer(state.navigationState, action)
        )
    },
    listOf(
        mainScreenMiddleware(chat, clipboard)
    )
)