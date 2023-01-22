package dev.vadzimv.paraphrase.settings

import dev.vadzimv.paraphrase.ChatSettings
import dev.vadzimv.paraphrase.redux.Action
import dev.vadzimv.paraphrase.redux.deprecated.Effect
import dev.vadzimv.paraphrase.redux.deprecated.ForwardingMiddleware
import dev.vadzimv.paraphrase.redux.deprecated.Slice

typealias SettingsSlice = Slice<SettingsState, SettingsAction, SettingsAction>

fun createSettingsSlice(): SettingsSlice = Slice(
    SettingsState(ChatSettings()),
    ForwardingMiddleware(),
    ::settingsReducer
)

sealed interface SettingsAction : dev.vadzimv.paraphrase.redux.Action, Effect {
    data class TokenUpdated(val newValue: String): SettingsAction
}

data class SettingsState(
    val chatSettings: ChatSettings
)

fun settingsReducer(state: SettingsState, action: SettingsAction): SettingsState {
    return state
}