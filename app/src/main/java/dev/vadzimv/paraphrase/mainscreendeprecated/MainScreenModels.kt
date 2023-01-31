package dev.vadzimv.paraphrase.mainscreen

import dev.vadzimv.paraphrase.Clipboard
import dev.vadzimv.paraphrase.ChatResponse
import dev.vadzimv.paraphrase.Chat
import dev.vadzimv.paraphrase.ChatRequest
import dev.vadzimv.paraphrase.redux.Action
import dev.vadzimv.paraphrase.redux.deprecated.Effect
import dev.vadzimv.paraphrase.redux.deprecated.Middleware
import dev.vadzimv.paraphrase.redux.deprecated.Slice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

typealias MainScreenSlice = Slice<MainScreenState, MainScreenAction, MainScreenEffect>

fun createMainScreenSlice(
    paraphrasor: Chat,
    clipboard: Clipboard,
): MainScreenSlice = Slice(
    initialState = MainScreenState.Empty,
    middleware = MainScreenMiddleware(paraphrasor, clipboard),
    reducer = ::mainScreenReducer
)

sealed interface MainScreenAction : dev.vadzimv.paraphrase.redux.Action {
    data class UserSelectedTextToParaphrase(val text: String?) : MainScreenAction
    object CopyText : MainScreenAction
}

sealed interface MainScreenEffect : Effect {
    object ParaphrasingStarted : MainScreenEffect
    object ParaphrasingFailed : MainScreenEffect
    data class ParaphrasingCompleted(val initialText: String, val paraphrased: String) :
        MainScreenEffect
}

class MainScreenMiddleware(
    private val chat: Chat,
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
                            when (val result = chat.request(ChatRequest(
                                text = "paraphrase: \"${action.text}\""
                            ))) {
                                ChatResponse.Error -> MainScreenEffect.ParaphrasingFailed
                                is ChatResponse.Success -> MainScreenEffect.ParaphrasingCompleted(
                                    action.text,
                                    result.reply
                                )
                            }
                        emit(nextAction)
                    }
                } else {
                    flow { }
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