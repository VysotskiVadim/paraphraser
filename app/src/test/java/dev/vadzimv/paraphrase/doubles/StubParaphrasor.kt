package dev.vadzimv.paraphrase.doubles

import dev.vadzimv.paraphrase.ParaphraseResult
import dev.vadzimv.paraphrase.Paraphrasor
import kotlinx.coroutines.Deferred

class StubParaphrasor : Paraphrasor {

    private var result: ParaphraseResult = ParaphraseResult.Success("paraphrased")
    private var waitHandle: Deferred<Unit>? = null

    fun setResult(paraphrasedText: String) {
        this.result = ParaphraseResult.Success(paraphrasedText)
    }

    fun setErrorResult() {
        this.result = ParaphraseResult.Error
    }

    fun setWaitHandle(handle: Deferred<Unit>) {
        this.waitHandle = handle
    }

    override suspend fun paraphrase(phrase: String): ParaphraseResult {
        waitHandle?.await()
        return result
    }
}