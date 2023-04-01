import chat.ChatGpt
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import telegram.telegramBot
import util.OPEN_AI_API_KEY
import util.TELEGRAM_BOT_TOKEN
import util.dotenv

private const val DEBUG = false

private lateinit var chatGpt: ChatGpt
private lateinit var bot: Bot

fun main() {
    val chatGptApiKey = if (DEBUG) {
        dotenv[OPEN_AI_API_KEY]
    } else {
        System.getenv(OPEN_AI_API_KEY)
    }

    val telegramBotToken = if (DEBUG) {
        dotenv[TELEGRAM_BOT_TOKEN]
    } else {
        System.getenv(TELEGRAM_BOT_TOKEN)
    }

    chatGpt = ChatGpt(chatGptApiKey)
    bot = telegramBot(telegramBotToken) {
        dispatchText(::dispatchText)
    }
    bot.startPolling()
}

private fun dispatchText(id: Long, text: String) = CoroutineScope(Dispatchers.IO).launch {
    bot.sendMessage(
        ChatId.fromId(id),
        text = chatGpt.completeRequest(text) ?: "```No message received```",
        parseMode = ParseMode.MARKDOWN,
    )
}