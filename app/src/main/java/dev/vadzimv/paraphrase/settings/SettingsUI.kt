package dev.vadzimv.paraphrase.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import dev.vadzimv.paraphrase.redux.abstractions.ActionProcessor

@Composable
fun SettingsUI(settingsState: SettingsState, actionProcessor: ActionProcessor) {
    Column {
        Text(text = "screen is not implemented yet")
        Text(text = "Your token is: ${settingsState.chatSettings.openAIToken.substring(0, 4)}...")
    }
}