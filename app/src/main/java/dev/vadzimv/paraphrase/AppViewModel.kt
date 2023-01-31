package dev.vadzimv.paraphrase

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class AppViewModel(application: Application) : AndroidViewModel(application) {
    val store = createStore(
        chat = OpenAIParaphrasor(),
        clipboard = AndroidClipboard(application)
    )
}