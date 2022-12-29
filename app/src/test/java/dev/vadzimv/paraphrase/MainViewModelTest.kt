package dev.vadzimv.paraphrase

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MainViewModelTest {

    @Test
    fun `initial state`() {
        val viewModel = createViewModel()
        val state = viewModel.state.value
        assertTrue("state is $state", state is MainViewModel.State.Empty)
    }

    @Test
    fun `successfully paraphrasing`() {
        val paraphrasorWaitHandle = CompletableDeferred<Unit>()
        val paraphrasor = StubParaphrasor().apply {
            setResult("paraphrased")
            setWaitHandle(paraphrasorWaitHandle)
        }
        val viewModel = createViewModel(
            paraphrasor = paraphrasor
        )

        viewModel.userSelectedTextToParaphrase("test")
        val stateAfterTextSelection = viewModel.state.value
        paraphrasorWaitHandle.complete(Unit)
        val stateAfterParaphrasingCompleted = viewModel.state.value

        assertTrue(
            "state is $stateAfterTextSelection",
            stateAfterTextSelection is MainViewModel.State.Loading
        )
        assertTrue(
            "state is $stateAfterParaphrasingCompleted",
            stateAfterParaphrasingCompleted is MainViewModel.State.Ready
        )
        stateAfterParaphrasingCompleted as MainViewModel.State.Ready
        assertEquals("paraphrased", stateAfterParaphrasingCompleted.paraphrasedText)
        assertEquals("test", stateAfterParaphrasingCompleted.initialText)
    }

    @Test
    fun `copy results`() {
        val clipboard = FakePlainTextClipboard()
        val paraphrasor = StubParaphrasor().apply {
            setResult("paraphrased")
        }
        val viewModel = createViewModel(
            paraphrasor = paraphrasor,
            clipboard = clipboard
        )
        viewModel.userSelectedTextToParaphrase("test")

        viewModel.copyText()

        assertEquals("paraphrased", clipboard.value)
    }

    @Test
    fun `error paraphrasing`() {
        val paraphrasor = StubParaphrasor().apply {
            setErrorResult()
        }
        val viewModel = createViewModel(
            paraphrasor = paraphrasor
        )

        viewModel.userSelectedTextToParaphrase("test")

        val state = viewModel.state.value
        assertTrue("state is $state", state is MainViewModel.State.Error)
    }
}

private fun createViewModel(
    paraphrasor: Paraphrasor = StubParaphrasor(),
    clipboard: Clipboard = FakePlainTextClipboard()
) = MainViewModel(
    paraphrasor = paraphrasor,
    scope = TestScope(UnconfinedTestDispatcher()),
    clipboard = clipboard
)

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