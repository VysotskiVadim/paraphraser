package dev.vadzimv.paraphrase.mainscreen

import dev.vadzimv.paraphrase.AppState
import dev.vadzimv.paraphrase.Chat
import dev.vadzimv.paraphrase.ChatRequest
import dev.vadzimv.paraphrase.ChatResponse
import dev.vadzimv.paraphrase.mainscreendeprecated.MainScreenAction
import dev.vadzimv.paraphrase.mainscreendeprecated.MainScreenEffect
import dev.vadzimv.paraphrase.redux.Dispatcher
import dev.vadzimv.paraphrase.redux.Middleware
import dev.vadzimv.paraphrase.redux.middleware
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun mainScreenMiddleware(chat: Chat): Middleware<AppState> = middleware { _, next, action ->
    when (action) {
        is MainScreenAction -> when (action) {
            MainScreenAction.CopyText -> TODO()
            is MainScreenAction.UserSelectedTextToParaphrase -> {
                if (action.text != null) {
                    next(MainScreenEffect.ParaphrasingStarted)
                    GlobalScope.launch(Dispatchers.Unconfined) {
                        val effect = when (val result = chat.request(
                            ChatRequest(
                                text = "paraphrase: \"${action.text}\""
                            )
                        )) {
                            ChatResponse.Error -> MainScreenEffect.ParaphrasingFailed
                            is ChatResponse.Success -> MainScreenEffect.ParaphrasingCompleted(
                                action.text,
                                result.reply
                            )
                        }
                        next(effect)
                    }
                }
            }
        }
        else -> next(action)
    }
}