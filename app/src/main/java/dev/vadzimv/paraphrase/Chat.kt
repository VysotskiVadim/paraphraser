package dev.vadzimv.paraphrase

import com.theokanning.openai.OpenAiService
import com.theokanning.openai.completion.CompletionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface Chat {
    suspend fun request(request: ChatRequest): ChatResponse
}

data class ChatRequest(
    val text: String,
    val settings: ChatSettings = ChatSettings()
)

data class ChatSettings(
    val openAIToken: String = BuildConfig.OPEN_AI_TOKEN,
    val temperature: Double = 0.5,
    val model: String = "text-davinci-003",
    val maxTokens: Int = 200
)

sealed interface ChatResponse {
    data class Success(
        val reply: String
    ) : ChatResponse

    object Error : ChatResponse
}

class OpenAIParaphrasor : Chat {
    override suspend fun request(request: ChatRequest): ChatResponse {
        val service = OpenAiService(request.settings.openAIToken)
        val completionRequest = CompletionRequest.builder()
            .prompt(request.text)
            .model(request.settings.model)
            .temperature(request.settings.temperature)
            .maxTokens(request.settings.maxTokens)
            .build()
        return withContext(Dispatchers.IO) {
            try {
                val result = service.createCompletion(completionRequest)
                val text = result.choices.first().text
                ChatResponse.Success(
                    text.trim()
                )
            } catch (t: Throwable) {
                ChatResponse.Error
            }
        }
    }
}