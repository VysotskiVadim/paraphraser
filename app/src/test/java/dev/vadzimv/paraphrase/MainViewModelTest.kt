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
        val viewModel = createViewModel(
            paraphrasor = DummuParaphrasor()
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
        val viewModel = createViewModel(
            paraphrasor = DummuParaphrasor(),
            clipboard = clipboard
        )
        viewModel.userSelectedTextToParaphrase("test")

        viewModel.copyText()

        assertEquals("paraphrased", clipboard.value)
    }
}

private fun createViewModel(
    paraphrasor: Paraphrasor = DummuParaphrasor(),
    clipboard: Clipboard = FakePlainTextClipboard()
) = MainViewModel(
    paraphrasor = paraphrasor,
    scope = TestScope(UnconfinedTestDispatcher()),
    clipboard = clipboard
)

class DummuParaphrasor : Paraphrasor {
    override suspend fun paraphrase(phrase: String): ParaphraseResult {
        return ParaphraseResult.Success("paraphrased")
    }
}

class FakePlainTextClipboard: Clipboard {

    var value: String? = null

    override fun paste(text: String) {
        value = text
    }

}