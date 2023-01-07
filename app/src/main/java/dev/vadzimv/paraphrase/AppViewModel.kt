package dev.vadzimv.paraphrase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

class AppViewModel(application: Application) : AndroidViewModel(application) {
    val store = Store(
        scope = viewModelScope,
        createMainScreenSlice(
            OpenAIParaphrasor(),
            AndroidClipboard(application)
        )
    )
}