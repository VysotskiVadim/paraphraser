package dev.vadzimv.paraphrase.redux

import java.util.concurrent.CopyOnWriteArrayList

typealias StateObserver<TState> = (TState) -> Unit

class Store<TState>(
    initialState: TState,
    private val reducer: Reducer<TState>,
    middlewares: List<Middleware<TState>> = listOf()
) {

    private var observers = CopyOnWriteArrayList<StateObserver<TState>>()

    private var _state: TState = initialState
        set(value) {
            field = value
            observers.forEach { it(value) }
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
        observers.add(observer)
    }

    fun unregisterStateObserver(observer: (TState) -> Unit) {
        observers.remove(observer)
    }

    private fun setMiddlewares(middlewares: List<Middleware<TState>>) {
        middlewares.reversed().forEach {
            dispatcher = it(this)(dispatcher)
        }
    }
}
