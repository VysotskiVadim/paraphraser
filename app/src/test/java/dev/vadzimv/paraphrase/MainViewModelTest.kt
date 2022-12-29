package dev.vadzimv.paraphrase

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
        assertTrue("state is $state", state is MainViewModel.State.Empty,)
    }

    @Test
    fun `successfully paraphrasing`() {
        val paraphrasor = StubParaphrasor().apply {
            setResult("paraphrased")
        }
        val viewModel = createViewModel(
            paraphrasor = paraphrasor
        )

        viewModel.userSelectedTextToParaphrase("test")

        val state = viewModel.state.value
        assertTrue("state is $state", state is MainViewModel.State.Ready)
        state as MainViewModel.State.Ready
        assertEquals("paraphrased", state.paraphrasedText)
        assertEquals("test", state.initialText)
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
    fun setResult(paraphrasedText: String) {
        this.result = ParaphraseResult.Success(paraphrasedText)
    }
    override suspend fun paraphrase(phrase: String): ParaphraseResult {
        return result
    }
}

class FakePlainTextClipboard: Clipboard {
    var value: String? = null
    override fun paste(text: String) {
        value = text
    }
}