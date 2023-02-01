package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.redux.Action
import dev.vadzimv.paraphrase.redux.Dispatcher
import dev.vadzimv.paraphrase.redux.GetState
import dev.vadzimv.paraphrase.redux.trunk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

typealias AuthRequestBlock = suspend (token: String, dispatch: Dispatcher, getState: GetState<AppState>) -> Unit

fun authenticatedRequestAction(block: AuthRequestBlock): Action = trunk<AppState> { dispatch, getState ->
    GlobalScope.launch(Dispatchers.Unconfined) {
        block(getState().settingsState.chatSettings.openAIToken, dispatch, getState)
    }
}