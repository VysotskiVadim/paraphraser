package dev.vadzimv.paraphrase.redux.abstractions

import dev.vadzimv.paraphrase.Reducer

class Slice<TState, TAction: Action, TEffect>(
    val initialState: TState,
    val middleware: Middleware<TState, TAction, TEffect>,
    val reducer: Reducer<TState, TEffect>
)