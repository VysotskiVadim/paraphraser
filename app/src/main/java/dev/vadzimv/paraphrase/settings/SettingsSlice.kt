package dev.vadzimv.paraphrase.settings

import dev.vadzimv.paraphrase.redux.Action

sealed interface SettingsAction : Action {
    data class TokenUpdated(val newValue: String) : SettingsAction
    data class TemperatureChanged(val newValue: String) : SettingsAction
    data class MaxTokensChanged(val newValue: String) : SettingsAction
    object Save : SettingsAction
}

fun settingsReducer(state: SettingsState, action: Action) = if (action is SettingsAction) {
    when (action) {
        is SettingsAction.Save -> state.copy(chatSettings = state.chatSettings.copy(openAIToken = state.settingsUIState.accessToken.value))
        else -> state.copy(settingsUIState = settingsUiReducer(state.settingsUIState, action))
    }

} else state


fun settingsUiReducer(state: SettingsUIState, action: SettingsAction) = when (action) {
    is SettingsAction.MaxTokensChanged -> state
    SettingsAction.Save -> state
    is SettingsAction.TemperatureChanged -> state
    is SettingsAction.TokenUpdated -> state.copy(accessToken = state.accessToken.copy(value = action.newValue))
}