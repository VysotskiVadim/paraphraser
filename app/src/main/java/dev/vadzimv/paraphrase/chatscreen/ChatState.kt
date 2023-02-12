package dev.vadzimv.paraphrase.chatscreen

import dev.vadzimv.paraphrase.AppState

fun AppState.getChatScreenState() = chatScreenState

data class ChatScreenState(
    val chatItems: List<ChatItem>,
    val inputState: InputState,
    val canSendQuestion: Boolean,
)

fun createDefaultChatScreenState() = ChatScreenState(
    emptyList(),
    InputState(""),
    false
)

data class InputState(val text: String)

sealed interface ChatItem {
    object Loading : ChatItem
    data class ClarifyActionForText(val text: String) : ChatItem
    data class ReceivedMessage(val text: String) : ChatItem
    data class SentMessage(val text: String) : ChatItem
    object RetriableError: ChatItem
}