package dev.vadzimv.paraphrase.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.vadzimv.paraphrase.AppState
import dev.vadzimv.paraphrase.chat.ChatScreenUI
import dev.vadzimv.paraphrase.redux.Dispatcher
import dev.vadzimv.paraphrase.settings.SettingsUI

@Composable
fun NavigationUI(state: AppState, dispatcher: Dispatcher) {
    Column {
        TopAppBar(
            title = {
                Text(text = "Open AI")
            },
            actions = {
                Button(onClick = { dispatcher(NavigationAction.OpenSettings) }) {
                    Text(text = "Settings")
                }
            }
        )
        Surface(Modifier.fillMaxHeight()) {
            when (state.navigationState.currentScreen) {
                Screen.Settings -> SettingsUI(state.settingsState, dispatcher)
                Screen.Chat -> ChatScreenUI(state.chatScreenState, dispatcher)
            }
        }
    }
}