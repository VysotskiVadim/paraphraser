package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.redux.Action
import dev.vadzimv.paraphrase.redux.Dispatcher
import dev.vadzimv.paraphrase.redux.GetState
import dev.vadzimv.paraphrase.redux.trunk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

typealias AuthRequestBlock = suspend (dispatch: Dispatcher, getState: GetState<AppState>) -> AuthenticatedActionResult

sealed interface AuthenticatedActionResult {
    object AuthOk : AuthenticatedActionResult
    object TokenError: AuthenticatedActionResult
}

fun authenticatedRequestAction(block: AuthRequestBlock): Action = trunk<AppState> { dispatch, getState ->
    GlobalScope.launch(Dispatchers.Unconfined) {
        val result = block(dispatch, getState)
        // TODO: refresh token?
    }
}