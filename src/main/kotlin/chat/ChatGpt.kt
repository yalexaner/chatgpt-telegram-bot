package chat

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.coroutineScope

/**
 * Proxy class to interact with OpenAI API
 */
@OptIn(BetaOpenAI::class)
class ChatGpt(apiKey: String) {

    private val openAI = OpenAI(apiKey)

    /**
     * Sends passed request to ChatGPT and gets generated result
     *
     * @param request A message that will be sent to ChatGPT
     * @return Generated result
     */
    suspend fun completeRequest(request: String): String? = coroutineScope {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId(MODEL_ID),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = request,
                )
            ),
            n = 1,
        )

        val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
        completion.choices.firstOrNull()?.message?.content
    }

    companion object {
        private const val MODEL_ID = "gpt-3.5-turbo"
    }
}