package dev.vadzimv.paraphrase.redux.abstractions

import kotlinx.coroutines.flow.Flow

interface Middleware<TState, TAction: Action, TEffect> {
    fun processAction(state: TState, action: TAction): Flow<TEffect>
}