package dev.vadzimv.paraphrase.doubles

import dev.vadzimv.paraphrase.ChatResponse
import dev.vadzimv.paraphrase.Chat
import dev.vadzimv.paraphrase.ChatRequest
import kotlinx.coroutines.Deferred

class StubChat : Chat {

    private var result: ChatResponse = ChatResponse.Success("paraphrased")
    private var waitHandle: Deferred<Unit>? = null

    fun setResult(paraphrasedText: String) {
        this.result = ChatResponse.Success(paraphrasedText)
    }

    fun setErrorResult() {
        this.result = ChatResponse.Error
    }

    fun setWaitHandle(handle: Deferred<Unit>) {
        this.waitHandle = handle
    }

    override suspend fun request(request: ChatRequest): ChatResponse {
        waitHandle?.await()
        return result
    }
}