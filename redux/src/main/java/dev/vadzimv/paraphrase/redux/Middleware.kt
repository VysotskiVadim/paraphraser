package dev.vadzimv.paraphrase.redux

fun interface Middleware<TState> {
    fun next(
        getState: () -> TState,
        dispatcher: Dispatcher,
        action: Action
    )
}