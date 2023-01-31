package dev.vadzimv.paraphrase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.vadzimv.paraphrase.mainscreendeprecated.createMainScreenSlice
import dev.vadzimv.paraphrase.navigation.createNavigationSlice
import dev.vadzimv.paraphrase.settings.createSettingsSlice

class AppViewModel(application: Application) : AndroidViewModel(application) {
    val store = Store(
        scope = viewModelScope,
        createMainScreenSlice(
            OpenAIParaphrasor(),
            AndroidClipboard(application)
        ),
        createNavigationSlice(),
        createSettingsSlice()
    )
}