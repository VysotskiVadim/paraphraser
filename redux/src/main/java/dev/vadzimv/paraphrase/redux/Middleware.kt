package dev.vadzimv.paraphrase.redux

typealias Middleware<State> = (store: Store<State>) -> (next: Dispatcher) -> (action: Action) -> Unit