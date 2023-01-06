package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.mainscreen.MainScreenState
import dev.vadzimv.paraphrase.redux.abstractions.Action
import dev.vadzimv.paraphrase.redux.abstractions.Middleware
import dev.vadzimv.paraphrase.redux.abstractions.Slice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

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
    object ParaphrasingStarted : MainScreenAction
    object ParaphrasingFailed : MainScreenAction
    data class ParaphrasingCompleted(val initialText: String, val paraphrased: String) :
        MainScreenAction

    object CopyText : MainScreenAction
}

class MainScreenMiddleware(
    private val paraphrasor: Paraphrasor,
    private val clipboard: Clipboard
) : Middleware<MainScreenState, MainScreenAction> {
    override fun processAction(
        state: MainScreenState,
        action: MainScreenAction
    ): Flow<MainScreenAction> {
        return when (action) {
            is MainScreenAction.UserSelectedTextToParaphrase -> {
                if (action.text != null) {
                    flow {
                        emit(MainScreenAction.ParaphrasingStarted)
                        val nextAction =
                            when (val result = paraphrasor.paraphrase(action.text)) {
                                ParaphraseResult.Error -> MainScreenAction.ParaphrasingFailed
                                is ParaphraseResult.Success -> MainScreenAction.ParaphrasingCompleted(
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
            else -> flowOf(action)
        }
    }
}

fun mainScreenReducer(state: MainScreenState, action: MainScreenAction): MainScreenState {
    return when (action) {
        is MainScreenAction.ParaphrasingStarted -> MainScreenState.Loading
        is MainScreenAction.ParaphrasingCompleted -> MainScreenState.Ready(
            initialText = action.initialText,
            paraphrasedText = action.paraphrased
        )
        is MainScreenAction.ParaphrasingFailed -> MainScreenState.Error
        else -> state
    }
}