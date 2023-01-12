package dev.vadzimv.paraphrase.mainscreen

import dev.vadzimv.paraphrase.Clipboard
import dev.vadzimv.paraphrase.Chat
import dev.vadzimv.paraphrase.doubles.FakePlainTextClipboard
import dev.vadzimv.paraphrase.doubles.StubChat
import dev.vadzimv.paraphrase.navigation.createNavigationSlice

fun createTestMainScreenSlice(
    paraphrasor: Chat = StubChat(),
    clipboard: Clipboard = FakePlainTextClipboard()
) = createMainScreenSlice(
    paraphrasor = paraphrasor,
    clipboard = clipboard,
)

fun createTestNavigationSlice() = createNavigationSlice()