package dev.vadzimv.paraphrase.settings

import dev.vadzimv.paraphrase.AppState
import dev.vadzimv.paraphrase.KeyValueStorage
import dev.vadzimv.paraphrase.redux.Action
import dev.vadzimv.paraphrase.redux.Dispatcher
import dev.vadzimv.paraphrase.redux.Store
import dev.vadzimv.paraphrase.redux.middleware

fun createSettingsMiddleware(storage: KeyValueStorage) = middleware { store: Store<AppState>, next: Dispatcher, action: Action ->
    next(action)
    if (action is SettingsAction.Save) {
        storage.save("accessToken", store.state.chatSettingsSelector().openAIToken)
    }
}