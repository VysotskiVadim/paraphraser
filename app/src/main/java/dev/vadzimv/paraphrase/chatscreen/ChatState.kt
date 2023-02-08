package dev.vadzimv.paraphrase.chatscreen

data class ChatScreenState(
    val chatItems: List<ChatItem>,
    val inputState: InputState
)

fun createDefaultChatScreenState() = ChatScreenState(
    emptyList(),
    InputState("")
)

data class InputState(val text: String)

sealed interface ChatItem {
    object Loading : ChatItem
    data class IncomingMessage(val text: String) : ChatItem
    data class SentMessage(val text: String) : ChatItem
    object RetriableError: ChatItem
}