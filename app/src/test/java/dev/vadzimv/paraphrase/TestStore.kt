package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.doubles.FakePlainTextClipboard
import dev.vadzimv.paraphrase.doubles.StubChat

fun createTestStore(
    chat: Chat = StubChat(),
    clipboard: Clipboard = FakePlainTextClipboard()
) = createStore(
    chat = chat,
    clipboard = clipboard
)