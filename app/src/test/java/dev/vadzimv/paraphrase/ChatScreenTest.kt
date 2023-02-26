package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.chat.ChatItem
import dev.vadzimv.paraphrase.chat.ChatScreenActions
import dev.vadzimv.paraphrase.chat.getChatScreenState
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

        assertFalse(store.state.chatScreenState.canSendQuestion)
        store.dispatch(ChatScreenActions.UserInputUpdated("ping"))
        assertTrue(store.state.chatScreenState.canSendQuestion)
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

    @Test
    fun `user sends empty request`() {
        val store = createTestStore()
        store.dispatch(ChatScreenActions.UserInputUpdated(""))
        val initialChatState = store.state.chatScreenState

        store.dispatch(ChatScreenActions.UserClickedSendQuestion)

        assertEquals(initialChatState, store.state.chatScreenState)
    }

    @Test
    fun `user processes text from a different app`() {
        val chat = StubChat().apply {
            setResult("result")
        }
        val store = createTestStore(chat = chat)

        store.dispatch(ChatScreenActions.UserSelectedTestFromADifferentApp("test"))

        val textInsertedChatItems = store.state.chatScreenState.chatItems
        assertEquals("test", (textInsertedChatItems[0] as? ChatItem.ClarifyActionForText)?.text)
        assertEquals(1, textInsertedChatItems.size)

        store.dispatch(ChatScreenActions.UserInputUpdated("paraphrase"))
        store.dispatch(ChatScreenActions.UserClickedSendQuestion)

        val testProcessedItems = store.state.chatScreenState.chatItems
        assertEquals("paraphrase \"test\"", (testProcessedItems[1] as? ChatItem.SentMessage)?.text)
        assertEquals(
            "chat state $testProcessedItems",
            "result", (testProcessedItems[2] as? ChatItem.ReceivedMessage)?.text
        )
    }
}