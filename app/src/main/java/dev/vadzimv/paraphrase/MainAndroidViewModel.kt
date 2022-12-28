package dev.vadzimv.paraphrase

import android.app.Application
import android.provider.CalendarContract.Events
import androidx.compose.runtime.internal.composableLambdaInstance
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainAndroidViewModel(application: Application) : AndroidViewModel(application) {
    val wrappedVm = MainViewModel(
        OpenAIParaphrasor(),
        viewModelScope,
        AndroidClipboard(application)
    )
}

class MainViewModel(
    private val paraphrasor: Paraphrasor,
    private val scope: CoroutineScope,
    private val clipboard: Clipboard
) : Actions {

    private val currentText: String? = null

    sealed interface State {
        object Empty : State
        object Loading : State
        object Error : State
        data class Ready(
            val initialText: String,
            val paraphrasedText: String
        ) : State
    }

    private val _state = MutableStateFlow<State>(State.Empty)
    val state: StateFlow<State> = _state

    fun userSelectedTextToParaphrase(text: String?) {
        scope.launch {
            if (text != currentText && text != null && text.isNotBlank()) {
                _state.value = State.Loading
                val result = paraphrasor.paraphrase(text)
                _state.value = when (result) {
                    ParaphraseResult.Error -> State.Error
                    is ParaphraseResult.Success -> State.Ready(
                        text,
                        result.paraphrased
                    )
                }
            }
        }
    }

    override fun copyText() {
        val state = state.value
        if (state is State.Ready) {
            clipboard.paste(state.paraphrasedText)
        }
    }
}

interface Actions {
    fun copyText()
}