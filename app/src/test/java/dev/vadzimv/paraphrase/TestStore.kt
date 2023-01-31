package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.mainscreendeprecated.MainScreenSlice
import dev.vadzimv.paraphrase.navigation.NavigationSlice
import dev.vadzimv.paraphrase.settings.SettingsSlice
import dev.vadzimv.paraphrase.settings.createSettingsSlice
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.TestCoroutineScope

class TestStore<TState, TAction : dev.vadzimv.paraphrase.redux.Action>(
    mainScreenSlice: MainScreenSlice = createTestMainScreenSlice(),
    navigationScreenSlice: NavigationSlice = createTestNavigationSlice(),
    settingsSlice: SettingsSlice = createSettingsSlice(),
    private val stateSelector: (AppState) -> TState,
) {

    private val store = Store(
        scope = TestCoroutineScope(),
        mainScreenSlice,
        navigationScreenSlice,
        settingsSlice
    )

    val state
        get(): StateFlow<TState> = store.state.map { stateSelector(it) }
            .stateIn(TestCoroutineScope(), SharingStarted.Eagerly, stateSelector(store.state.value))

    fun processAction(action: TAction) {
        store.processAction(action)
    }

}