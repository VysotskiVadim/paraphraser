package dev.vadzimv.paraphrase.chat

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
        if (store.state.chatScreenState.inputState.text.isNotBlank()) {
            val input = store.state.getChatScreenState().inputState.text
            val clarifyQuestion = store.state.chatScreenState.chatItems.lastOrNull() as? ChatItem.ClarifyActionForText
            val question = if (clarifyQuestion != null) {
                input + " \"${clarifyQuestion.text}\""
            } else input
            if (question != input) {
                next(ChatScreenEffects.AskedQuestionAfterClarification(question))
            }
            next(askQuestionAction(chat, question))
        }
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