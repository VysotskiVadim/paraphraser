package dev.vadzimv.paraphrase.navigation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import dev.vadzimv.paraphrase.AppState
import dev.vadzimv.paraphrase.MainScreen
import dev.vadzimv.paraphrase.redux.abstractions.ActionProcessor

@Composable
fun NavigationUI(state: AppState, actionProcessor: ActionProcessor) {
    when (state.navigationState.currentScreen) {
        Screen.Main -> MainScreen(state.mainScreenState, actionProcessor)
        Screen.Settings -> Text(text = "not implemented")
    }
}