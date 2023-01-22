package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.mainscreen.MainScreenAction
import dev.vadzimv.paraphrase.mainscreen.MainScreenSlice
import dev.vadzimv.paraphrase.mainscreen.MainScreenState
import dev.vadzimv.paraphrase.navigation.NavigationAction
import dev.vadzimv.paraphrase.navigation.NavigationSlice
import dev.vadzimv.paraphrase.navigation.NavigationState
import dev.vadzimv.paraphrase.redux.Action
import dev.vadzimv.paraphrase.redux.deprecated.ActionProcessor
import dev.vadzimv.paraphrase.settings.SettingsSlice
import dev.vadzimv.paraphrase.settings.SettingsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AppState(
    val navigationState: NavigationState,
    val mainScreenState: MainScreenState,
    val settingsState: SettingsState,
)

class Store(
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    private val mainScreenSlice: MainScreenSlice,
    private val navigationSlice: NavigationSlice,
    private val settingsSlice: SettingsSlice
): ActionProcessor {

    private val _state = MutableStateFlow(
        AppState(
            navigationState = navigationSlice.initialState,
            mainScreenState = mainScreenSlice.initialState,
            settingsState = settingsSlice.initialState
        )
    )
    val state: StateFlow<AppState> get() = _state


    override fun processAction(action: dev.vadzimv.paraphrase.redux.Action) {
        scope.launch {
            when (action) {
                is MainScreenAction -> mainScreenSlice.middleware.processAction(_state.value.mainScreenState, action).collect {
                    _state.value = _state.value.copy(mainScreenState = mainScreenSlice.reducer(_state.value.mainScreenState, it))
                }
                is NavigationAction -> navigationSlice.middleware.processAction(_state.value.navigationState, action).collect {
                    _state.value = _state.value.copy(navigationState = navigationSlice.reducer(_state.value.navigationState, it))
                }
            }
        }
    }

}

