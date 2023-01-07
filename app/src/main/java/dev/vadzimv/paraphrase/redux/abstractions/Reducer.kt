package dev.vadzimv.paraphrase.redux.abstractions

typealias Reducer<TState, TEffect> = (TState, TEffect) -> TState