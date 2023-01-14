package dev.vadzimv.paraphrase.settings

import dev.vadzimv.paraphrase.ChatSettings
import dev.vadzimv.paraphrase.redux.abstractions.Action
import dev.vadzimv.paraphrase.redux.abstractions.Effect
import dev.vadzimv.paraphrase.redux.abstractions.ForwardingMiddleware
import dev.vadzimv.paraphrase.redux.abstractions.Slice

typealias SettingsSlice = Slice<SettingsState, SettingsAction, SettingsAction>

fun createSettingsSlice(): SettingsSlice = Slice(
    SettingsState(ChatSettings()),
    ForwardingMiddleware(),
    ::settingsReducer
)

sealed interface SettingsAction : Action, Effect {
    data class TokenUpdated(val newValue: String): SettingsAction
}

data class SettingsState(
    val chatSettings: ChatSettings
)

fun settingsReducer(state: SettingsState, action: SettingsAction): SettingsState {
    return state
}