package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.doubles.FakeKeyValueStorage
import dev.vadzimv.paraphrase.doubles.FakePlainTextClipboard
import dev.vadzimv.paraphrase.doubles.StubChat

fun createTestStore(
    chat: Chat = StubChat(),
    clipboard: Clipboard = FakePlainTextClipboard(),
    keyValueStorage: KeyValueStorage = FakeKeyValueStorage()
) = createStore(
    chat = chat,
    clipboard = clipboard,
    keyValueStorage = keyValueStorage
)