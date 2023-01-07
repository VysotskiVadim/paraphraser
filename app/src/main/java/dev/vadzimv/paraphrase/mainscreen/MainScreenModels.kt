package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.mainscreen.MainScreenState
import dev.vadzimv.paraphrase.redux.abstractions.Action
import dev.vadzimv.paraphrase.redux.abstractions.Middleware
import dev.vadzimv.paraphrase.redux.abstractions.Slice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun createMainScreenSlice(
    paraphrasor: Paraphrasor,
    clipboard: Clipboard,
) = Slice(
    initialState = MainScreenState.Empty,
    middleware = MainScreenMiddleware(paraphrasor, clipboard),
    reducer = ::mainScreenReducer
)

sealed interface MainScreenAction : Action {
    data class UserSelectedTextToParaphrase(val text: String?) : MainScreenAction
    object CopyText : MainScreenAction
}

sealed interface MainScreenEffect {
    object ParaphrasingStarted : MainScreenEffect
    object ParaphrasingFailed : MainScreenEffect
    data class ParaphrasingCompleted(val initialText: String, val paraphrased: String) :
        MainScreenEffect
}

class MainScreenMiddleware(
    private val paraphrasor: Paraphrasor,
    private val clipboard: Clipboard
) : Middleware<MainScreenState, MainScreenAction, MainScreenEffect> {
    override fun processAction(
        state: MainScreenState,
        action: MainScreenAction
    ): Flow<MainScreenEffect> {
        return when (action) {
            is MainScreenAction.UserSelectedTextToParaphrase -> {
                if (action.text != null) {
                    flow {
                        emit(MainScreenEffect.ParaphrasingStarted)
                        val nextAction =
                            when (val result = paraphrasor.paraphrase(action.text)) {
                                ParaphraseResult.Error -> MainScreenEffect.ParaphrasingFailed
                                is ParaphraseResult.Success -> MainScreenEffect.ParaphrasingCompleted(
                                    action.text,
                                    result.paraphrased
                                )
                            }
                        emit(nextAction)
                    }
                } else {
                    flow {  }
                }
            }
            is MainScreenAction.CopyText -> {
                if (state is MainScreenState.Ready) {
                    clipboard.paste(state.paraphrasedText)
                }
                flow { }
            }
        }
    }
}

fun mainScreenReducer(state: MainScreenState, effect: MainScreenEffect): MainScreenState {
    return when (effect) {
        is MainScreenEffect.ParaphrasingStarted -> MainScreenState.Loading
        is MainScreenEffect.ParaphrasingCompleted -> MainScreenState.Ready(
            initialText = effect.initialText,
            paraphrasedText = effect.paraphrased
        )
        is MainScreenEffect.ParaphrasingFailed -> MainScreenState.Error
        else -> state
    }
}