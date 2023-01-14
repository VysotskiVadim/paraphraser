package dev.vadzimv.paraphrase.redux.abstractions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface Middleware<TState, TAction : Action, TEffect : Effect> {
    fun processAction(state: TState, action: TAction): Flow<TEffect>
}

class ForwardingMiddleware<TState, TAction> :
    Middleware<TState, TAction, TAction> where TAction : Effect, TAction : Action {
    override fun processAction(state: TState, action: TAction): Flow<TAction> {
        return flowOf(action)
    }
}