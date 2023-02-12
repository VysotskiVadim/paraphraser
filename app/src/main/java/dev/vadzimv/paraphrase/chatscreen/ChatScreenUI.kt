package dev.vadzimv.paraphrase.chatscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.vadzimv.paraphrase.redux.Dispatcher

@Preview(showBackground = true)
@Composable
fun ChatScreenUIPreview() {
    ChatScreenUI(state = createDefaultChatScreenState(), dispatcher = { })
}

@Composable
fun ChatScreenUI(state: ChatScreenState, dispatcher: Dispatcher) {
    Column {
        LazyColumn {
            items(state.chatItems) {item ->
                when (item) {
                    ChatItem.Loading -> Text(text = "loading")
                    is ChatItem.ReceivedMessage -> Text(text = item.text)
                    ChatItem.RetriableError -> Text(text = "error")
                    is ChatItem.SentMessage -> Text(text = item.text)
                }

            }
        }
        Row {
            val inputState = state.inputState
            TextField(
                value = inputState.text,
                onValueChange = { dispatcher(ChatScreenActions.UserInputUpdated(it)) }
            )
            Button(onClick = { dispatcher(ChatScreenActions.UserClickedSendQuestion) }) {
                Text(text = "send")
            }
        }
    }
}