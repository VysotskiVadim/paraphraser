package dev.vadzimv.paraphrase.navigation

import androidx.compose.runtime.Composable
import dev.vadzimv.paraphrase.AppState
import dev.vadzimv.paraphrase.mainscreen.MainScreen
import dev.vadzimv.paraphrase.redux.Dispatcher
import dev.vadzimv.paraphrase.settings.SettingsUI

@Composable
fun NavigationUI(state: AppState, dispatcher: Dispatcher) {
    when (state.navigationState.currentScreen) {
        Screen.Main -> MainScreen(state.mainScreenState, dispatcher)
        Screen.Settings -> SettingsUI(state.settingsState, dispatcher)
    }
}