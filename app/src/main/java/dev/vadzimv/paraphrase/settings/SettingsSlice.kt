package dev.vadzimv.paraphrase.settings

import dev.vadzimv.paraphrase.ChatSettings
import dev.vadzimv.paraphrase.redux.Action

sealed interface SettingsAction :Action {
    data class TokenUpdated(val newValue: String): SettingsAction
}

data class SettingsState(
    val chatSettings: ChatSettings
)

fun createSettingsInitialState() = SettingsState(ChatSettings())

fun settingsReducer(state: SettingsState, action: Action): SettingsState {
    return state
}