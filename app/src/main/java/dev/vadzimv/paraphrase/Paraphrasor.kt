package dev.vadzimv.paraphrase

import com.theokanning.openai.OpenAiService
import com.theokanning.openai.completion.CompletionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface Paraphrasor {
    suspend fun paraphrase(phrase: String): ParaphraseResult
}

sealed interface ParaphraseResult {
    data class Success(
        val paraphrased: String
    ) : ParaphraseResult

    object Error : ParaphraseResult
}

class OpenAIParaphrasor : Paraphrasor {
    override suspend fun paraphrase(phrase: String): ParaphraseResult {
        val service = OpenAiService(BuildConfig.OPEN_AI_TOKEN)
        val request = CompletionRequest.builder()
            .prompt("paraphrase \"$phrase\"")
            .model("text-davinci-003")
            .temperature(0.5)
            .maxTokens(200)
            .build()
        return withContext(Dispatchers.IO) {
            try {
                val result = service.createCompletion(request)
                ParaphraseResult.Success(
                    result.choices.first().text
                )
            } catch (t: Throwable) {
                ParaphraseResult.Error
            }
        }
    }
}