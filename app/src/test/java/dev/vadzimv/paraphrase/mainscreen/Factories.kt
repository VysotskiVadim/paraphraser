package dev.vadzimv.paraphrase.mainscreen

import dev.vadzimv.paraphrase.Clipboard
import dev.vadzimv.paraphrase.Paraphrasor
import dev.vadzimv.paraphrase.doubles.FakePlainTextClipboard
import dev.vadzimv.paraphrase.doubles.StubParaphrasor
import dev.vadzimv.paraphrase.navigation.createNavigationSlice

fun createTestMainScreenSlice(
    paraphrasor: Paraphrasor = StubParaphrasor(),
    clipboard: Clipboard = FakePlainTextClipboard()
) = createMainScreenSlice(
    paraphrasor = paraphrasor,
    clipboard = clipboard,
)

fun createTestNavigationSlice() = createNavigationSlice()