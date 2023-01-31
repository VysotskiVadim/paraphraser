package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.doubles.FakePlainTextClipboard
import dev.vadzimv.paraphrase.doubles.StubChat
import dev.vadzimv.paraphrase.mainscreen.mainScreenStateSelector
import dev.vadzimv.paraphrase.mainscreendeprecated.MainScreenAction
import dev.vadzimv.paraphrase.mainscreendeprecated.MainScreenState
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MainScreenTest {

    @Test
    fun `initial state`() {
        val store = createTestStore()
        val state = store.state.mainScreenStateSelector()
        assertTrue("state is $state", state is MainScreenState.Empty)
    }

    @Test
    fun `paraphrasing null`() {
        val store = createTestStore()
        store.dispatch(MainScreenAction.UserSelectedTextToParaphrase(null))
        val state = store.state.mainScreenStateSelector()
        assertTrue("state is $state", state is MainScreenState.Empty)
    }

    @Test
    fun `successfully paraphrasing`() {
        val chatWaitHandle = CompletableDeferred<Unit>()
        val stubChat = StubChat().apply {
            setResult("paraphrased")
            setWaitHandle(chatWaitHandle)
        }
        val store = createTestStore(
            chat = stubChat
        )

        store.dispatch(MainScreenAction.UserSelectedTextToParaphrase("test"))
        val stateAfterTextSelection = store.state.mainScreenStateSelector()
        chatWaitHandle.complete(Unit)
        val stateAfterParaphrasingCompleted = store.state.mainScreenStateSelector()

        assertTrue(
            "state is $stateAfterTextSelection",
            stateAfterTextSelection is MainScreenState.Loading
        )
        assertTrue(
            "state is $stateAfterParaphrasingCompleted",
            stateAfterParaphrasingCompleted is MainScreenState.Ready
        )
        stateAfterParaphrasingCompleted as MainScreenState.Ready
        assertEquals("paraphrased", stateAfterParaphrasingCompleted.paraphrasedText)
        assertEquals("test", stateAfterParaphrasingCompleted.initialText)
    }

    @Test
    fun `copy results`() {
        val clipboard = FakePlainTextClipboard()
        val chat = StubChat().apply {
            setResult("paraphrased")
        }
        val store = createTestStore(
            chat = chat,
            clipboard = clipboard
        )

        store.dispatch(MainScreenAction.UserSelectedTextToParaphrase("test"))
        store.dispatch(MainScreenAction.CopyText)

        assertEquals("paraphrased", clipboard.value)
    }

    @Test
    fun `error paraphrasing`() {
        val chat = StubChat().apply {
            setErrorResult()
        }
        val store = createTestStore(
            chat = chat
        )

        store.dispatch(MainScreenAction.UserSelectedTextToParaphrase("test"))

        val state = store.state.mainScreenStateSelector()
        assertTrue("state is $state", state is MainScreenState.Error)
    }
}
