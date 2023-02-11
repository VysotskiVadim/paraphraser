package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.chatscreen.ChatItem
import dev.vadzimv.paraphrase.chatscreen.ChatScreenActions
import dev.vadzimv.paraphrase.chatscreen.ChatScreenState
import dev.vadzimv.paraphrase.chatscreen.getChatScreenState
import dev.vadzimv.paraphrase.doubles.StubChat
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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
    fun `user asks questions and gets replies`() {
        val waitHandle = CompletableDeferred<Unit>()
        val chat = StubChat().apply {
            setResult("pong")
            setWaitHandle(waitHandle)
        }
        val store = createTestStore(
            chat = chat
        )

        assertTrue(store.state.chatScreenState.canSendQuestion)
        store.dispatch(ChatScreenActions.UserInputUpdated("ping"))
        store.dispatch(ChatScreenActions.UserClickedSendQuestion)

        assertEquals("", store.state.chatScreenState.inputState.text)
        val chatLoadingStateItems = store.state.getChatScreenState().chatItems
        assertEquals("ping", (chatLoadingStateItems[0] as? ChatItem.SentMessage)?.text)
        assertTrue(chatLoadingStateItems[1] is ChatItem.Loading)
        assertFalse(store.state.chatScreenState.canSendQuestion)

        waitHandle.complete(Unit)
        val chatLoadedState = store.state.chatScreenState.chatItems
        assertEquals("ping", (chatLoadedState[0] as? ChatItem.SentMessage)?.text)
        assertEquals("pong", (chatLoadedState[1] as? ChatItem.ReceivedMessage)?.text)
        assertTrue(store.state.chatScreenState.canSendQuestion)

        store.dispatch(ChatScreenActions.UserInputUpdated("ping"))
        store.dispatch(ChatScreenActions.UserClickedSendQuestion)

        val moreQuestionsState = store.state.chatScreenState.chatItems
        assertEquals("ping", (moreQuestionsState[0] as? ChatItem.SentMessage)?.text)
        assertEquals("pong", (moreQuestionsState[1] as? ChatItem.ReceivedMessage)?.text)
        assertEquals("ping", (moreQuestionsState[2] as? ChatItem.SentMessage)?.text)
        assertEquals("pong", (moreQuestionsState[3] as? ChatItem.ReceivedMessage)?.text)
        assertTrue(store.state.chatScreenState.canSendQuestion)
    }
}