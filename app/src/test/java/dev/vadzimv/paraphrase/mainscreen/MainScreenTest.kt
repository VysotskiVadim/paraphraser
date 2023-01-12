package dev.vadzimv.paraphrase.mainscreen

import dev.vadzimv.paraphrase.TestStore
import dev.vadzimv.paraphrase.doubles.FakePlainTextClipboard
import dev.vadzimv.paraphrase.doubles.StubChat
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MainScreenTest {

    @Test
    fun `initial state`() {
        val store = createTestMainScreenSlice().toStore()
        val state = store.state.value
        assertTrue("state is $state", state is MainScreenState.Empty)
    }

    @Test
    fun `paraphrasing null`() {
        val store = createTestMainScreenSlice().toStore()
        store.processAction(MainScreenAction.UserSelectedTextToParaphrase(null))
        val state = store.state.value
        assertTrue("state is $state", state is MainScreenState.Empty)
    }

    @Test
    fun `successfully paraphrasing`() {
        val paraphrasorWaitHandle = CompletableDeferred<Unit>()
        val paraphrasor = StubChat().apply {
            setResult("paraphrased")
            setWaitHandle(paraphrasorWaitHandle)
        }
        val store = createTestMainScreenSlice(
            paraphrasor = paraphrasor
        ).toStore()

        store.processAction(MainScreenAction.UserSelectedTextToParaphrase("test"))
        val stateAfterTextSelection = store.state.value
        paraphrasorWaitHandle.complete(Unit)
        val stateAfterParaphrasingCompleted = store.state.value

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
        val paraphrasor = StubChat().apply {
            setResult("paraphrased")
        }
        val store = createTestMainScreenSlice(
            paraphrasor = paraphrasor,
            clipboard = clipboard
        ).toStore()

        store.processAction(MainScreenAction.UserSelectedTextToParaphrase("test"))
        store.processAction(MainScreenAction.CopyText)

        assertEquals("paraphrased", clipboard.value)
    }

    @Test
    fun `error paraphrasing`() {
        val paraphrasor = StubChat().apply {
            setErrorResult()
        }
        val store = createTestMainScreenSlice(
            paraphrasor = paraphrasor
        ).toStore()

        store.processAction(MainScreenAction.UserSelectedTextToParaphrase("test"))

        val state = store.state.value
        assertTrue("state is $state", state is MainScreenState.Error)
    }
}

fun MainScreenSlice.toStore() = TestStore<MainScreenState, MainScreenAction>(
    mainScreenSlice = this
) { it.mainScreenState }