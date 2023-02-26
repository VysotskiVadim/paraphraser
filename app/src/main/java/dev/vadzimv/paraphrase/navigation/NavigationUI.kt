package dev.vadzimv.paraphrase.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.vadzimv.paraphrase.AppState
import dev.vadzimv.paraphrase.R
import dev.vadzimv.paraphrase.chat.ChatScreenUI
import dev.vadzimv.paraphrase.redux.Dispatcher
import dev.vadzimv.paraphrase.settings.SettingsUI

private const val ICON_SIZE = 30

@Composable
fun NavigationUI(state: AppState, dispatcher: Dispatcher) {
    val navigationState = state.navigationState
    Column {
        TopAppBar(
            navigationIcon = if (navigationState.handleBackButton) {
                {
                    IconButton(onClick = {
                        dispatcher(NavigationAction.Back)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_arrow_back),
                            "back",
                            Modifier.size(ICON_SIZE.dp)
                        )
                    }
                }
            } else null,
            title = {
                Text(text = "Open AI")
            },
            actions = {
                if (navigationState.showSettingInToolbar)
                    IconButton(onClick = { dispatcher(NavigationAction.OpenSettings) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_settings),
                            contentDescription = "settings",
                            Modifier.size(ICON_SIZE.dp)
                        )
                    }
            }
        )
        Surface(Modifier.fillMaxHeight()) {
            when (navigationState.currentScreen) {
                Screen.Settings -> SettingsUI(state.settingsState, dispatcher)
                Screen.Chat -> ChatScreenUI(state.chatScreenState, dispatcher)
            }
        }
    }
}