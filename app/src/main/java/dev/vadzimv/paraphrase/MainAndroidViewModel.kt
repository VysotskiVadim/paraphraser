package dev.vadzimv.paraphrase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainAndroidViewModel : ViewModel() {
    val wrappedVm = MainViewModel(
        OpenAIParaphrasor(),
        viewModelScope
    )
}

class MainViewModel(
    private val paraphrasor: Paraphrasor,
    private val scope: CoroutineScope
): Actions {

    private val currentText: String? = null

    sealed interface State {
        object Empty : State
        object Loading : State
        object Error : State
        data class Ready(
            val paraphrasedText: String
        ): State
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
                    is ParaphraseResult.Success -> State.Ready(result.paraphrased)
                }
            }
        }
    }
}

interface Actions {
}