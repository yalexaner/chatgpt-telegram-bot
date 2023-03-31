import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import util.OPEN_AI_API_KEY
import util.TELEGRAM_BOT_TOKEN

@OptIn(BetaOpenAI::class)
fun main() {
    val openAI = OpenAI(dotenv[OPEN_AI_API_KEY])
    val bot = bot {
        token = dotenv[TELEGRAM_BOT_TOKEN]
        dispatch {
            text {
                runBlocking {
                    launch {
                        val chatCompletionRequest = ChatCompletionRequest(
                            model = ModelId("gpt-3.5-turbo"),
                            messages = listOf(
                                ChatMessage(
                                    role = ChatRole.User,
                                    content = text,
                                )
                            )
                        )
                        val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
                        bot.sendMessage(
                            ChatId.fromId(message.chat.id),
                            text = completion.choices.firstOrNull()?.message?.content ?: "Nothing"
                        )
                    }
                }
            }
        }
    }
    bot.startPolling()
}

private val dotenv by lazy { dotenv() }