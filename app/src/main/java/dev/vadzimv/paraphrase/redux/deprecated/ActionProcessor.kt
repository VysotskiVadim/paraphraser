package dev.vadzimv.paraphrase.redux.deprecated

import dev.vadzimv.paraphrase.redux.Action

fun interface ActionProcessor {
    fun processAction(action: Action)
}