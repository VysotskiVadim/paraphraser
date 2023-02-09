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
            is ChatScreenEffects.SuccessfulChatResponse -> state
            else -> state
        }
    }
    else -> state
}