package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.mainscreen.MainScreenState
import dev.vadzimv.paraphrase.redux.abstractions.Action
import dev.vadzimv.paraphrase.redux.abstractions.ActionProcessor
import dev.vadzimv.paraphrase.redux.abstractions.Slice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AppState(
    val navigationState: NavigationState,
    val mainScreenState: MainScreenState
)

sealed interface NavigationState {
    object MainScreen : NavigationState
}

class Store(
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
    private val mainScreenSlice: Slice<MainScreenState, MainScreenAction>
): ActionProcessor {

    private val _state = MutableStateFlow(
        AppState(
            navigationState = NavigationState.MainScreen,
            mainScreenState = mainScreenSlice.initialState
        )
    )
    val state: StateFlow<AppState> get() = _state


    override fun processAction(action: Action) {
        scope.launch {
            when (action) {
                is MainScreenAction -> mainScreenSlice.middleware.processAction(_state.value.mainScreenState, action).collect {
                    _state.value = _state.value.copy(mainScreenState = mainScreenSlice.reducer(_state.value.mainScreenState, it))
                }
            }
        }
    }

}

