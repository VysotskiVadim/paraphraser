package dev.vadzimv.paraphrase.mainscreen

import dev.vadzimv.paraphrase.AppState
import dev.vadzimv.paraphrase.AuthenticatedActionResult
import dev.vadzimv.paraphrase.Chat
import dev.vadzimv.paraphrase.ChatRequest
import dev.vadzimv.paraphrase.ChatResponse
import dev.vadzimv.paraphrase.Clipboard
import dev.vadzimv.paraphrase.authenticatedRequestAction
import dev.vadzimv.paraphrase.redux.Middleware
import dev.vadzimv.paraphrase.redux.middleware
import dev.vadzimv.paraphrase.settings.chatSettingsSelector

fun mainScreenMiddleware(
    chat: Chat, // should this or specific trunk middleware accept dependencies?
    clipboard: Clipboard
): Middleware<AppState> =
    middleware { store, next, action ->
        when (action) {
            is MainScreenAction -> when (action) {
                MainScreenAction.CopyText -> {
                    val state = store.state.mainScreenStateSelector()
                    if (state is MainScreenState.Ready) {
                        clipboard.paste(state.paraphrasedText)
                    }
                }
                is MainScreenAction.UserSelectedTextToParaphrase -> {
                    if (action.text != null) {
                        next(paraphraseAction(chat, action.text))
                    }
                }
            }
            else -> next(action)
        }
    }

private fun paraphraseAction(
    chat: Chat,
    text: String
) = authenticatedRequestAction { dispatch, getState ->
    dispatch(MainScreenEffect.ParaphrasingStarted)
    val effect = when (val result = chat.request(
        ChatRequest(
            text = "paraphrase: \"${text}\"",
            getState().chatSettingsSelector()
        )
    )) {
        ChatResponse.Error -> MainScreenEffect.ParaphrasingFailed
        is ChatResponse.Success -> MainScreenEffect.ParaphrasingCompleted(
            text,
            result.reply
        )
    }
    dispatch(effect)
    AuthenticatedActionResult.AuthOk
}