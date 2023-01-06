package dev.vadzimv.paraphrase.redux.abstractions

import dev.vadzimv.paraphrase.Reducer

class Slice<TState, TAction: Action>(
    val initialState: TState,
    val middleware: Middleware<TState, TAction>,
    val reducer: Reducer<TState, TAction>
)