package dev.vadzimv.paraphrase

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

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
            val state by viewModel.state.collectAsState()
            MainScreen(state, viewModel)
        }
    }
}

@Composable
fun MainScreen(state: MainViewModel.State, actions: Actions) {
    when (state) {
        MainViewModel.State.Empty -> Text("Please pick a text to paraphrase")
        MainViewModel.State.Error -> Text("error")
        MainViewModel.State.Loading -> Text("paraphrasing...")
        is MainViewModel.State.Ready -> Text(state.paraphrasedText)
    }
}