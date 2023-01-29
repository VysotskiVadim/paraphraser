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
        middlewares.forEach {
            addMiddleware(it)
        }
    }

    override fun dispatch(action: Action) {
        dispatcher(action)
    }

    private fun addMiddleware(middleware: Middleware<TState>) {
        dispatcher = middleware(this)(dispatcher)
    }
}
