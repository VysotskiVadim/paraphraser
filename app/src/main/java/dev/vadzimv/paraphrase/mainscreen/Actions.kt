package dev.vadzimv.paraphrase.mainscreen


import dev.vadzimv.paraphrase.redux.Action

sealed interface MainScreenAction : dev.vadzimv.paraphrase.redux.Action {
    data class UserSelectedTextToParaphrase(val text: String?) : MainScreenAction
    object CopyText : MainScreenAction
}

sealed interface MainScreenEffect : Action {
    object ParaphrasingStarted : MainScreenEffect
    object ParaphrasingFailed : MainScreenEffect
    data class ParaphrasingCompleted(val initialText: String, val paraphrased: String) :
        MainScreenEffect
}