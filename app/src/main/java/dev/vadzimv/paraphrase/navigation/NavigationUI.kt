package dev.vadzimv.paraphrase.navigation

import androidx.compose.runtime.Composable
import dev.vadzimv.paraphrase.AppState
import dev.vadzimv.paraphrase.MainScreen
import dev.vadzimv.paraphrase.redux.abstractions.ActionProcessor
import dev.vadzimv.paraphrase.settings.SettingsUI

@Composable
fun NavigationUI(state: AppState, actionProcessor: ActionProcessor) {
    when (state.navigationState.currentScreen) {
        Screen.Main -> MainScreen(state.mainScreenState, actionProcessor)
        Screen.Settings -> SettingsUI(state.settingsState, actionProcessor)
    }
}