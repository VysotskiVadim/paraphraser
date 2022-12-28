package dev.vadzimv.paraphrase

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            Surface() {
                MainScreen(state, viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(state: MainViewModel.State, actions: Actions) {
    Column(Modifier.padding(20.dp)) {
        val primaryFontSize = 25.sp
        val secondaryFontSize = 19.sp
        when (state) {
            MainViewModel.State.Empty -> {
                Text(
                    "Please pick a text to paraphrase. " +
                        "Select any text in other app click Open AI paraphrase",
                    fontSize = primaryFontSize
                )
            }
            MainViewModel.State.Error -> Text("Error", fontSize = primaryFontSize)

            MainViewModel.State.Loading -> Text("Paraphrasing...", fontSize = primaryFontSize)
            is MainViewModel.State.Ready -> {
                Text(text = "Initial text: ${state.initialText}", fontSize = secondaryFontSize)
                Text(text = "Paraphrased:", fontSize = primaryFontSize)
                Text(text = state.paraphrasedText, fontSize = primaryFontSize)
                Button(onClick = { actions.copyText() }) {
                    Text(text = "copy")
                }
            }
        }
    }
}