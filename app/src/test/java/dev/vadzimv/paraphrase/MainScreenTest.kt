package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.mainscreen.MainScreenState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MainScreenTest {

    @Test
    fun `initial state`() {
        val store = createTestStore()
        val state = store.state.value
        assertTrue("state is $state", state is MainScreenState.Empty)
    }

    @Test
    fun `paraphrasing null`() {
        val store = createTestStore()
        store.processAction(MainScreenAction.UserSelectedTextToParaphrase(null))
        val state = store.state.value
        assertTrue("state is $state", state is MainScreenState.Empty)
    }

    @Test
    fun `successfully paraphrasing`() {
        val paraphrasorWaitHandle = CompletableDeferred<Unit>()
        val paraphrasor = StubParaphrasor().apply {
            setResult("paraphrased")
            setWaitHandle(paraphrasorWaitHandle)
        }
        val store = createTestStore(
            paraphrasor = paraphrasor
        )

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
        val paraphrasor = StubParaphrasor().apply {
            setResult("paraphrased")
        }
        val store = createTestStore(
            paraphrasor = paraphrasor,
            clipboard = clipboard
        )

        store.processAction(MainScreenAction.UserSelectedTextToParaphrase("test"))
        store.processAction(MainScreenAction.CopyText)

        assertEquals("paraphrased", clipboard.value)
    }

    @Test
    fun `error paraphrasing`() {
        val paraphrasor = StubParaphrasor().apply {
            setErrorResult()
        }
        val store = createTestStore(
            paraphrasor = paraphrasor
        )

        store.processAction(MainScreenAction.UserSelectedTextToParaphrase("test"))

        val state = store.state.value
        assertTrue("state is $state", state is MainScreenState.Error)
    }
}

fun createTestStore(
    paraphrasor: Paraphrasor = StubParaphrasor(),
    clipboard: Clipboard = FakePlainTextClipboard()
): TestStore<MainScreenState, MainScreenAction> {
    return TestStore(paraphrasor, clipboard) { it.mainScreenState }
}

class StubParaphrasor : Paraphrasor {

    private var result: ParaphraseResult = ParaphraseResult.Success("paraphrased")
    private var waitHandle: Deferred<Unit>? = null

    fun setResult(paraphrasedText: String) {
        this.result = ParaphraseResult.Success(paraphrasedText)
    }

    fun setErrorResult() {
        this.result = ParaphraseResult.Error
    }

    fun setWaitHandle(handle: Deferred<Unit>) {
        this.waitHandle = handle
    }

    override suspend fun paraphrase(phrase: String): ParaphraseResult {
        waitHandle?.await()
        return result
    }
}

class FakePlainTextClipboard : Clipboard {
    var value: String? = null
    override fun paste(text: String) {
        value = text
    }
}