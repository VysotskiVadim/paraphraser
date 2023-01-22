package dev.vadzimv.paraphrase.redux.deprecated

import dev.vadzimv.paraphrase.redux.Action

class Slice<TState, TAction: Action, TEffect: Effect>(
    val initialState: TState,
    val middleware: Middleware<TState, TAction, TEffect>,
    val reducer: Reducer<TState, TEffect>
)