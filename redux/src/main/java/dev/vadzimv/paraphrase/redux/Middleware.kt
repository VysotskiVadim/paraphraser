package dev.vadzimv.paraphrase.redux

//fun interface Middleware<TState> {
//    fun next(
//        getState: () -> TState,
//        dispatcher: Dispatcher,
//        action: Action
//    )
//}

typealias Middleware<State> = (store: Store<State>) -> (next: Dispatcher) -> (action: Action) -> Unit
