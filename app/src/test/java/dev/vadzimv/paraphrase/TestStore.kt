package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.mainscreen.MainScreenSlice
import dev.vadzimv.paraphrase.mainscreen.createTestMainScreenSlice
import dev.vadzimv.paraphrase.redux.abstractions.Action
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.TestCoroutineScope

class TestStore<TState, TAction : Action>(
    mainScreenSlice: MainScreenSlice = createTestMainScreenSlice(),
    val stateSelector: (AppState) -> TState,
    ) {

    private val store = Store(
        scope = TestCoroutineScope(),
        mainScreenSlice
    )

    val state
        get(): StateFlow<TState> = store.state.map { stateSelector(it) }
            .stateIn(TestCoroutineScope(), SharingStarted.Eagerly, stateSelector(store.state.value))

    fun processAction(action: TAction) {
        store.processAction(action)
    }

}