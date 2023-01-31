package dev.vadzimv.paraphrase.mainscreen

sealed interface MainScreenState {
    object Empty : MainScreenState
    object Loading : MainScreenState
    object Error : MainScreenState
    data class Ready(
        val initialText: String,
        val paraphrasedText: String
    ) : MainScreenState
}