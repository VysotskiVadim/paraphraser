package dev.vadzimv.paraphrase.doubles

import dev.vadzimv.paraphrase.Clipboard

class FakePlainTextClipboard : Clipboard {
    var value: String? = null
    override fun paste(text: String) {
        value = text
    }
}