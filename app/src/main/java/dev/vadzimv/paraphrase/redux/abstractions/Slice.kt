package dev.vadzimv.paraphrase.redux.abstractions

class Slice<TState, TAction: Action, TEffect: Effect>(
    val initialState: TState,
    val middleware: Middleware<TState, TAction, TEffect>,
    val reducer: Reducer<TState, TEffect>
)