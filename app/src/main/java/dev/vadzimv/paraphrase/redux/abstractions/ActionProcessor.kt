package dev.vadzimv.paraphrase.redux.abstractions

interface ActionProcessor {
    fun processAction(action: Action)
}