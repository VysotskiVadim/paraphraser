package dev.vadzimv.paraphrase.mainscreen

import dev.vadzimv.paraphrase.mainscreendeprecated.MainScreenEffect
import dev.vadzimv.paraphrase.mainscreendeprecated.MainScreenState
import dev.vadzimv.paraphrase.redux.Action

fun mainScreenReducer(state: MainScreenState, action: Action) = when (action) {
    is MainScreenEffect -> when (val effect: MainScreenEffect = action) {
        is MainScreenEffect.ParaphrasingStarted -> MainScreenState.Loading
        is MainScreenEffect.ParaphrasingCompleted -> MainScreenState.Ready(
            initialText = effect.initialText,
            paraphrasedText = effect.paraphrased
        )
        is MainScreenEffect.ParaphrasingFailed -> MainScreenState.Error
    }
    else -> state
}