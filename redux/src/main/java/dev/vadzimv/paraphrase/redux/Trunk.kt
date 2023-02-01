package dev.vadzimv.paraphrase.redux

typealias GetState<TState> = () -> TState

typealias TrunkBlock<TState> = (dispatch: Dispatcher, getState: GetState<TState>) -> Unit

fun <TState> trunk(block: TrunkBlock<TState>): Action = TrunkAction(block)

fun <TState : Any> trunkMiddleware(): Middleware<TState> = middleware { store: Store<TState>, next: Dispatcher, action: Action ->
    val trunkAction= action as? TrunkAction<TState>
    if (trunkAction != null) {
        trunkAction.block(store::dispatch, store::state)
    } else {
        next(action)
    }
}

private class TrunkAction<TState>(
    val block: TrunkBlock<TState>
) : Action