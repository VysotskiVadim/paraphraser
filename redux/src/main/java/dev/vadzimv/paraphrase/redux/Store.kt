package dev.vadzimv.paraphrase.redux

class Store<TState>(
    initialState: TState,
    private val reducer: Reducer<TState>,
    middlewares: List<Middleware<TState>> = listOf()
): Dispatcher {

    private var _state: TState = initialState
    val state: TState get() = _state

    private var dispatcher: (Action) -> Unit = { action -> _state = reducer(_state, action) }

    init {
        setMiddlewares(middlewares)
    }

    override fun dispatch(action: Action) {
        dispatcher(action)
    }

    private fun setMiddlewares(middlewares: List<Middleware<TState>>) {
        middlewares.forEach {
            dispatcher = it(this)(dispatcher)
        }
    }
}
