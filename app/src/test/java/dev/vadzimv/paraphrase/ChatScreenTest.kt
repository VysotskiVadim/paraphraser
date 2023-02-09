package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.chatscreen.ChatItem
import dev.vadzimv.paraphrase.chatscreen.ChatScreenActions
import dev.vadzimv.paraphrase.chatscreen.getChatScreenState
import dev.vadzimv.paraphrase.doubles.StubChat
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.assertEquals
import org.junit.Test

class ChatScreenTest {

    @Test
    fun `user types question`() {
        val store = createTestStore()
        store.dispatch(ChatScreenActions.UserInputUpdated("t"))
        store.dispatch(ChatScreenActions.UserInputUpdated("te"))
        store.dispatch(ChatScreenActions.UserInputUpdated("tes"))
        store.dispatch(ChatScreenActions.UserInputUpdated("test"))

        assertEquals("test", store.state.getChatScreenState().inputState.text)
    }

    @Test
    fun `user asks question and gets reply`() {
        val waitHandle = CompletableDeferred<Unit>()
        val chat = StubChat().apply {
            setResult("pong")
            setWaitHandle(waitHandle)
        }
        val store = createTestStore(
            chat = chat
        )

        store.dispatch(ChatScreenActions.UserInputUpdated("ping"))
        store.dispatch(ChatScreenActions.UserClickedSendQuestion)

        assertEquals("", store.state.chatScreenState.inputState.text)
        val chatLoadingStateItems = store.state.getChatScreenState().chatItems
        assertEquals("ping", (chatLoadingStateItems[0] as? ChatItem.SentMessage)?.text)
    }
}