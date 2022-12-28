package dev.vadzimv.paraphrase

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

interface Clipboard {
    fun paste(text: String)
}

class AndroidClipboard(private val context: Context): Clipboard {

    override fun paste(text: String) {
        val data = ClipData.newPlainText(text, text)
        manager.setPrimaryClip(data)
    }

    private val manager get() =  context.getSystemService(ClipboardManager::class.java)

}