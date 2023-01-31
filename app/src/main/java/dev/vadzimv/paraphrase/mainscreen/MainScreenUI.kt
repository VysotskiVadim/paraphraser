package dev.vadzimv.paraphrase.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.vadzimv.paraphrase.navigation.NavigationAction
import dev.vadzimv.paraphrase.redux.Action
import dev.vadzimv.paraphrase.redux.Dispatcher

@Composable
fun MainScreen(state: MainScreenState, dispatcher: Dispatcher) {
    Column(Modifier.padding(20.dp)) {
        Button(onClick = { dispatcher(NavigationAction.OpenSettings) }) {
            Text(text = "Settings")
        }

        val primaryFontSize = 25.sp
        val secondaryFontSize = 17.sp
        when (state) {
            MainScreenState.Empty -> {
                Text(
                    "Please pick a text to paraphrase. " +
                        "Select any text in other app click Open AI paraphrase",
                    fontSize = primaryFontSize
                )
            }
            MainScreenState.Error -> Text("Error", fontSize = primaryFontSize)

            MainScreenState.Loading -> Text("Paraphrasing...", fontSize = primaryFontSize)
            is MainScreenState.Ready -> {
                Text(text = state.initialText, fontSize = secondaryFontSize)
                Text(
                    text = " Is paraphrased to:",
                    fontSize = secondaryFontSize,
                    modifier = Modifier.padding(top = 20.dp, bottom = 13.dp),
                )
                Text(text = state.paraphrasedText, fontSize = primaryFontSize)
                Button(onClick = { dispatcher(MainScreenAction.CopyText) }, Modifier.padding(top = 10.dp)) {
                    Text(text = "copy")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun readyStatePreview() = MainScreen(
    state = MainScreenState.Ready(
        "testing preview of the text",
        "paraphrased test"
    ),
    { }
)