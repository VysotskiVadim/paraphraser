package dev.vadzimv.paraphrase.redux.deprecated

import dev.vadzimv.paraphrase.redux.Action

interface ActionProcessor {
    fun processAction(action: Action)
}