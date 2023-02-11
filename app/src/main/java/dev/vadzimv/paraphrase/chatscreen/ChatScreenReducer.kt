package dev.vadzimv.paraphrase.chatscreen

import dev.vadzimv.paraphrase.redux.Action

fun chatScreenReducer(state: ChatScreenState, action: Action): ChatScreenState = when (action) {
    is ChatScreenActions -> {
        when (action) {
            is ChatScreenActions.UserInputUpdated -> state.copy(inputState = InputState(action.newValue))
            is ChatScreenActions.UserClickedSendQuestion -> state.copy(
                inputState = InputState(""),
                chatItems = state.chatItems + ChatItem.SentMessage(state.inputState.text),
            )
            else -> state
        }
    }
    is ChatScreenEffects -> {
        when (val effect = action) {
            is ChatScreenEffects.AskingQuestion -> state.copy(
                chatItems = state.chatItems + ChatItem.Loading,
                canSendQuestion = false,
            )
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