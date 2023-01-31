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
import dev.vadzimv.paraphrase.mainscreendeprecated.MainScreenAction
import dev.vadzimv.paraphrase.navigation.NavigationAction
import dev.vadzimv.paraphrase.navigation.NavigationUI
import dev.vadzimv.paraphrase.theme.ParaphrasorTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val androidViewModel by viewModels<AppViewModel>()
    private val store get() = androidViewModel.store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.action == Intent.ACTION_PROCESS_TEXT) {
            val text = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
            store.processAction(MainScreenAction.UserSelectedTextToParaphrase(text))
        }
        handBackPress()
        setContent {
            ParaphrasorTheme {
                val state by store.state.collectAsState()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationUI(state, store)
                }
            }
        }
    }

    private fun handBackPress() {
        val callback:OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                store.processAction(NavigationAction.Back)
            }
        }
        onBackPressedDispatcher.addCallback(callback)
        lifecycleScope.launch {
            store.state.collect {
                callback.isEnabled = it.navigationState.handleBackButton
            }
        }
    }
}
