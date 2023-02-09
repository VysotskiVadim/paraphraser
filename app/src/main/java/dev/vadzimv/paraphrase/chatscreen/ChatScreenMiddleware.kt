package dev.vadzimv.paraphrase.chatscreen

import dev.vadzimv.paraphrase.AppState
import dev.vadzimv.paraphrase.AuthenticatedActionResult
import dev.vadzimv.paraphrase.Chat
import dev.vadzimv.paraphrase.ChatRequest
import dev.vadzimv.paraphrase.ChatResponse
import dev.vadzimv.paraphrase.authenticatedRequestAction
import dev.vadzimv.paraphrase.redux.Action
import dev.vadzimv.paraphrase.redux.Dispatcher
import dev.vadzimv.paraphrase.redux.Store
import dev.vadzimv.paraphrase.redux.middleware
import dev.vadzimv.paraphrase.settings.chatSettingsSelector

fun createChatScreenMiddleware(chat: Chat) = middleware { store: Store<AppState>, next: Dispatcher, action: Action ->
    if (action is ChatScreenActions.UserClickedSendQuestion) {
        next(askQuestionAction(chat, store.state.getChatScreenState().inputState.text))
    }
    next(action)
}

private fun askQuestionAction(
    chat: Chat,
    text: String
) = authenticatedRequestAction { dispatch, getState ->
    dispatch(ChatScreenEffects.AskingQuestion)
    val effect = when (val result = chat.request(
        ChatRequest(
            text = text,
            getState().chatSettingsSelector()
        )
    )) {
        ChatResponse.Error -> ChatScreenEffects.FailedChatResponse
        is ChatResponse.Success -> ChatScreenEffects.SuccessfulChatResponse(
            result.reply
        )
    }
    dispatch(effect)
    AuthenticatedActionResult.AuthOk
}