package dev.vadzimv.paraphrase.chatscreen

import dev.vadzimv.paraphrase.redux.Action

sealed interface ChatScreenActions : Action {
    data class UserInputUpdated(val newValue: String): ChatScreenActions
    object UserClickedSendQuestion : ChatScreenActions
}

internal sealed interface ChatScreenEffects : Action {
    data class SuccessfulChatResponse(val response: String): ChatScreenEffects
    object FailedChatResponse: ChatScreenEffects
}

