package dev.vadzimv.paraphrase

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import dev.vadzimv.paraphrase.mainscreen.MainScreenAction
import dev.vadzimv.paraphrase.navigation.NavigationAction
import dev.vadzimv.paraphrase.navigation.NavigationUI
import dev.vadzimv.paraphrase.redux.Action
import dev.vadzimv.paraphrase.redux.Store
import dev.vadzimv.paraphrase.theme.ParaphrasorTheme
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val androidViewModel by viewModels<AppViewModel>()
    private val store get() = androidViewModel.store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.action == Intent.ACTION_PROCESS_TEXT) {
            val text = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
            store.dispatch(MainScreenAction.UserSelectedTextToParaphrase(text))
        }
        handBackPress()
        setContent {
            ParaphrasorTheme {
                val state by store.flowableState().collectAsState(store.state)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationUI(state) { action: Action ->
                        store.dispatch(action)
                    }
                }
            }
        }
    }

    private fun handBackPress() {
        val callback:OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                store.dispatch(NavigationAction.Back)
            }
        }
        onBackPressedDispatcher.addCallback(callback)
        lifecycleScope.launch {
            store.flowableState().onStart { emit(store.state) }.collect {
                callback.isEnabled = it.navigationState.handleBackButton
            }
        }
    }
}

private fun Store<AppState>.flowableState() = callbackFlow<AppState> {
    val observer = { state: AppState ->
        this.trySend(state)
        Unit
    }
    this@flowableState.registerStateObserver(observer)
    awaitClose {
        this@flowableState.unregisterStateObserver(observer)
    }
}