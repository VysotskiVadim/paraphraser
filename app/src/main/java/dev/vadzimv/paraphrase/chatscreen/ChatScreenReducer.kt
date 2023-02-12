package dev.vadzimv.paraphrase.chatscreen

import dev.vadzimv.paraphrase.redux.Action

fun chatScreenReducer(state: ChatScreenState, action: Action): ChatScreenState = when (action) {
    is ChatScreenActions -> {
        when (action) {
            is ChatScreenActions.UserInputUpdated -> state.copy(
                inputState = InputState(action.newValue),
                canSendQuestion = action.newValue.isNotBlank()
            )
            is ChatScreenActions.UserSelectedTestFromADifferentApp -> if (action.text != null) {
                state.copy(
                    chatItems = state.chatItems + ChatItem.ClarifyActionForText(action.text)
                )
            } else state
            else -> state
        }
    }
    is ChatScreenEffects -> {
        when (action) {
            is ChatScreenEffects.AskingQuestion -> {
                if (state.inputState.text.isNotBlank()) {
                    state.copy(
                        inputState = InputState(""),
                        chatItems = state.chatItems + ChatItem.SentMessage(state.inputState.text) + ChatItem.Loading,
                        canSendQuestion = false,
                    )
                } else {
                    state
                }
            }
            is ChatScreenEffects.SuccessfulChatResponse -> state.copy(
                chatItems = state.chatItems.toMutableList().apply {
                    val loadingIndex = indexOfFirst { it is ChatItem.Loading }
                    set(loadingIndex, ChatItem.ReceivedMessage(action.response))
                },
                canSendQuestion = true
            )
            is ChatScreenEffects.AskedQuestionAfterClarification -> state.copy(
                chatItems = state.chatItems + ChatItem.SentMessage(action.question) + ChatItem.Loading,
                inputState = InputState(""),
                canSendQuestion = false, //TODO: fix duplication with regular loading
            )
            is ChatScreenEffects.FailedChatResponse -> state.copy(
                chatItems = state.chatItems + ChatItem.RetriableError
            )
        }
    }
    else -> state
}