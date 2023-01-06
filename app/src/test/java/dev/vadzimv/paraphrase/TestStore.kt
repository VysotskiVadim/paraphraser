package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.redux.abstractions.Action
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.TestCoroutineScope

class TestStore<TState, TAction: Action>(
    private val paraphrasor: Paraphrasor = StubParaphrasor(),
    private val clipboard: Clipboard = FakePlainTextClipboard(),
    private val stateSelector: (AppState) -> TState,
    ) {

    private val store = Store(
        scope = TestCoroutineScope(),
        createMainScreenSlice(
            paraphrasor, clipboard
        )
    )

    val state get(): StateFlow<TState> = store.state.map { stateSelector(it) }
        .stateIn(TestCoroutineScope(), SharingStarted.Eagerly, stateSelector(store.state.value))

    fun processAction(action: TAction) {
        store.processAction(action)
    }

}