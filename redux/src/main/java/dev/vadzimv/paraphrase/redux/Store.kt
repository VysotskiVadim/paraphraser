package dev.vadzimv.paraphrase.redux

typealias StateObserver<TState> = (TState) -> Unit

class Store<TState>(
    initialState: TState,
    private val reducer: Reducer<TState>,
    middlewares: List<Middleware<TState>> = listOf()
) {

    private var stateObserver: StateObserver<TState> = { }

    private var _state: TState = initialState
        set(value) {
            field = value
            stateObserver(value)
        }
    val state: TState get() = _state

    private var dispatcher: Dispatcher = { action -> _state = reducer(_state, action) }

    init {
        setMiddlewares(middlewares)
    }

    fun dispatch(action: Action) {
        dispatcher(action)
    }

    fun registerStateObserver(observer: (TState) -> Unit) {
        stateObserver = observer
    }

    fun unregisterStateObserver(observer: (TState) -> Unit) {
        if (stateObserver == observer) {
            stateObserver = { }
        }
    }

    private fun setMiddlewares(middlewares: List<Middleware<TState>>) {
        middlewares.reversed().forEach {
            dispatcher = it(this)(dispatcher)
        }
    }
}
