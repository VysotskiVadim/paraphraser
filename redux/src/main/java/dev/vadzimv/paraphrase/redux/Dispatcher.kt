package dev.vadzimv.paraphrase.redux

fun interface Dispatcher {
    fun dispatch(action: Action)
}