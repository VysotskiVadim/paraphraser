package dev.vadzimv.paraphrase.redux

class Store<TState>(
    initialState: TState,
    private val reducer: Reducer<TState>
): Dispatcher {

    private var _state: TState = initialState
    val state: TState get() = _state

    var dispatcher: (Action) -> Unit = { this.rootDispatcher(it) }

    override fun dispatch(action: Action) {
        dispatcher(action)
    }

    private fun rootDispatcher(action: Action) {
        _state = reducer(_state, action)
    }

    fun addMiddleware(middleware: Middleware<TState>) {
        dispatcher = middleware(this)(dispatcher)
    }
}
