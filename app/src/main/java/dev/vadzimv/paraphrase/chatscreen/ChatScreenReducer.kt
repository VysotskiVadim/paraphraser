package dev.vadzimv.paraphrase.chatscreen

import dev.vadzimv.paraphrase.redux.Action

fun chatScreenReducer(state: ChatScreenState, action: Action): ChatScreenState = when (action) {
    is ChatScreenActions -> {
        when (action) {
            is ChatScreenActions.UserInputUpdated -> state.copy(
                inputState = InputState(action.newValue),
                canSendQuestion = action.newValue.isNotBlank()
            )
            else -> state
        }
    }
    is ChatScreenEffects -> {
        when (val effect = action) {
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
                    set(loadingIndex, ChatItem.ReceivedMessage(effect.response))
                },
                canSendQuestion = true
            )
            else -> state
        }
    }
    else -> state
}