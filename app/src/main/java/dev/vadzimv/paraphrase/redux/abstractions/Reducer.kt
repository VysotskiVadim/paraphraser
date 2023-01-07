package dev.vadzimv.paraphrase

typealias Reducer<TState, TEffect> = (TState, TEffect) -> TState