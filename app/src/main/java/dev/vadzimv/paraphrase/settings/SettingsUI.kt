package dev.vadzimv.paraphrase.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.vadzimv.paraphrase.redux.Dispatcher

@Composable
fun SettingsUI(settingsState: SettingsState, dispatcher: Dispatcher) {
    val settingsUI = settingsState.settingsUIState
    SettingsScreen(settingsUI, dispatcher)
}

@Composable
private fun SettingsScreen(
    settingsUI: SettingsUIState,
    dispatcher: Dispatcher
) {
    Column {
        Text(text = "Open AI API access token:")
        TextField(
            value = settingsUI.accessToken.value,
            onValueChange = {
                dispatcher(SettingsAction.TokenUpdated(it))
            },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { dispatcher(SettingsAction.Save) }, Modifier.fillMaxWidth()) {
            Text("Save settings")
        }
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() =
    SettingsScreen(settingsUI = previewSettingsUIState(), dispatcher = { })