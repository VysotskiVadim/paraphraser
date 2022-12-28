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
        val viewModel = createViewModel()
        viewModel.userSelectedTextToParaphrase("test")
        val state = viewModel.state.value
        assertTrue("state is $state", state is MainViewModel.State.Ready)
        state as MainViewModel.State.Ready
        assertEquals("test", state.paraphrasedText)
    }
}

private fun createViewModel() = MainViewModel(
    DummuParaphrasor(),
    scope = TestScope(UnconfinedTestDispatcher())
)

class DummuParaphrasor : Paraphrasor {
    override suspend fun paraphrase(phrase: String): ParaphraseResult {
        return ParaphraseResult.Success(phrase)
    }
}