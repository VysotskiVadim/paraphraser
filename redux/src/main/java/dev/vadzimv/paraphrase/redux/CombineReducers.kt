package dev.vadzimv.paraphrase.redux

class ReducersCombinator<TRootState> internal constructor() {
    private val reducers = mutableListOf<Reducer<TRootState>>()

    fun <TSubState> reducer(
        reducer: Reducer<TSubState>,
        subStateSelector: (TRootState) -> TSubState,
        updater: (TRootState, TSubState) -> TRootState
    ) {
        reducers.add { state, action ->
            updater(state, reducer(subStateSelector(state), action))
        }
    }

    fun createReducer(): Reducer<TRootState> {
        return { state, action -> reducers.fold(state) { accumulatorState, reducer -> reducer(accumulatorState, action) } }
    }
}

fun <TRootState> combineReducers(block: ReducersCombinator<TRootState>.()-> Unit): Reducer<TRootState> {
    return ReducersCombinator<TRootState>().apply(block).createReducer()
}

