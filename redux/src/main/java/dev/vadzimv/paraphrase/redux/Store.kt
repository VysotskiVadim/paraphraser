package dev.vadzimv.paraphrase.redux

class Store<TState>(
    initialState: TState
): Dispatcher {

    private val reducers = mutableListOf<Reducer<TState>>()

    private var _state: TState = initialState
    val state: TState get() = _state

    fun <TSubState> addSlice(
        reducer: Reducer<TSubState>,
        subStateSelector: (TState) -> TSubState,
        updater: (TState, TSubState) -> TState
    ) {
        reducers.add { state, action ->
            updater(state, reducer(subStateSelector(state), action))
        }
    }

    override fun dispatch(action: Action) {
        _state = reducers.fold(state) { state, reducer -> reducer(state, action) }
    }
}

