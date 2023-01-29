package dev.vadzimv.paraphrase.redux

class Store<TState>(
    initialState: TState,
    private val reducer: Reducer<TState>,
    middlewares: List<Middleware<TState>> = listOf()
) {

    private var _state: TState = initialState
    val state: TState get() = _state

    private var dispatcher: Dispatcher = { action -> _state = reducer(_state, action) }

    init {
        setMiddlewares(middlewares)
    }

    fun dispatch(action: Action) {
        dispatcher(action)
    }

    private fun setMiddlewares(middlewares: List<Middleware<TState>>) {
        middlewares.reversed().forEach {
            dispatcher = it(this)(dispatcher)
        }
    }
}
