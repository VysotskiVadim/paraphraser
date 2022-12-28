package dev.vadzimv.paraphrase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(state: MainViewModel.State, actions: Actions) {
    Column(Modifier.padding(20.dp)) {
        val primaryFontSize = 25.sp
        val secondaryFontSize = 17.sp
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
                Text(text = state.initialText, fontSize = secondaryFontSize)
                Text(
                    text = " Is paraphrased to:",
                    fontSize = secondaryFontSize,
                    modifier = Modifier.padding(top = 20.dp, bottom = 13.dp),
                )
                Text(text = state.paraphrasedText, fontSize = primaryFontSize)
                Button(onClick = { actions.copyText() }, Modifier.padding(top = 10.dp)) {
                    Text(text = "copy")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun readyStatePreview() = MainScreen(
    state = MainViewModel.State.Ready(
        "testing preview of the text",
        "paraphrased test"
    ),
    actions = object : Actions {
        override fun copyText() {
            TODO("Not yet implemented")
        }
    }
)