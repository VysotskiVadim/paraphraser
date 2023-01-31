package dev.vadzimv.paraphrase.redux

typealias Middleware<State> = (store: Store<State>) -> (next: Dispatcher) -> (action: Action) -> Unit

fun <State> middleware(dispatch: (Store<State>, next: Dispatcher, action: Action) -> Unit): Middleware<State> =
    { store ->
        { next ->
            { action: Action ->
                dispatch(store, next, action)
            }
        }
    }