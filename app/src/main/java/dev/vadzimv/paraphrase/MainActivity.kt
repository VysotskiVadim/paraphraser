package dev.vadzimv.paraphrase

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.vadzimv.paraphrase.theme.ParaphrasorTheme

class MainActivity : ComponentActivity() {

    private val androidViewModel by viewModels<MainAndroidViewModel>()
    private val viewModel get() = androidViewModel.wrappedVm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.action == Intent.ACTION_PROCESS_TEXT) {
            val text = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
            viewModel.userSelectedTextToParaphrase(text)
        }
        setContent {
            ParaphrasorTheme {
                val state by viewModel.state.collectAsState()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(state, viewModel)
                }
            }
        }
    }
}

