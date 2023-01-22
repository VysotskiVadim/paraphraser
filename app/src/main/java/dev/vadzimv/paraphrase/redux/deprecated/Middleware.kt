package dev.vadzimv.paraphrase.redux.deprecated

import dev.vadzimv.paraphrase.redux.Action
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