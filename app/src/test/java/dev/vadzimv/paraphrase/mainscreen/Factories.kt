package dev.vadzimv.paraphrase.mainscreen

import dev.vadzimv.paraphrase.Clipboard
import dev.vadzimv.paraphrase.Paraphrasor
import dev.vadzimv.paraphrase.doubles.FakePlainTextClipboard
import dev.vadzimv.paraphrase.doubles.StubParaphrasor

fun createTestMainScreenSlice(
    paraphrasor: Paraphrasor = StubParaphrasor(),
    clipboard: Clipboard = FakePlainTextClipboard()
) = createMainScreenSlice(
    paraphrasor = paraphrasor,
    clipboard = clipboard,
)