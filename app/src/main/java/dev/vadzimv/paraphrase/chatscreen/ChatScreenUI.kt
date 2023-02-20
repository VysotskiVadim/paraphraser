package dev.vadzimv.paraphrase.chatscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.vadzimv.paraphrase.redux.Dispatcher

@Preview(showBackground = true)
@Composable
fun ChatScreenUIEmptyPreview() {
    ChatScreenUI(
        state = createDefaultChatScreenState(),
        dispatcher = { }
    )
}

@Preview(showBackground = true)
@Composable
fun ChatScreenUIPreviewLongText() {
    ChatScreenUI(
        state = createDefaultChatScreenState().run {
            copy(
                chatItems = listOf(
                    ChatItem.SentMessage("ping ".repeat(3)),
                    ChatItem.ReceivedMessage("pong ".repeat(4)),
                    ChatItem.SentMessage("ping ".repeat(20)),
                    ChatItem.ReceivedMessage("pong ".repeat(20))
                ),
                inputState = this.inputState.copy(
                    text = "This is" + "very ".repeat(20) + "long text"
                )
            )
        },
        dispatcher = { }
    )
}

@Composable
fun ChatScreenUI(state: ChatScreenState, dispatcher: Dispatcher) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn(Modifier.fillMaxWidth()) {
            items(state.chatItems) { item ->
                when (item) {
                    ChatItem.Loading -> Text(text = "loading")
                    is ChatItem.ReceivedMessage -> ReceivedMessage(item)
                    ChatItem.RetriableError -> Text(text = "error")
                    is ChatItem.SentMessage -> SentMessage(item)
                    is ChatItem.ClarifyActionForText ->
                        ReceivedMessage(ChatItem.ReceivedMessage(item.text + "\nWhat should I do with the text above? \uD83D\uDC46"))
                }
            }
        }
        Row(
            Modifier
                .weight(1f, false)
                .padding(5.dp)
        ) {
            val inputState = state.inputState
            TextField(
                value = inputState.text,
                onValueChange = { dispatcher(ChatScreenActions.UserInputUpdated(it)) },
                Modifier.weight(1f)
            )
            Button(
                onClick = { dispatcher(ChatScreenActions.UserClickedSendQuestion) },
                Modifier
                    .width(80.dp)
                    .align(Alignment.Bottom)
                    .padding(start = 5.dp)
            ) {
                Text(text = "send")
            }
        }
    }
}

@Composable
private fun ReceivedMessage(item: ChatItem.ReceivedMessage) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = item.text,
            Modifier
                .padding(10.dp)
                .padding(end = 20.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color.Magenta)
                .padding(10.dp)
                .align(Alignment.Start)
        )
    }
}

@Composable
private fun SentMessage(item: ChatItem.SentMessage) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = item.text,
            Modifier
                .padding(10.dp)
                .padding(start = 20.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color.Cyan)
                .padding(10.dp)
                .align(Alignment.End)
        )
    }
}